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
	
	//TODO: Change this to actually check if the int queue is full and if the activelist is full
	public void calc(IntegerQueue intQueue) {
		for(int i = 0; i < instructionsToIssue_r.size(); i++) {
			Instruction nextInstruction = instructionsToIssue_r.get(i);
			if (intQueue.addInstruction(nextInstruction)) {
				instructionsToIssue_n.remove(nextInstruction);
			}
		}
		while(instructionsToIssue_n.size() < 4 && !instructionsRemaining_n.isEmpty()) {
			instructionsToIssue_n.add(instructionsRemaining_n.remove(0));
		}
	}
	
	public void edge() {
		this.instructionsToIssue_r = new ArrayList<Instruction>(this.instructionsToIssue_n);
		this.instructionsRemaining_r = new ArrayList<Instruction>(this.instructionsRemaining_n);
	}

}
