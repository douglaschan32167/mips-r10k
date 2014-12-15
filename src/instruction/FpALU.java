package instruction;

public class FpALU {
	
	FpInstruction inst_n;
	FpInstruction inst_r;
	int cyclesToCompletion_n;
	int cyclesToCompletion_r;
	boolean hasGottenDispatch;
	
	public FpALU(){
		this.cyclesToCompletion_r = 0;
		this.cyclesToCompletion_n = 0;
		this.hasGottenDispatch = false;
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
	
	public boolean canTakeDispatch() {
		return cyclesToCompletion_r <= 1 && !this.hasGottenDispatch;
	}
	
	public void setNextInstruction(FpInstruction fpi) {
		this.inst_n = fpi;
		this.cyclesToCompletion_n = 2;
		this.hasGottenDispatch = true;
	}
	
	public void edge() {
		this.inst_r = this.inst_n;
		this.hasGottenDispatch = false;
//		this.inst_n = null;
		this.cyclesToCompletion_r = this.cyclesToCompletion_n;
	}

}
