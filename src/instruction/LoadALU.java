package instruction;

public class LoadALU extends MemoryALU {
	
	LoadInstruction inst_n;
	LoadInstruction inst_r;
	
	public LoadALU() {
		super();
	}
	
	public LoadInstruction execute(LoadInstruction inst) {
		this.dispatchedToThisCycle = true;
		return inst;
	}
	
	public void setNextInstruction(LoadInstruction inst) {
		this.inst_n = inst;
		this.dispatchedToThisCycle = true;
	}
	
	public LoadInstruction executeInstruction() {
		return this.inst_r;
	}
	
	public LoadInstruction getCurrInst() {
		return this.inst_r;
	}
	
	public int getCyclesToCompletion() {
		return this.cyclesToCompletion_r;
	}
	
	public void edge() {
		this.dispatchedToThisCycle = false;
		this.inst_r = this.inst_n;
		this.inst_n = null;
	}

}
