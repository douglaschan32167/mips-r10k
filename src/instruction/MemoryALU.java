package instruction;

public class MemoryALU {

	boolean dispatchedToThisCycle;
	
	public MemoryALU() {
		this.dispatchedToThisCycle = false;
	}
	
	public boolean canTakeDispatch() {
		return !this.dispatchedToThisCycle;
	}
	

}
