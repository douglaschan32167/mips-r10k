package instruction;

public class BranchInstruction extends Instruction {

	boolean isMispredicted;
	
	public BranchInstruction(String op, int rs, int rt, int rd,
			String extraField, int lineNumber) {
		super(op, rs, rt, rd, extraField, lineNumber);
		this.isMispredicted = Integer.parseInt(extraField) == 1;
		// TODO Auto-generated constructor stub
	}

	public BranchInstruction(Instruction inst) {
		super(inst.getOp(), inst.getRs(), inst.getRt(), inst.getRd(), inst.getExtraField(), inst.getLineNumber());
		this.instString = inst.instString;
		this.isMispredicted = Integer.parseInt(extraField) == 1;
	}
	
	@Override
	public boolean isBranchInstruction(){
		return true;
	}
	
	public void setIsMispredicted(boolean b) {
		this.isMispredicted = b;
	}
	
	public boolean isMispredicted(){
		return this.isMispredicted;
	}
}
