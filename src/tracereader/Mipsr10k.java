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
	
	public static void main(String[] args) {
		System.out.println(args);
		if(args.length == 0) {
			System.err.println("You must specify an in put file");
			System.exit(1);
		}
		Mipsr10k processor = new Mipsr10k(args[0]);
		processor.execute();
	}
	
	public int execute() {
		int numCycles = 0;
		while(!isDone()) {
			numCycles += 1;
			calc(numCycles);
			edge();
		}
		printCommittedInstructions(numCycles);
		return numCycles;
		
	}
	
	public boolean isDone() {
		return this.regFile.isDone() && this.instFetcher.isDone();
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
				} else if (inst.getCancelledCycleNum() == i+1) {
					schedule += "  X  |";
				} else {
					schedule += "     |";
				}
			}
			String is = "%-22s%s\n";
			System.out.printf(is, String.valueOf(inst.getLineNumber())+ " " + inst.getString(), schedule);
		}
		
	}

}
