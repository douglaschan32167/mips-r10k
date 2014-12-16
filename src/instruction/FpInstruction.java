package instruction;

public class FpInstruction extends ArithmeticInstruction {
	
	int packingCycleNum;

	public FpInstruction(String op, int rs, int rt, int rd, String extraField,
			int lineNumber) {
		super(op, rs, rt, rd, extraField, lineNumber);
		// TODO Auto-generated constructor stub
	}
	
	public FpInstruction(Instruction inst) {
		super(inst.getOp(), inst.getRs(), inst.getRt(), inst.getRd(), inst.getExtraField(), inst.getLineNumber());
		this.instString = inst.instString;
	}
	
	public void setPackingCycleNum(int num) {
		this.packingCycleNum = num;
	}
	
	public int getPackingCycleNum(){
		return this.packingCycleNum;
	}

}
