package instruction;

public class Instruction {
	
	String op;
	String rs;
	String rt;
	String rd;
	String extraField;
	
	public Instruction(String op, String rs, String rt, String rd, String extraField) {
		this.op = op;
		this.rs = rs;
		this.rt = rt;
		this.rd = rd;
		this.extraField = extraField;
	}
	
	/** Return the type of the instruction (integer, store, load, branch, fpadd, fpmul) */
	public String getType() {
		return op;
	}

}
