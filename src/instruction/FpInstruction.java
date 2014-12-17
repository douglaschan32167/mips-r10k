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
		this.decodeCycleNum = inst.getDecodeCycleNum();
		this.fetchCycleNum = inst.getFetchCycleNum();
	}
	
	public void setPackingCycleNum(int num) {
		this.packingCycleNum = num;
	}
	
	
	@Override
	public boolean isFpInstruction(){
		return true;
	}
	public int getPackingCycleNum(){
		return this.packingCycleNum;
	}

}
