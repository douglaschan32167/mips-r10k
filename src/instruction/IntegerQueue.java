package instruction;

import java.util.HashSet;
import java.util.LinkedList;

import Register.RegisterFile;

public class IntegerQueue {
	
	LinkedList<IntInstruction> instructions_r;
	LinkedList<IntInstruction> instructions_n;
	IntegerALU intAlu1;
	IntegerALU intAlu2;
	RegisterFile regFile;
	
	public IntegerQueue(RegisterFile regFile) {
		this.instructions_r = new LinkedList<IntInstruction>();
		this.instructions_n = new LinkedList<IntInstruction>();
		this.intAlu1 = new IntegerALU();
		this.intAlu2 = new IntegerALU();
		this.regFile = regFile;
	}
	
	public boolean isFull() {
		return instructions_n.size() == 16;
	}
	
	public boolean isEmpty() {
		return instructions_r.isEmpty();
	}
	
	public boolean addInstruction(IntInstruction inst) {
		if(this.isFull()) {
			return false;
		} else if (regFile.addToActiveList(inst)) {
			instructions_n.add(inst);
			return true;
		} else {
			return false;
		}
	}
	
	public void calc() {
		Instruction completed1 = intAlu1.executeInstruction();
		Instruction completed2 = intAlu2.executeInstruction();
		if(completed1 != null) {
			regFile.setReadyForCommit(completed1);
		}
		if(completed2 != null) {
			regFile.setReadyForCommit(completed2);
		}
		int numDispatched = 0;
		for(IntInstruction inst : instructions_r) {
			if(regFile.checkRegisters(inst)) {
				dispatchToAlu(inst);
				numDispatched += 1;
				instructions_n.remove(inst);
				if (numDispatched == 2) {
					return;
				}
			}
		}
	}
	
	public void edge() {
		instructions_r = new LinkedList<IntInstruction>(instructions_n);
		intAlu1.edge();
		intAlu2.edge();
	}
	
	private void releaseInstruction(Instruction inst) {
		instructions_n.remove(inst);
	}
	
	private void dispatchToAlu(Instruction inst) {
		if(!intAlu1.hasInstThisCycle()) {
			intAlu1.setNextInstruction(inst);
			System.out.println("dispatched to int alu 1");
		} else if(!intAlu2.hasInstThisCycle()) {
			intAlu2.setNextInstruction(inst);
			System.out.println("dispatched to int alu 2");
		}
	}
	

}
