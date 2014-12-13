package tracereader;

import instruction.Instruction;
import instruction.IntegerQueue;

import java.util.ArrayList;

public class InstructionFetcher {
	TraceReader traceReader;
	ArrayList<Instruction> instructionList;
	ArrayList<Instruction> instructionsRemaining_r;
	ArrayList<Instruction> instructionsRemaining_n;
	ArrayList<Instruction> instructionsToIssue_r;
	ArrayList<Instruction> instructionsToIssue_n;
	
	public InstructionFetcher(String tracePath) {
		this.traceReader = new TraceReader(tracePath);
		this.instructionList = this.traceReader.readTrace();
		this.instructionsRemaining_r = new ArrayList<Instruction>(instructionList);
		this.instructionsRemaining_n = new ArrayList<Instruction>(instructionList);
		this.instructionsToIssue_n = new ArrayList<Instruction>();
		this.instructionsToIssue_r = new ArrayList<Instruction>();
	}
	
	public ArrayList<Instruction> decode(int intCapacity, int addrCapacity, int fpCapacity) {
		ArrayList<Instruction> result = new ArrayList<Instruction>();
		if(intCapacity > 0) {
			Instruction nextInstruction = this.instructionsRemaining_r.get(0);
			result.add(nextInstruction);
			this.instructionsRemaining_n.remove(nextInstruction);
			
		}
		return result;
		
	}
	
	public void calc(IntegerQueue intQueue) {
		if(!intQueue.isFull()) {
			intQueue.addInstruction(this.instructionList.get(0));
		}
	}
	
	public void edge() {
		this.instructionsToIssue_r = new ArrayList<Instruction>(this.instructionsToIssue_n);
		this.instructionsRemaining_r = new ArrayList<Instruction>(this.instructionsRemaining_n);
	}

}
