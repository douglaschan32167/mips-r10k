package tracereader;

import instruction.AddressQueue;
import instruction.FpQueue;
import instruction.IntegerQueue;
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

}
