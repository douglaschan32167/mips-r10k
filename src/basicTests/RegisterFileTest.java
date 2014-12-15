package basicTests;

import static org.junit.Assert.*;
import instruction.Instruction;
import instruction.IntInstruction;
import instruction.IntegerQueue;

import org.junit.Test;

import Register.RegisterFile;

public class RegisterFileTest {

	@Test
	public void makeRegFileBusyTest() {
		RegisterFile regFile = new RegisterFile();
		assert(regFile.makePhysRegBusy(33));
	}
	
	@Test
	public void sharedRegFileTest() {
		RegisterFile regFile = new RegisterFile();
		IntegerQueue intQ1 = new IntegerQueue(regFile);
		IntegerQueue intQ2 = new IntegerQueue(regFile);
		IntInstruction i1 = new IntInstruction("I", 1, 1, 1, null, 1);
		IntInstruction i2 = new IntInstruction("I", 1, 1, 2, null, 2);
		intQ1.addInstruction(i1);
		intQ2.addInstruction(i2);
		assert(regFile.checkRegisters(i2) == false);
		assert(regFile.checkRegisters(i1));
		
		regFile.edge();
		
		regFile.setReadyForCommit(i1);
		
		regFile.edge();
		
		regFile.commitInstructions();
		assert(regFile.checkRegisters(i2) == false);
		
		regFile.edge();
		
		assert(regFile.checkRegisters(i2));
	}

}
