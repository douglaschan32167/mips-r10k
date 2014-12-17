package instruction;

public class FpALU {
	
	FpInstruction inst_n;
	FpInstruction inst_r;
	int cyclesToCompletion_n;
	int cyclesToCompletion_r;
	boolean hasGottenDispatch;
	FpInstruction currInst;
	boolean hasExecuted;
	
	public FpALU(){
		this.cyclesToCompletion_r = 0;
		this.cyclesToCompletion_n = 0;
		this.hasGottenDispatch = false;
		this.hasExecuted = false;
	}
	
	public FpInstruction executeInstruction(){
		int timeLeft = this.cyclesToCompletion_r - 1;
		if(timeLeft == 0) {
			this.cyclesToCompletion_n = timeLeft;
			return inst_r;
		} else {
			cyclesToCompletion_n = timeLeft;
			return null;
		}
	}
	
	public void execute1(FpInstruction fpInst) {
		this.currInst = fpInst;
		this.hasExecuted = true;
	}
	
	public boolean canTakeDispatch() {
		return !this.hasExecuted;
//		return cyclesToCompletion_r <= 1 && !this.hasGottenDispatch;
	}
	
	public FpInstruction execute2() {
		FpInstruction tempInst = this.currInst;
		if(this.currInst != null) {
			this.hasExecuted = true;
			this.currInst = null;
		}
		return tempInst;
	}
	
	public void setNextInstruction(FpInstruction fpi) {
		this.inst_n = fpi;
		this.cyclesToCompletion_n = 2;
		this.hasGottenDispatch = true;
	}
	
	public int getCyclesToCompletion() {
		return this.cyclesToCompletion_r;
	}
	
	public FpInstruction getCurrInst() {
		return this.inst_r;
	}
	
	public void edge() {
		this.inst_r = this.inst_n;
		this.hasGottenDispatch = false;
//		this.inst_n = null;
		this.cyclesToCompletion_r = this.cyclesToCompletion_n;
		this.hasExecuted = false;
	}

}
