package instruction;

public class MemoryInstruction extends Instruction {
	
	int addrCalcCycleNum;

	public MemoryInstruction(String op, int rs, int rt, int rd,
			String extraField, int lineNumber) {
		super(op, rs, rt, rd, extraField, lineNumber);
		// TODO Auto-generated constructor stub
	}
	
	/** This will be overwritten.*/
	public boolean isLoadInstruction() {
		return false;
	}
	
	/** This will also be overwritten.*/
	public boolean isStoreInstruction() {
		return false;
	}

	public void setAddrCalcCycleNum(int num) {
		this.addrCalcCycleNum = num;
	}
	
	public int getAddrCalcCycleNum() {
		return this.addrCalcCycleNum;
	}
}
