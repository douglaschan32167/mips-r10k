package instruction;

public class FpInstruction extends Instruction {

	public FpInstruction(String op, int rs, int rt, int rd, String extraField,
			int lineNumber) {
		super(op, rs, rt, rd, extraField, lineNumber);
		// TODO Auto-generated constructor stub
	}
	
	public FpInstruction(Instruction inst) {
		super(inst.getOp(), inst.getRs(), inst.getRt(), inst.getRd(), inst.getExtraField(), inst.getLineNumber());
	}

}
