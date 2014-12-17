package instruction;

import java.util.LinkedList;

import Register.RegisterFile;

public class FpQueue {

	LinkedList<FpInstruction> instructions_r;
	LinkedList<FpInstruction> instructions_n;
	FpALU fpAdder;
	FpALU fpMul;
	RegisterFile regFile;
	
	public FpQueue(RegisterFile regFile) {
		this.regFile = regFile;
		this.instructions_r = new LinkedList<FpInstruction>();
		this.instructions_n = new LinkedList<FpInstruction>();
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
	public void calc(int cycleNum) {
		FpInstruction completed1 = fpAdder.executeInstruction();
		FpInstruction completed2 = fpMul.executeInstruction();
		if(completed1 != null) {
			completed1.setExecuteCycleNum(cycleNum);
			regFile.setReadyToPack(completed1);
		}
		if(completed2 != null) {
			completed2.setExecuteCycleNum(cycleNum);
			regFile.setReadyToPack(completed2);
		}
		if(regFile.mustPurgeMispredict()) {
			purgeMispredict(this.regFile.getMispredictedInstruction());
			return;
		}
		for(FpInstruction inst : instructions_r) {
			if(inst.getType().equals("M") && fpMul.canTakeDispatch() && this.regFile.checkRegisters(inst)) {
				inst.setIssueCycleNum(cycleNum);
				fpMul.setNextInstruction(inst);
				this.instructions_n.remove(inst);
			}
			if(inst.getType().equals("A") && fpAdder.canTakeDispatch() && this.regFile.checkRegisters(inst)) {
				inst.setIssueCycleNum(cycleNum);
				fpAdder.setNextInstruction(inst);
				this.instructions_n.remove(inst);
			}
		}
	}
	
	public void edge() {
		instructions_r = new LinkedList<FpInstruction>(instructions_n);
		fpAdder.edge();
		fpMul.edge();
	}
	
	public void purgeMispredict(BranchInstruction branch) {
		for(Instruction inst : this.instructions_n) {
			if(inst.dependsOn(branch)){
				this.instructions_n.remove(inst);
			}
		}
	}
	
	private void releaseInstruction(Instruction inst) {
		instructions_n.remove(inst);
	}
	
	private void dispatchToAlu(Instruction inst) {
	}
}
