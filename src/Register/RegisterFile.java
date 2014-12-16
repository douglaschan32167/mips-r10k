package Register;

import instruction.ActiveList;
import instruction.ArithmeticInstruction;
import instruction.BranchInstruction;
import instruction.BranchMask;
import instruction.FpInstruction;
import instruction.Instruction;
import instruction.LoadInstruction;
import instruction.MemoryInstruction;
import instruction.StoreInstruction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

public class RegisterFile {
	
	HashMap<Integer, PhysicalRegister> registerMap_r;
	HashMap<Integer, PhysicalRegister> registerMap_n;
	HashMap<Integer, PhysicalRegister> speculativeRegMap_r;
	HashMap<Integer, PhysicalRegister> speculativeRegMap_n;
	HashMap<Instruction, List<Integer>> physRegDependencies;
	HashSet<Instruction> instructionsReadyForCommit_n;
	HashSet<Instruction> instructionsReadyForCommit_r;
	HashSet<FpInstruction> packingFpInstructions_n;
	HashSet<FpInstruction> packingFpInstructions_r;
	LinkedList<PhysicalRegister> freeList_r;
	LinkedList<PhysicalRegister> freeList_n;
	ActiveList activeList_r;
	ActiveList activeList_n;
	boolean[] busyTable_r;
	boolean[] busyTable_n;
	boolean hasStarted = false;
	boolean mustPurgeMispredict_n;
	boolean mustPurgeMispredict_r;
	BranchInstruction instructionToPurge_n;
	BranchInstruction instructionToPurge_r;
	HashMap<BranchInstruction, HashMap<Integer, PhysicalRegister>> regMapScreenshots;
	HashMap<BranchInstruction, boolean[]> busyTableScreenshots;
	HashSet<BranchInstruction> committedBranches;
	LinkedList<Instruction> committedInstructions;
	BranchMask branchMask;
	
	public RegisterFile(){
		this.freeList_r = new LinkedList<PhysicalRegister>();
		this.registerMap_r = new HashMap<Integer, PhysicalRegister>();
		this.freeList_n = new LinkedList<PhysicalRegister>();
		this.registerMap_n = new HashMap<Integer, PhysicalRegister>();
		this.busyTable_r = new boolean[65];
		this.busyTable_n = new boolean[65];
		for(int i = 0; i < 65; i++) {
			freeList_r.add(new PhysicalRegister(i));
			busyTable_r[i] = true; //WHAT SHOULD I INITIALIZE THIS AS?
			busyTable_n[i] = true;
		}
		//Set r0 to be the 0 physical register, which should never change
		PhysicalRegister a = freeList_r.remove();
		System.out.println("register logical 0 is mapped to " + String.valueOf(a.getNumber()));
		registerMap_r.put(0, a);
		for (int i = 0; i < 31; i++) {
			registerMap_r.put(i+1, freeList_r.remove());
		}
		this.freeList_n = new LinkedList<PhysicalRegister>(this.freeList_r);
		this.registerMap_n = new HashMap<Integer, PhysicalRegister>(this.registerMap_r);
		this.speculativeRegMap_n = new HashMap<Integer, PhysicalRegister>(this.registerMap_r);
		this.speculativeRegMap_r = new HashMap<Integer, PhysicalRegister>(this.registerMap_r);
		this.activeList_r = new ActiveList();
		this.activeList_n = new ActiveList();
		this.instructionsReadyForCommit_n = new HashSet<Instruction>();
		this.instructionsReadyForCommit_r = new HashSet<Instruction>();
		this.physRegDependencies = new HashMap<Instruction, List<Integer>>();
		this.packingFpInstructions_n = new HashSet<FpInstruction>();
		this.packingFpInstructions_r = new HashSet<FpInstruction>();
		this.mustPurgeMispredict_n = false;
		this.mustPurgeMispredict_r = false;
		this.regMapScreenshots = new HashMap<BranchInstruction, HashMap<Integer, PhysicalRegister>>();
		this.busyTableScreenshots = new HashMap<BranchInstruction, boolean[]>();
		this.committedBranches = new HashSet<BranchInstruction>();
		this.branchMask = new BranchMask();
		this.committedInstructions = new LinkedList<Instruction>();
		
	}
	
