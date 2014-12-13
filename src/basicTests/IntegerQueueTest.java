package basicTests;

import static org.junit.Assert.*;
import instruction.Instruction;
import instruction.IntegerQueue;

import org.junit.Test;

import Register.RegisterFile;

public class IntegerQueueTest {

	@Test
	public void isFullTest() {
		RegisterFile regFile = new RegisterFile();
		IntegerQueue intQueue = new IntegerQueue(regFile);
		assert(intQueue.isEmpty());
		for(int i = 0; i < 16; i++) {
			assert(intQueue.addInstruction(new Instruction("I", 1, 1, 1, null, i+1)));
		}
		assert(intQueue.isFull());
	}

}
