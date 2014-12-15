package Register;

import instruction.ActiveList;
import instruction.ArithmeticInstruction;
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
		registerMap_r.put(0, freeList_r.remove());
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
		
	}
	
	public void commitInstructions() {
		for(int i = 0; i < 4; i++) {
			Instruction nextInstruction = activeList_n.getFirstInstruction();
			if(nextInstruction != null && this.instructionsReadyForCommit_r.contains(nextInstruction)) {
				if(nextInstruction.isStoreInstruction()) {
					this.activeList_n.commitStore((StoreInstruction) nextInstruction);
				} else {
					PhysicalRegister[] prs = this.activeList_n.commitInstruction(nextInstruction);
					System.out.println("committed instruction");
					freeList_n.add(prs[1]);
					physRegDependencies.remove(nextInstruction);
					registerMap_n.put(nextInstruction.getRd(), prs[0]);
				}
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
	
	public void setReadyToPack(FpInstruction fpInst){
		int physDestNum = activeList_r.getPhysicalDestinationNum(fpInst);
		busyTable_n[physDestNum] = true;
		packingFpInstructions_n.add(fpInst);
	}
	
	public void packFps() {
		for(FpInstruction fpInst : this.packingFpInstructions_r){
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
		speculativeRegMap_n.put(inst.getRd(), pr);
		busyTable_n[pr.getNumber()] = false;
		return activeList_n.add(inst, pr, oldPr);
	}
	
	public boolean addLoadToActiveList(LoadInstruction loadInst){
		this.hasStarted = true;
		PhysicalRegister pr = freeList_n.remove();
		PhysicalRegister oldPr = speculativeRegMap_n.get(loadInst.getRt());
		ArrayList<Integer> physDeps = new ArrayList<Integer>();
		physDeps.add(speculativeRegMap_n.get(loadInst.getRt()).getNumber());//TODO: Is this necessary
		physDeps.add(speculativeRegMap_n.get(loadInst.getRs()).getNumber());
		this.physRegDependencies.put(loadInst, physDeps);
		this.speculativeRegMap_n.put(loadInst.getRt(), pr);
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
	public void calc() {
		commitInstructions();
		packFps();
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
		
	}
	
	//TODO: REMOVE ME ONLY FOR TESTING.
	public boolean makePhysRegBusy(int physRegNum) {
		return freeList_n.remove(new PhysicalRegister(physRegNum));
	}
	
	public boolean isDone() {
		return this.hasStarted && activeList_r.isEmpty();
	}
	
	
	

}
