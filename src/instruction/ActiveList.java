package instruction;

import java.util.LinkedList;

public class ActiveList {
	
	final int ACTIVE_LIST_SIZE = 32;
	
	LinkedList<Instruction> instructionList;
	
	public ActiveList() {
		this.instructionList = new LinkedList<Instruction>();
	}
	
	public ActiveList(LinkedList<Instruction> instList) {
		this.instructionList = new LinkedList<Instruction>(instList);
	}
	
	public ActiveList(ActiveList activeList) {
		this.instructionList = new LinkedList<Instruction>(activeList.instructionList);
	}
	
	public boolean add(Instruction inst) {
		if(isFull()) {
			return false;
		}
		
		return this.instructionList.add(inst);
	}
	
	public boolean remove(Instruction inst) {
		return instructionList.remove(inst);
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

}
