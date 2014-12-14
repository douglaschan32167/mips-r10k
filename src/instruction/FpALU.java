package instruction;

public class FpALU {
	
	FpInstruction inst_n;
	FpInstruction inst_r;
	int cyclesToCompletion_n;
	int cyclesToCompletion_r;
	
	public FpALU(){
		this.cyclesToCompletion_r = 0;
		this.cyclesToCompletion_n = 0;
	}
	
	public FpInstruction executeInstruction(){
		int timeLeft = this.cyclesToCompletion_r - 1;
		if(timeLeft == 0) {
			return inst_r;
		} else {
			cyclesToCompletion_n = timeLeft;
			return null;
		}
	}
	
	public boolean canTakeDispatch() {
		return cyclesToCompletion_r <= 1 && this.inst_n == null;
	}
	
	public void setNextInstruction(FpInstruction fpi) {
		this.inst_n = fpi;
		this.cyclesToCompletion_n = 2;
	}
	
	public void edge() {
		this.inst_r = this.inst_n;
		this.inst_n = null;
		this.cyclesToCompletion_r = this.cyclesToCompletion_n;
	}

}
