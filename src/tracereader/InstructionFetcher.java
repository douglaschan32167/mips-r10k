package tracereader;

import instruction.AddressQueue;
import instruction.FpInstruction;
import instruction.FpQueue;
import instruction.Instruction;
import instruction.IntInstruction;
import instruction.IntegerQueue;
import instruction.LoadInstruction;

import java.util.ArrayList;

public class InstructionFetcher {
	TraceReader traceReader;
	ArrayList<Instruction> instructionList;
	ArrayList<Instruction> instructionsRemaining_r;
	ArrayList<Instruction> instructionsRemaining_n;
	ArrayList<Instruction> instructionsToIssue_r;
	ArrayList<Instruction> instructionsToIssue_n;
	ArrayList<String> fpCodes;
	ArrayList<String> loadCodes;
	ArrayList<String> storeCodes;
	ArrayList<String> intCodes;
	IntegerQueue intQueue;
	FpQueue fpQueue;
	AddressQueue addressQueue;
	
	public InstructionFetcher(String tracePath, IntegerQueue intQueue, FpQueue fpQueue, AddressQueue addressQueue) {
		this.traceReader = new TraceReader(tracePath);
		this.instructionList = this.traceReader.readTrace();
		this.instructionsRemaining_r = new ArrayList<Instruction>(instructionList);
		this.instructionsRemaining_n = new ArrayList<Instruction>(instructionList);
		this.instructionsToIssue_n = new ArrayList<Instruction>();
		this.instructionsToIssue_r = new ArrayList<Instruction>();
		this.intQueue = intQueue;
		this.fpQueue = fpQueue;
		this.addressQueue = addressQueue;
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
	public void calc() {
		for(int i = 0; i < instructionsToIssue_r.size(); i++) {
			Instruction nextInstruction = instructionsToIssue_r.get(i);
			if(intCodes.contains(nextInstruction.getOp())) {
				IntInstruction intInst = new IntInstruction(nextInstruction);
				intQueue.addInstruction(intInst);
				instructionsToIssue_n.remove(nextInstruction);
			} else if (fpCodes.contains(nextInstruction.getOp())){
				FpInstruction fpInst = new FpInstruction(nextInstruction);
				fpQueue.addInstruction(fpInst);
				instructionsToIssue_n.remove(nextInstruction);
			} else if(loadCodes.contains(nextInstruction.getOp())) {
				LoadInstruction loadInst = new LoadInstruction(nextInstruction);
				addressQueue.addInstruction(loadInst);
				instructionsToIssue_n.remove(nextInstruction);
			} else {
				System.err.println("Instruction not supported yet" + nextInstruction.getOp());
				System.exit(1);
			}
//			if (intQueue.addInstruction(nextInstruction)) {
//				instructionsToIssue_n.remove(nextInstruction);
//			}
		}
		while(instructionsToIssue_n.size() < 4 && !instructionsRemaining_n.isEmpty()) {
			instructionsToIssue_n.add(instructionsRemaining_n.remove(0));
		}
	}
	
	public void edge() {
		this.instructionsToIssue_r = new ArrayList<Instruction>(this.instructionsToIssue_n);
		this.instructionsRemaining_r = new ArrayList<Instruction>(this.instructionsRemaining_n);
	}

}
