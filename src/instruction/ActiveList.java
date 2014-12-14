package instruction;

import java.util.HashMap;
import java.util.LinkedList;

import Register.PhysicalRegister;

public class ActiveList {
	
	final int ACTIVE_LIST_SIZE = 32;
	
	LinkedList<Instruction> instructionList;
	HashMap<Instruction, PhysicalRegister> destRegisters;
	HashMap<Instruction, PhysicalRegister> oldPhysRegs;
	
	public ActiveList() {
		this.instructionList = new LinkedList<Instruction>();
		this.destRegisters = new HashMap<Instruction, PhysicalRegister>();
		this.oldPhysRegs = new HashMap<Instruction, PhysicalRegister>();
	}
	
	public ActiveList(ActiveList activeList) {
		this.instructionList = new LinkedList<Instruction>(activeList.instructionList);
		this.destRegisters = new HashMap<Instruction, PhysicalRegister>(activeList.getDestRegisters());
	}
	
	public boolean add(Instruction inst, PhysicalRegister pr, PhysicalRegister oldPr) {
		if(isFull()) {
			return false;
		}
		destRegisters.put(inst, pr);
		oldPhysRegs.put(inst, oldPr);
		return this.instructionList.add(inst);
	}
	
	public PhysicalRegister[] commitInstruction(Instruction inst) {
		PhysicalRegister[] prs = new PhysicalRegister[2];
		instructionList.remove(inst);
		prs[0] = destRegisters.remove(inst);
		prs[1] = oldPhysRegs.remove(inst);
		return prs;
	}
	
	public int getPhysicalDestinationNum(Instruction inst) {
		return destRegisters.get(inst).getNumber();
	}
	
	public Instruction getFirstInstruction() {
		return this.instructionList.peekFirst();
	}
	
	public boolean isFull() {
		return instructionList.size() >= ACTIVE_LIST_SIZE;
	}
	
	public boolean isEmpty() {
		return instructionList.isEmpty();
	}
	
	public LinkedList<Instruction> getInstList() {
		return this.instructionList;
	}
	
	public HashMap<Instruction, PhysicalRegister> getDestRegisters() {
		return this.destRegisters;
	}

}