	public void commitInstructions(int cycleNum) {
		for(int i = 0; i < 4; i++) {
			Instruction nextInstruction = activeList_n.getFirstInstruction();
			if(nextInstruction != null && this.instructionsReadyForCommit_r.contains(nextInstruction)) {
				nextInstruction.setCommitCycleNum(cycleNum);
				if(nextInstruction.isBranchInstruction()) {
					this.busyTableScreenshots.remove(nextInstruction);
					this.regMapScreenshots.remove(nextInstruction);
//					this.branchMask.commitBranch((BranchInstruction) nextInstruction); 
					this.activeList_n.commitBranch((BranchInstruction) nextInstruction);
				} else if(nextInstruction.isStoreInstruction()) {
					this.activeList_n.commitStore((StoreInstruction) nextInstruction);
				} else {
					PhysicalRegister[] prs = this.activeList_n.commitInstruction(nextInstruction);
					System.out.println("committed instruction");
					freeList_n.add(prs[1]);
					physRegDependencies.remove(nextInstruction);
					registerMap_n.put(nextInstruction.getRd(), prs[0]);
				}
				this.committedInstructions.add(nextInstruction);
			}
		}
		//TODO: Implement!
	}
	
	public void setReadyForCommit(Instruction inst) {
		int physDestNum = activeList_r.getPhysicalDestinationNum(inst);
		busyTable_n[physDestNum] = true;
		instructionsReadyForCommit_n.add(inst);
		System.out.println("Instruction ready for commit" + inst.getString());
	}
	
	public void setStoreReadyForCommit(StoreInstruction storeInst){
		instructionsReadyForCommit_n.add(storeInst);
		System.out.println("Instruction ready for commit" + storeInst.getString());
	}
	
	public void setBranchReadyForCommit(BranchInstruction branch) {
		this.branchMask.commitBranch(branch); 
		instructionsReadyForCommit_n.add(branch);
		System.out.println("Instruction ready for commit" + branch.getString());
	}
	
	public void setReadyToPack(FpInstruction fpInst){
		int physDestNum = activeList_r.getPhysicalDestinationNum(fpInst);
		busyTable_n[physDestNum] = true;
		packingFpInstructions_n.add(fpInst);
		System.out.println("ready to pack" + fpInst.getString());
	}
	
	public void packFps(int cycleNum) {
		for(FpInstruction fpInst : this.packingFpInstructions_r){
			fpInst.setPackingCycleNum(cycleNum);
			this.packingFpInstructions_n.remove(fpInst);
			instructionsReadyForCommit_n.add(fpInst);
			//DO I NEED MORE HERE?
		}
	}
	
	public int getPhysicalRegNum(int logicalRegNum) {
		return registerMap_r.get(logicalRegNum).getNumber();
	}
	
	public List<Integer> getPhysDeps(MemoryInstruction memInst) {
		return this.physRegDependencies.get(memInst);
	}
	
	/** Check if all the registers are ready in this instruction */
	public boolean checkRegisters(Instruction inst) {
		List<Integer> physDeps = physRegDependencies.get(inst);
		for(Integer i : physDeps) {
			if(!busyTable_r[i]) {
				System.out.println("register not ready" + String.valueOf(i));
				return false;
			}
		}
		return true;
	}
	
	//IS THIS NEEDED?
//	public boolean checkRegisters(MemoryInstruction inst) {
//		System.err.println("check registers Not implemented yet for memory instructions");
//		System.exit(1);
//		return false;
//	}
	
	public boolean addToActiveList(ArithmeticInstruction inst) {
		//this should remove a physical register from the freelist and then assign it to the instruction
		this.hasStarted = true;
		PhysicalRegister pr = freeList_n.remove();
		PhysicalRegister oldPr = speculativeRegMap_n.get(inst.getRd());
		ArrayList<Integer> physDeps = new ArrayList<Integer>();
		physDeps.add(speculativeRegMap_n.get(inst.getRd()).getNumber()); //TODO:Is this necessary?
		physDeps.add(speculativeRegMap_n.get(inst.getRs()).getNumber());
		physDeps.add(speculativeRegMap_n.get(inst.getRt()).getNumber());
		this.physRegDependencies.put(inst, physDeps);
		if(inst.getRd() != 0) {
			speculativeRegMap_n.put(inst.getRd(), pr);
		}
		busyTable_n[pr.getNumber()] = false;
		return activeList_n.add(inst, pr, oldPr);
	}
	
	public boolean addBranchToActiveList(BranchInstruction branch){
		this.hasStarted = true;
		ArrayList<Integer> physDeps = new ArrayList<Integer>();
		physDeps.add(speculativeRegMap_n.get(branch.getRs()).getNumber());
		physDeps.add(speculativeRegMap_n.get(branch.getRt()).getNumber());
		this.physRegDependencies.put(branch, physDeps);
		return activeList_n.addBranch(branch);
	}
	
