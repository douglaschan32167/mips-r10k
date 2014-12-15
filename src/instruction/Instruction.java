package instruction;

public class Instruction {
	
	String op;
	int rs;
	int rt;
	int rd;
	String extraField;
	int lineNumber;
	String instString;
	
	public Instruction(String op, int rs, int rt, int rd, String extraField, int lineNumber) {
		this.op = op;
		this.rs = rs;
		this.rt = rt;
		this.rd = rd;
		this.extraField = extraField;
		this.lineNumber = lineNumber;
	}
	
	/** Return the type of the instruction (integer, store, load, branch, fpadd, fpmul) */
	public String getType() {
		return op;
	}
	
	public void setInstString(String instString){
		System.out.println(instString);
		this.instString = instString;
	}
	
	public String getString() {
		return this.instString;
	}
	
	public String getOp() {
		return this.op;
	}
	
	public int getRs() {
		return this.rs;
	}
	
	public int getRt() {
		return this.rt;
	}
	
	public int getRd() {
		return this.rd;
	}
	
	public String getExtraField(){
		return this.extraField;
	}
	
	public int getLineNumber() {
		return this.lineNumber;
	}
	
	public boolean isStoreInstruction() {
		return false;
	}

}
