package tracereader;

import instruction.AddressQueue;
import instruction.BranchInstruction;
import instruction.BranchMask;
import instruction.FpInstruction;
import instruction.FpQueue;
import instruction.Instruction;
import instruction.IntInstruction;
import instruction.IntegerQueue;
import instruction.LoadInstruction;
import instruction.StoreInstruction;

import java.util.ArrayList;
import java.util.LinkedList;

import Register.RegisterFile;

public class InstructionFetcher {
	TraceReader traceReader;
	ArrayList<Instruction> instructionList;
	LinkedList<Instruction> instructionsRemaining_r;
	LinkedList<Instruction> instructionsRemaining_n;
	ArrayList<Instruction> instructionsToIssue_r;
	ArrayList<Instruction> instructionsToIssue_n;
	ArrayList<Instruction> fetchedInstructions_n;
	ArrayList<Instruction> fetchedInstructions_r;
	ArrayList<String> fpCodes;
	ArrayList<String> loadCodes;
	ArrayList<String> storeCodes;
	ArrayList<String> intCodes;
	ArrayList<String> branchCodes;
//	BranchMask branchMask;
	IntegerQueue intQueue;
	FpQueue fpQueue;
	AddressQueue addressQueue;
	RegisterFile regFile;
	
	public InstructionFetcher(String tracePath, IntegerQueue intQueue, FpQueue fpQueue, AddressQueue addressQueue, RegisterFile regFile) {
//		this.branchMask = new BranchMask();
		this.traceReader = new TraceReader(tracePath);
		this.instructionList = this.traceReader.readTrace();
		this.instructionsRemaining_r = new LinkedList<Instruction>(instructionList);
		this.instructionsRemaining_n = new LinkedList<Instruction>(instructionList);
		this.instructionsToIssue_n = new ArrayList<Instruction>();
		this.instructionsToIssue_r = new ArrayList<Instruction>();
		this.fetchedInstructions_n = new ArrayList<Instruction>();
		this.fetchedInstructions_r = new ArrayList<Instruction>();
		this.intQueue = intQueue;
		this.fpQueue = fpQueue;
		this.addressQueue = addressQueue;
		this.regFile = regFile;
		this.fpCodes = new ArrayList<String>();
		this.fpCodes.add("M");
		this.fpCodes.add("m");
		this.fpCodes.add("fmul");
		this.fpCodes.add("A");
		this.fpCodes.add("a");
		this.fpCodes.add("fpadd");
		this.loadCodes = new ArrayList<String>();
		this.loadCodes.add("L");
		this.loadCodes.add("l");
		this.loadCodes.add("load");
		this.loadCodes.add("ld");
		this.storeCodes = new ArrayList<String>();
		this.storeCodes.add("S");
		this.storeCodes.add("s");
		this.storeCodes.add("st");
		this.intCodes = new ArrayList<String>();
		this.intCodes.add("I");
		this.intCodes.add("i");
		this.intCodes.add("add");
		this.intCodes.add("sub");
		this.branchCodes = new ArrayList<String>();
		this.branchCodes.add("b");
		this.branchCodes.add("B");
		this.branchCodes.add("branch");
	}
	
	public ArrayList<Instruction> decode(int intCapacity, int addrCapacity, int fpCapacity) {
		ArrayList<Instruction> result = new ArrayList<Instruction>();
		if(intCapacity > 0) {
			Instruction nextInstruction = this.instructionsRemaining_r.get(0);
			result.add(nextInstruction);
			this.instructionsRemaining_n.remove(nextInstruction);
			
		}
		return result;
		
	}
	
