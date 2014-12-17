package tracereader;

import java.util.LinkedList;

import instruction.AddressQueue;
import instruction.FpInstruction;
import instruction.FpQueue;
import instruction.Instruction;
import instruction.IntegerQueue;
import instruction.MemoryInstruction;
import Register.RegisterFile;

public class Mipsr10k {
	
	String tracePath;
	InstructionFetcher instFetcher;
	RegisterFile regFile;
	IntegerQueue intQueue;
	FpQueue fpQueue;
	AddressQueue addressQueue;
	
	public Mipsr10k(String tracePath) {
		this.regFile = new RegisterFile();
		this.addressQueue = new AddressQueue(regFile);
		this.intQueue = new IntegerQueue(regFile);
		this.fpQueue = new FpQueue(regFile);
		this.tracePath = tracePath;
		this.instFetcher = new InstructionFetcher(tracePath, intQueue, fpQueue, addressQueue, regFile);


	}
	
	public int execute() {
		int numCycles = 0;
		while(!regFile.isDone()) {
			numCycles += 1;
			if (numCycles >= 25) {
				System.out.println("here is the breakpoint");
				
			}
			System.out.println("cycle " + String.valueOf(numCycles));
			calc(numCycles);
			edge();
		}
		System.out.println("Put the breakpoint here");
		printCommittedInstructions(numCycles);
		return numCycles;
		
	}
	
	private void calc(int cycleNum) {
		instFetcher.calc(cycleNum);
		this.intQueue.calc(cycleNum);
		this.fpQueue.calc(cycleNum);
		this.addressQueue.calc(cycleNum);
		regFile.calc(cycleNum);
	}
	
	private void edge() {
		instFetcher.edge();
		intQueue.edge();
		this.fpQueue.edge();
		this.addressQueue.edge();
		regFile.edge();
	}
	
	public void printCommittedInstructions(int numCycles) {
		LinkedList<Instruction> committedInstructions = this.regFile.getCommittedInstructions();
		String instructions = "Instructions";
		String nextLine = "";
		System.out.printf("%-22s", instructions);
		for(int i = 0; i < numCycles; i++) {
			System.out.printf("%6.3s", String.valueOf(i+1) + "|");
		}
		System.out.printf("%s", "\n");
		for(Instruction inst : committedInstructions) {
			String schedule = "";
			for(int i = 0; i < numCycles; i++) {
				if(inst.getFetchCycleNum() == i+1) {
					schedule += "  F  |";
				} else if(inst.getDecodeCycleNum() == i+1) {
					schedule += "  D  |";
				} else if((inst.isLoadInstruction()|| inst.isStoreInstruction()) && ((MemoryInstruction)inst).getAddrCalcCycleNum() == i+1) {
					schedule += "  A  |";
				} else if (inst.getIssueCycleNum() ==i+1) {
					schedule += "  I  |";
				} else if (inst.isFpInstruction() && ((FpInstruction) inst).getExecuteCycleNum() == i) {
					schedule += "  E  |";
				} else if (inst.getExecuteCycleNum() == i+1) {
					schedule += "  E  |";
				} else if (inst.isFpInstruction() && ((FpInstruction) inst).getPackingCycleNum() == i + 1) {
					schedule += "  P  |";
				}else if (inst.getCommitCycleNum() == i+1) {
					schedule += "  C  |";
				}	else {
					schedule += "     |";
				}
			}
			String is = "%-22s%s\n";
			System.out.printf(is, String.valueOf(inst.getLineNumber())+ " " + inst.getString(), schedule);
		}
		
	}

}
