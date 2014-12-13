package instruction;

import java.util.HashSet;

import Register.RegisterFile;

public class IntegerQueue {
	
	HashSet<Instruction> instructions_r;
	HashSet<Instruction> instructions_n;
	RegisterFile regFile;
	
	public IntegerQueue(RegisterFile regFile) {
		this.instructions_r = new HashSet<Instruction>();
		this.instructions_n = new HashSet<Instruction>();
		this.regFile = regFile;
	}
	
	public boolean isFull() {
		return instructions_n.size() == 16;
	}
	
	public boolean isEmpty() {
		return instructions_r.isEmpty();
	}
	
	public boolean addInstruction(Instruction inst) {
		if(this.isFull()) {
			return false;
		} else {
			instructions_n.add(inst);
			return true;
		}
	}
	
	public void calc() {
		
	}
	
	public void edge() {
		instructions_r = new HashSet<Instruction>(instructions_n);
	}
	
	private void releaseInstruction(Instruction inst) {
		instructions_n.remove(inst);
	}
	

}
