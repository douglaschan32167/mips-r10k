package instruction;

public class FpALU {
	
	FpInstruction inst_n;
	FpInstruction inst_r;
	int cyclesToCompletion_n;
	int cyclesToCompletion_r;
	boolean hasGottenDispatch;
	FpInstruction currInst;
	boolean hasExecuted1;
	boolean hasExecuted2;
	
	public FpALU(){
		this.cyclesToCompletion_r = 0;
		this.cyclesToCompletion_n = 0;
		this.hasGottenDispatch = false;
		this.hasExecuted1 = false;
		this.hasExecuted2 = false;
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
		this.hasExecuted1 = true;
	}
	
	public boolean canTakeDispatch() {
		return !this.hasExecuted1;
//		return cyclesToCompletion_r <= 1 && !this.hasGottenDispatch;
	}
	
	public boolean canExecute2() {
		return !this.hasExecuted2;
	}
	
	public FpInstruction execute2() {
		FpInstruction tempInst = this.currInst;
		if(this.currInst != null) {
			this.hasExecuted2 = true;
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
		this.hasExecuted1 = false;
		this.hasExecuted2 = false;
	}
	
	public void purgeMispredict() {
		this.currInst = null;
	}

}