	public boolean addLoadToActiveList(LoadInstruction loadInst){
		this.hasStarted = true;
		PhysicalRegister pr = freeList_n.remove();
		PhysicalRegister oldPr = speculativeRegMap_n.get(loadInst.getRt());
		ArrayList<Integer> physDeps = new ArrayList<Integer>();
		physDeps.add(speculativeRegMap_n.get(loadInst.getRt()).getNumber());//TODO: Is this necessary
		physDeps.add(speculativeRegMap_n.get(loadInst.getRs()).getNumber()); //Somehow 0 is 32
		this.physRegDependencies.put(loadInst, physDeps);
		this.speculativeRegMap_n.put(loadInst.getRt(), pr);
		System.out.println("setting bt " + String.valueOf(pr.getNumber()));
		busyTable_n[pr.getNumber()] = false;
		return activeList_n.add(loadInst, pr, oldPr);
	}
	
	public boolean addStoreToActiveList(StoreInstruction storeInst){
		this.hasStarted = true;
		ArrayList<Integer> physDeps = new ArrayList<Integer>();
		physDeps.add(speculativeRegMap_n.get(storeInst.getRt()).getNumber());//TODO: Is this necessary
		physDeps.add(speculativeRegMap_n.get(storeInst.getRs()).getNumber());
		this.physRegDependencies.put(storeInst, physDeps);
		return activeList_n.addStore(storeInst);
	}
	
	public boolean hasFreePhysRegisters() {
		return !this.freeList_n.isEmpty();
	}
	public void calc(int cycleNum) {
		if(cycleNum >= 6) {
			System.out.println("breakpoint");
		}
		if(mustPurgeMispredict()) {
			System.out.println("must purge mispredict");
			purgeMispredict(getMispredictedInstruction());
			return;
		}
		commitInstructions(cycleNum);
		packFps(cycleNum);
		//put stuff here
	}
	
	public void edge() {
		this.freeList_r = new LinkedList<PhysicalRegister>(this.freeList_n);
		this.registerMap_r = new HashMap<Integer, PhysicalRegister>(this.registerMap_n);
		for(int i = 0; i < 65; i++) {
			busyTable_r[i] = busyTable_n[i]; 
		}
		this.activeList_r = new ActiveList(this.activeList_n);
		this.instructionsReadyForCommit_r = new HashSet<Instruction>(this.instructionsReadyForCommit_n);
		this.speculativeRegMap_r = new HashMap<Integer, PhysicalRegister>(this.speculativeRegMap_n);
		this.packingFpInstructions_r = new HashSet<FpInstruction>(this.packingFpInstructions_n);
		this.mustPurgeMispredict_r = this.mustPurgeMispredict_n;
		this.mustPurgeMispredict_n = false;
		this.instructionToPurge_r = this.instructionToPurge_n;
		this.instructionToPurge_n = null;
		this.activeList_n.edge();
		
	}
	
	//TODO: REMOVE ME ONLY FOR TESTING.
	public boolean makePhysRegBusy(int physRegNum) {
		return freeList_n.remove(new PhysicalRegister(physRegNum));
	}
	
	public boolean isDone() {
		return this.hasStarted && activeList_r.isEmpty();
	}
	
	public boolean mustPurgeMispredict() {
		return this.mustPurgeMispredict_r;
	}
	public BranchInstruction getMispredictedInstruction(){
		return this.instructionToPurge_r;
	}
	
	public void reportMispredictedBranch(BranchInstruction branch){
		this.instructionToPurge_n = branch;
		this.mustPurgeMispredict_n = true;
	}
	
	public void setSnapshot(BranchInstruction branch) {
		this.regMapScreenshots.put(branch, this.registerMap_r);
		boolean[] btCopy = new boolean[65];
		for(int i = 0; i < busyTable_r.length; i++) {
			btCopy[i] = busyTable_r[i];
		}
		this.busyTableScreenshots.put(branch, btCopy);
	}
	
	public void purgeMispredict(BranchInstruction branch) {
		List<Instruction> purgedInstructions = this.activeList_n.purgeMispredict(branch);
		this.branchMask.purgeMispredict(branch);
		this.instructionsReadyForCommit_n.removeAll(purgedInstructions);
		this.packingFpInstructions_n.removeAll(purgedInstructions);
		for(Instruction inst: purgedInstructions) {
			this.busyTableScreenshots.remove(inst);
			this.regMapScreenshots.remove(inst);
		}
		boolean[] busyTableSnapshot = this.busyTableScreenshots.get(branch);
		this.registerMap_n = new HashMap<Integer, PhysicalRegister>(this.regMapScreenshots.get(branch));
		for (int i = 0; i < busyTableSnapshot.length; i++) {
			busyTable_n[i] = busyTableSnapshot[i];
		}
		this.speculativeRegMap_n = new HashMap<Integer, PhysicalRegister>(this.registerMap_n);
	}
	
	public BranchMask getBranchMask(){
		return this.branchMask;
	}
	

}
