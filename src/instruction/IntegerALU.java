package instruction;

public class IntegerALU {

	Instruction inst_r;
	Instruction inst_n;
	boolean instThisCycle;
	
	public IntegerALU() {
		
	}
	
	public void setNextInstruction(Instruction inst){
		this.inst_n = inst;
		this.instThisCycle = true;
	}
	
	public Instruction executeInstruction() {
		return inst_r;
	}
	
	public boolean hasInstThisCycle() {
		return this.instThisCycle;
	}
	
	public void edge() {
		inst_r = inst_n;
		inst_n = null;
		this.instThisCycle = false;
	}
}
