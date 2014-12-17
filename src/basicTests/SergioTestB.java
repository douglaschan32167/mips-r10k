package basicTests;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import tracereader.Mipsr10k;

public class SergioTestB {

	@Before
	public void setUp() throws Exception {
	}

	
	@Test
	public void b1() {
		Mipsr10k b1Test = new Mipsr10k("Sergio test b1");
		int numCycles = b1Test.execute();
		assertEquals(numCycles, 10);
	}
	@Test
	public void b6() {
		Mipsr10k b6Test = new Mipsr10k("Sergio test b6");
		int numCycles = b6Test.execute();
		assertEquals(numCycles, 18);
	}

}
