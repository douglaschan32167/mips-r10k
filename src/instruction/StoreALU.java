package instruction;

public class StoreALU extends MemoryALU {
	
	StoreInstruction inst_n;
	StoreInstruction inst_r;
	
	public StoreALU() {
		super();
	}
	
	public StoreInstruction execute(StoreInstruction storeInst) {
		this.dispatchedToThisCycle = true;
		return storeInst;
	}
	public void setNextInstruction(StoreInstruction inst) {
		this.inst_n = inst;
		this.dispatchedToThisCycle = true;
	}
	
	public StoreInstruction executeInstruction() {
		return this.inst_r;
	}
	
	public void edge() {
		this.dispatchedToThisCycle = false;
		this.inst_r = this.inst_n;
		this.inst_n = null;
	}

}
