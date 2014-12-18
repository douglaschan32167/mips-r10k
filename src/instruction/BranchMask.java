package instruction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import Register.PhysicalRegister;

public class BranchMask {
	
	LinkedList<BranchInstruction> unresolvedBranches_n;
	LinkedList<BranchInstruction> unresolvedBranches_r;
	HashMap<BranchInstruction, HashMap<Integer, PhysicalRegister>> regMapScreenshots;
	HashMap<BranchInstruction, boolean[]> busyTableScreenshots;
	
	public BranchMask(){
		this.unresolvedBranches_r = new LinkedList<BranchInstruction>();
		this.unresolvedBranches_n = new LinkedList<BranchInstruction>();
//		this.regMapScreenshots = new HashMap<BranchInstruction, HashMap<Integer, PhysicalRegister>>();
//		this.busyTableScreenshots = new HashMap<BranchInstruction, boolean[]>();
	}
	
	public boolean addBranch(BranchInstruction branch) {
		if(branch.getLineNumber() == 40) {
			int a = 1;
		}
		if(isFull()) {
			return false;
		}
		return this.unresolvedBranches_n.add(branch);
	}
	
	public int numBranches() {
		return this.unresolvedBranches_n.size();
	}
	
	public boolean isFull() {
		return this.unresolvedBranches_n.size() >= 4;
	}
	
	public boolean checkBranchDependency(BranchInstruction branch) {
		return this.unresolvedBranches_r.contains(branch);
	}
	
	public ArrayList<BranchInstruction> getBranchDependencies() {
		return new ArrayList<BranchInstruction>(this.unresolvedBranches_n);
	}
	public void commitBranch(BranchInstruction branch) {
		this.unresolvedBranches_n.remove(branch);
	}
	
	public void purgeMispredict(BranchInstruction branch) {
		for(Instruction inst: this.unresolvedBranches_r) {
			if(inst.dependsOn(branch)) {
				this.unresolvedBranches_n.remove(inst);
//				this.busyTableScreenshots.remove(inst);
//				this.regMapScreenshots.remove(inst);
			}
		}
		this.unresolvedBranches_n.remove(branch);
	}
	
	public void edge() {
		this.unresolvedBranches_r = new LinkedList<BranchInstruction>(this.unresolvedBranches_n);
	}
}
