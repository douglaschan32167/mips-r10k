package instruction;

import java.util.ArrayList;
import java.util.List;

public class Instruction {
	
	String op;
	int rs;
	int rt;
	int rd;
	String extraField;
	int lineNumber;
	String instString;
	List<BranchInstruction> branchDependencies;
	int fetchCycleNum;
	int decodeCycleNum;
	int executeCycleNum;
	int issueCycleNum;
	int commitCycleNum;
	
	public Instruction(String op, int rs, int rt, int rd, String extraField, int lineNumber) {
		this.op = op;
		this.rs = rs;
		this.rt = rt;
		this.rd = rd;
		this.extraField = extraField;
		this.lineNumber = lineNumber;
		this.branchDependencies = new ArrayList<BranchInstruction>();
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
	
	public boolean isLoadInstruction() {
		return false;
	}
	
	public boolean isBranchInstruction() {
		return false;
	}
	
	public void setBranchDependencies(List<BranchInstruction> deps) {
		this.branchDependencies = deps;
	}
	
	public boolean dependsOn(BranchInstruction branch) {
		return this.branchDependencies.contains(branch);
	}
	
	public void setFetchCycleNum(int num) {
		this.fetchCycleNum = num;
	}
	
	public void setDecodeCycleNum(int num) {
		this.decodeCycleNum = num;
	}
	
	public void setIssueCycleNum(int num) {
		this.issueCycleNum = num;
	}
	
	public void setExecuteCycleNum(int num){
		this.executeCycleNum = num;
	}
	
	public void setCommitCycleNum(int num) {
		this.commitCycleNum = num;
	}
	
	public void setExtraField(String s){
		this.extraField = s;
	}

}
