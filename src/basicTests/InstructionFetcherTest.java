package basicTests;

import static org.junit.Assert.*;

import java.util.ArrayList;

import instruction.AddressQueue;
import instruction.FpQueue;
import instruction.Instruction;
import instruction.IntegerQueue;

import org.junit.Test;

import Register.RegisterFile;
import tracereader.InstructionFetcher;

public class InstructionFetcherTest {

	@Test
	public void testInstructionFetcher() {
//		fail("Not yet implemented");
	}

//	@Test
//	public void testDecode() {
//		RegisterFile regFile = new RegisterFile();
//		IntegerQueue intQueue = new IntegerQueue(regFile);
//		FpQueue fpQueue = new FpQueue(regFile);
//		InstructionFetcher instFetcher = new InstructionFetcher("testTrace", intQueue, fpQueue);
//		ArrayList<Instruction> decodedInstructions = instFetcher.decode(4, 4, 4);
//		for (Instruction i : decodedInstructions) {
//			intQueue.addInstruction(i);
//		}
//		intQueue.edge();
////		System.out.println(decodedInstructions);
//		assertFalse(intQueue.isEmpty());
//	}
	
	@Test
	public void testCalc() {
		RegisterFile regFile = new RegisterFile();
		IntegerQueue intQueue = new IntegerQueue(regFile);
		FpQueue fpQueue = new FpQueue(regFile);
		AddressQueue addressQueue = new AddressQueue(regFile);
		InstructionFetcher instFetcher = new InstructionFetcher("testTrace", intQueue, fpQueue, addressQueue, regFile);
		instFetcher.calc(1);
		instFetcher.edge();
		intQueue.edge();
		instFetcher.calc(1);
		instFetcher.edge();
		intQueue.edge();
		boolean ie = intQueue.isEmpty();
		assertFalse(ie);
	}

	@Test
	public void testEdge() {
//		fail("Not yet implemented");
	}

}
