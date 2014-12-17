package instruction;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import Register.PhysicalRegister;

public class ActiveList {
	
	final int ACTIVE_LIST_SIZE = 32;
	
	LinkedList<Instruction> instructionList_n;
	LinkedList<Instruction> instructionList_r;
	HashMap<Instruction, PhysicalRegister> destRegisters;
	HashMap<Instruction, PhysicalRegister> oldPhysRegs;
	
	public ActiveList() {
		this.instructionList_r = new LinkedList<Instruction>();
		this.instructionList_n = new LinkedList<Instruction>();
		this.destRegisters = new HashMap<Instruction, PhysicalRegister>();
		this.oldPhysRegs = new HashMap<Instruction, PhysicalRegister>();
	}
	
	public ActiveList(ActiveList activeList) {
		this.instructionList_r = new LinkedList<Instruction>(activeList.instructionList_r);
		this.instructionList_n = new LinkedList<Instruction>(activeList.instructionList_n);
		this.destRegisters = new HashMap<Instruction, PhysicalRegister>(activeList.getDestRegisters());
		this.oldPhysRegs = new HashMap<Instruction, PhysicalRegister>(activeList.getOldPhysRegs());
	}
	
	public boolean add(Instruction inst, PhysicalRegister pr, PhysicalRegister oldPr) {
		if(isFull()) {
			return false;
		}
		destRegisters.put(inst, pr);
		oldPhysRegs.put(inst, oldPr);
		return this.instructionList_n.add(inst);
	}
	
	public boolean addStore(StoreInstruction storeInst) {
		if(isFull()){
			return false;
		}
		return this.instructionList_n.add(storeInst);
	}
	
	public boolean addBranch(BranchInstruction branch) {
		if(isFull()) {
			return false;
		}
		return this.instructionList_n.add(branch);
	}
	
	public PhysicalRegister[] commitInstruction(Instruction inst) {
		PhysicalRegister[] prs = new PhysicalRegister[2];
		instructionList_n.remove(inst);
		prs[0] = destRegisters.remove(inst);
		prs[1] = oldPhysRegs.remove(inst);
		return prs;
	}
	
	public void commitStore(StoreInstruction storeInst) {
		instructionList_n.remove(storeInst);
	}
	
	public void commitBranch(BranchInstruction branch) {
		instructionList_n.remove(branch);
	}
	
	public int getPhysicalDestinationNum(Instruction inst) {
		return destRegisters.get(inst).getNumber();
	}
	
	public Instruction getFirstInstruction() {
		return this.instructionList_n.peekFirst();
	}
	
	public boolean isFull() {
		return instructionList_n.size() >= ACTIVE_LIST_SIZE;
	}
	
	public boolean isEmpty() {
		return instructionList_n.isEmpty();
	}
	
	public HashMap<Instruction, PhysicalRegister> getOldPhysRegs(){
		return this.oldPhysRegs;
	}
	
	public LinkedList<Instruction> getInstList() {
		return this.instructionList_r;
	}
	
	public HashMap<Instruction, PhysicalRegister> getDestRegisters() {
		return this.destRegisters;
	}
	
	public List<Instruction> purgeMispredict(BranchInstruction branch){
		LinkedList<Instruction> purgedInstructions = new LinkedList<Instruction>();
		for(Instruction inst : instructionList_r) {
			if(inst.dependsOn(branch)) {
				this.instructionList_n.remove(inst);
				this.destRegisters.remove(inst);
				this.oldPhysRegs.remove(inst);
				purgedInstructions.add(inst);
			}
		}
		return purgedInstructions;
	}
	
	public void edge(){
		this.instructionList_r = new LinkedList<Instruction>(this.instructionList_n);
	}

}
