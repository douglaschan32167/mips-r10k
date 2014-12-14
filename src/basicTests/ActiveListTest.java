package basicTests;

import static org.junit.Assert.*;
import instruction.ActiveList;
import instruction.Instruction;

import org.junit.Before;
import org.junit.Test;

import Register.PhysicalRegister;

public class ActiveListTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void maxCapacityTest() {
		ActiveList al = new ActiveList();
		assert(al.isEmpty());
		for(int i = 0; i < 32; i++) {
			al.add(
					new Instruction("I", 1, 1, 1, "notused", i+1),
					new PhysicalRegister(i),
					new PhysicalRegister(i));
			assertFalse(al.isEmpty());
		}
		assert(al.isFull());
		assertFalse(al.add(
				new Instruction("I", 1, 1, 1, "notused", 32),
				new PhysicalRegister(32),
				new PhysicalRegister(33)));
	}

}
