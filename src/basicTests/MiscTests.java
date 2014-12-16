package basicTests;

import static org.junit.Assert.*;

import java.util.LinkedList;

import instruction.Instruction;

import org.junit.Before;
import org.junit.Test;

public class MiscTests {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void test() {
		Instruction i = new Instruction("L", 1, 1, 1, "notused", 1);
		LinkedList<Instruction> testList = new LinkedList<Instruction>();
		testList.add(i);
		i.setInstString("let's see if it is still contained");
		assertTrue(testList.contains(i));
		i.setFetchCycleNum(1);
		assertTrue(testList.contains(i));
		i.setDecodeCycleNum(2);
		assertTrue(testList.contains(i));
		i.setIssueCycleNum(3);
		assertTrue(testList.contains(i));
		i.setExecuteCycleNum(4);
		assertTrue(testList.contains(i));
		i.setCommitCycleNum(5);
		
	}

}
