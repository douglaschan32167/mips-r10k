package instruction;

public class StoreInstruction extends MemoryInstruction {

	public StoreInstruction(String op, int rs, int rt, int rd,
			String extraField, int lineNumber) {
		super(op, rs, rt, rd, extraField, lineNumber);
		// TODO Auto-generated constructor stub
	}
	
	public StoreInstruction(Instruction inst) {
		super(inst.getOp(), inst.getRs(), inst.getRt(), inst.getRd(), inst.getExtraField(), inst.getLineNumber());
		this.instString = inst.instString;
		this.fetchCycleNum = inst.getFetchCycleNum();
		this.decodeCycleNum = inst.getDecodeCycleNum();
	}
	
	@Override
	public boolean isStoreInstruction() {
		return true;
	}

}
