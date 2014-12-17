package instruction;

public class MemoryALU {

	boolean dispatchedToThisCycle;
	int cyclesToCompletion_n;
	int cyclesToCompletion_r;
	
	public MemoryALU() {
		this.dispatchedToThisCycle = false;
	}
	
	public MemoryInstruction execute(MemoryInstruction memInst) {
		this.dispatchedToThisCycle = true;
		return memInst;
	}
	
	public boolean canTakeDispatch() {
		return !this.dispatchedToThisCycle;
	}
	
	public void edge(){
		this.dispatchedToThisCycle = false;
		this.cyclesToCompletion_r = this.cyclesToCompletion_n;
	}

}
