package instruction;

public class IntInstruction extends Instruction {

	public IntInstruction(String op, int rs, int rt, int rd, String extraField,
			int lineNumber) {
		super(op, rs, rt, rd, extraField, lineNumber);
		// TODO Auto-generated constructor stub
	}
	
	public IntInstruction(Instruction inst) {
		super(inst.getOp(), inst.getRs(), inst.getRt(), inst.getRd(), inst.getExtraField(), inst.getLineNumber());
	}


}
