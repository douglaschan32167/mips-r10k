package instruction;

import java.util.HashSet;

import Register.RegisterFile;

public class FpQueue {

	HashSet<FpInstruction> instructions_r;
	HashSet<FpInstruction> instructions_n;
	FpALU fpAdder;
	FpALU fpMul;
	RegisterFile regFile;
	
	public FpQueue(RegisterFile regFile) {
		this.regFile = regFile;
		this.instructions_r = new HashSet<FpInstruction>();
		this.instructions_n = new HashSet<FpInstruction>();
		this.fpAdder = new FpALU();
		this.fpMul = new FpALU();
	}
	
	public boolean isFull() {
		return instructions_n.size() == 16;
	}
	
	public boolean isEmpty() {
		return instructions_r.isEmpty();
	}
	
	public boolean addInstruction(FpInstruction inst) {
		if(this.isFull()) {
			return false;
		} else if (regFile.addToActiveList(inst)) {
			instructions_n.add(inst);
			return true;
		} else {
			return false;
		}
	}
	
	//TODO: Change this to be add and mul specific
	public void calc() {
		FpInstruction completed1 = fpAdder.executeInstruction();
		FpInstruction completed2 = fpMul.executeInstruction();
		if(completed1 != null) {
			regFile.setReadyForCommit(completed1);
		}
		if(completed2 != null) {
			regFile.setReadyForCommit(completed2);
		}
		for(FpInstruction inst : instructions_r) {
			if(inst.getType().equals("M") && fpMul.canTakeDispatch()) {
				fpMul.setNextInstruction(inst);
			}
			if(inst.getType().equals("A") && fpAdder.canTakeDispatch()) {
				fpAdder.setNextInstruction(inst);
			}
		}
	}
	
	public void edge() {
		instructions_r = new HashSet<FpInstruction>(instructions_n);
		fpAdder.edge();
		fpMul.edge();
	}
	
	private void releaseInstruction(Instruction inst) {
		instructions_n.remove(inst);
	}
	
	private void dispatchToAlu(Instruction inst) {
	}
}
