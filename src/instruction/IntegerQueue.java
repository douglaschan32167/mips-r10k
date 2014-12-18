package instruction;

import java.util.HashSet;
import java.util.LinkedList;

import Register.RegisterFile;

public class IntegerQueue {
	
	LinkedList<Instruction> instructions_r;
	LinkedList<Instruction> instructions_n;
	IntegerBranchALU intBranchAlu1;
	IntegerALU intAlu2;
	RegisterFile regFile;
	
	public IntegerQueue(RegisterFile regFile) {
		this.instructions_r = new LinkedList<Instruction>();
		this.instructions_n = new LinkedList<Instruction>();
		this.intBranchAlu1 = new IntegerBranchALU();
		this.intAlu2 = new IntegerALU();
		this.regFile = regFile;
	}
	
	public boolean isFull() {
		return instructions_n.size() == 16;
	}
	
	public boolean isEmpty() {
		return instructions_r.isEmpty();
	}
	
	public boolean addInstruction(Instruction inst) {
		if(this.isFull()) {
			return false;
		}
		if (inst.isBranchInstruction()){
			if(regFile.addBranchToActiveList((BranchInstruction) inst)) {
				instructions_n.add(inst);
				return true;
			}
		} else if(regFile.addToActiveList((ArithmeticInstruction) inst)){
			instructions_n.add(inst);
			return true;
		}
		return false;
//		} else if (regFile.addToActiveList(inst)) {
//			instructions_n.add(inst);
//			return true;
//		} else {
//			return false;
//		}
	}
	
	public void calc(int cycleNum) {
//		Instruction completed1 = intBranchAlu1.executeInstruction();
//		Instruction completed2 = intAlu2.executeInstruction();
//		if(this.regFile.mustPurgeMispredict()){
//			purgeMispredict(this.regFile.getMispredictedInstruction());
//			return;
//		}
//		boolean shouldDispatch = true;
//		if(completed1 != null) {
//			completed1.setExecuteCycleNum(cycleNum);
//			if(completed1.isBranchInstruction()) {
//				if (((BranchInstruction) completed1).isMispredicted()){
//					regFile.reportMispredictedBranch((BranchInstruction) completed1);
//					shouldDispatch = false;
//					regFile.setBranchReadyForCommit((BranchInstruction) completed1);
//				} else {
//					regFile.setBranchReadyForCommit((BranchInstruction) completed1);
//				}
//				//TODO: Rollback and stuff, canceling instructions
//			} else {
//				completed1.setExecuteCycleNum(cycleNum);
//				regFile.setReadyForCommit(completed1);
//			}
//		}
//		if(completed2 != null) {
//			completed2.setExecuteCycleNum(cycleNum);
//			regFile.setReadyForCommit(completed2);
//		}
		if(this.regFile.mustPurgeMispredict()){
			purgeMispredict(this.regFile.getMispredictedInstruction());
			return;
		}
		int numDispatched = 0;
		for(Instruction inst : instructions_r) {
			if(regFile.checkRegisters(inst)) {
				if(dispatchToAlu(inst)){
					inst.setExecuteCycleNum(cycleNum);
					numDispatched += 1;
					if(inst.isBranchInstruction()) {
						if(((BranchInstruction) inst).isMispredicted()) {
							this.regFile.reportMispredictedBranch((BranchInstruction) inst);
						}
						this.regFile.setBranchReadyForCommit((BranchInstruction) inst);
					} else {
						regFile.setReadyForCommit(inst);
					}
					instructions_n.remove(inst);
					if (numDispatched == 2) {
						return;
					}
				}
			}
		}
	}
	
	public void edge() {
		instructions_r = new LinkedList<Instruction>(instructions_n);
		intBranchAlu1.edge();
		intAlu2.edge();
	}
	
	private void releaseInstruction(Instruction inst) {
		instructions_n.remove(inst);
	}
	
	private boolean dispatchToAlu(Instruction inst) {
		if(!intBranchAlu1.hasInstThisCycle()) {
			intBranchAlu1.execute(inst);
			if(inst.isBranchInstruction()) {
				this.regFile.removeFromBranchMask((BranchInstruction) inst);
			}
			System.out.println("dispatched to int alu 1");
			return true;
		} else if(!intAlu2.hasInstThisCycle() && !inst.isBranchInstruction()) {
			intAlu2.execute(inst);
			System.out.println("dispatched to int alu 2");
			return true;
		}
		return false;
	}
	
	public void purgeMispredict(BranchInstruction branch) {
		for (Instruction inst : this.instructions_r) {
			if (inst.dependsOn(branch)) {
				this.instructions_n.remove(inst);
			}
		}
	}
	

}