	//TODO: Change this to actually check if the int queue is full and if the activelist is full
	public void calc(int cycleNum) {
		int numDecoded = 0;
		if(this.regFile.mustPurgeMispredict()) {
			System.out.println("must purge mispredict instfetch");
			purgeMispredict(this.regFile.getMispredictedInstruction());
			return;
		}
		for(int i = 0; i < instructionsToIssue_r.size(); i++) {

			if(numDecoded == 4) {
				break;
			}
			Instruction nextInstruction = instructionsToIssue_r.get(i);
			if(intCodes.contains(nextInstruction.getOp())) {
				IntInstruction intInst = new IntInstruction(nextInstruction);
				intInst.setBranchDependencies(regFile.getBranchMask().getBranchDependencies());
				intInst.setIssueCycleNum(cycleNum);
				if(intQueue.addInstruction(intInst)){
					instructionsToIssue_n.remove(nextInstruction);
					numDecoded += 1;
				}
			} else if (fpCodes.contains(nextInstruction.getOp())){
				FpInstruction fpInst = new FpInstruction(nextInstruction);
				fpInst.setBranchDependencies(regFile.getBranchMask().getBranchDependencies());
				fpInst.setIssueCycleNum(cycleNum);
				if(fpQueue.addInstruction(fpInst)) {
					instructionsToIssue_n.remove(nextInstruction);
					numDecoded += 1;
				}
				//TODO: Should I break?
			} else if(loadCodes.contains(nextInstruction.getOp())) {
				LoadInstruction loadInst = new LoadInstruction(nextInstruction);
				loadInst.setBranchDependencies(regFile.getBranchMask().getBranchDependencies());
				loadInst.setIssueCycleNum(cycleNum);
				if (addressQueue.addInstruction(loadInst)) {
					instructionsToIssue_n.remove(nextInstruction);
					numDecoded +=1;
				}
			} else if(storeCodes.contains(nextInstruction.getOp())) {
				StoreInstruction storeInst = new StoreInstruction(nextInstruction);
				storeInst.setBranchDependencies(regFile.getBranchMask().getBranchDependencies());
				storeInst.setIssueCycleNum(cycleNum);
				if(addressQueue.addInstruction(storeInst)) {
					instructionsToIssue_n.remove(nextInstruction);
					numDecoded += 1;
				}
			} else if(branchCodes.contains(nextInstruction.getOp())){
				BranchInstruction branch = new BranchInstruction(nextInstruction);
				branch.setBranchDependencies(regFile.getBranchMask().getBranchDependencies());
				branch.setIssueCycleNum(cycleNum);
				if(regFile.getBranchMask().isFull()){
					break;
				}
				if(intQueue.addInstruction(branch)) {
					instructionsToIssue_n.remove(nextInstruction);
					regFile.getBranchMask().addBranch(branch);
					regFile.setSnapshot(branch);
					numDecoded += 1;
				}
			} else {
				System.err.println("Instruction not supported yet" + nextInstruction.getOp());
				System.exit(1);
			}
//			if (intQueue.addInstruction(nextInstruction)) {
//				instructionsToIssue_n.remove(nextInstruction);
//			}
		}
		if (this.regFile.mustPurgeMispredict()) {
			return;
		}
		int numFetched = 0;
		while(instructionsToIssue_n.size() < 4 && !fetchedInstructions_n.isEmpty()) {
			Instruction nextInst = fetchedInstructions_n.remove(0);
			nextInst.setDecodeCycleNum(cycleNum);
			instructionsToIssue_n.add(nextInst);
		}
		while(!this.instructionsRemaining_n.isEmpty() && numFetched < 4) {
			Instruction fetchInst = this.instructionsRemaining_n.remove(0);
			fetchInst.setFetchCycleNum(cycleNum);
			this.fetchedInstructions_n.add(fetchInst);
			numFetched += 1;
		}
	}
	
	public void edge() {
		this.instructionsToIssue_r = new ArrayList<Instruction>(this.instructionsToIssue_n);
		this.instructionsRemaining_r = new LinkedList<Instruction>(this.instructionsRemaining_n);
		this.fetchedInstructions_r = new ArrayList<Instruction>(this.fetchedInstructions_n);
	}
	
	public void purgeMispredict(BranchInstruction branch) {
		this.instructionsRemaining_n = new LinkedList<Instruction>();
		for(Instruction inst : instructionList) {
			if(inst.getLineNumber() > branch.getLineNumber()) {
				this.instructionsRemaining_n.addLast(inst);
			}
		}
		branch.setIsMispredicted(false);
		branch.setExtraField("0");
		this.instructionsToIssue_n = new ArrayList<Instruction>();
		this.instructionsRemaining_n.addFirst(new BranchInstruction(branch));
		this.fetchedInstructions_n = new ArrayList<Instruction>();
	}
	
	public boolean isDone() {
		return this.instructionsRemaining_r.isEmpty() && this.instructionsToIssue_r.isEmpty() && this.fetchedInstructions_r.isEmpty();
	}

}
