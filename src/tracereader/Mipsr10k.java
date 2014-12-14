package tracereader;

import instruction.FpQueue;
import instruction.IntegerQueue;
import Register.RegisterFile;

public class Mipsr10k {
	
	String tracePath;
	InstructionFetcher instFetcher;
	RegisterFile regFile;
	IntegerQueue intQueue;
	FpQueue fpQueue;
	
	public Mipsr10k(String tracePath) {
		this.regFile = new RegisterFile();
		this.intQueue = new IntegerQueue(regFile);
		this.fpQueue = new FpQueue(regFile);
		this.tracePath = tracePath;
		this.instFetcher = new InstructionFetcher(tracePath, intQueue, fpQueue);


	}
	
	public int execute() {
		int numCycles = 0;
		while(!regFile.isDone()) {
			numCycles += 1;
			System.out.println("cycle " + String.valueOf(numCycles));
			calc();
			edge();
		}
		return numCycles;
		
	}
	
	private void calc() {
		instFetcher.calc();
		intQueue.calc();
		regFile.calc();
	}
	
	private void edge() {
		instFetcher.edge();
		intQueue.edge();
		regFile.edge();
	}

}
