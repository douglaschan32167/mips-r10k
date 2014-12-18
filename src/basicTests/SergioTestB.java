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
	public void b2() {
		Mipsr10k b2Test = new Mipsr10k("Sergio test b2");
		int numCycles = b2Test.execute();
		assertEquals(numCycles, 10);
	}
	
	@Test
	public void b3() {
		Mipsr10k b3Test = new Mipsr10k("Sergio test b3");
		int numCycles = b3Test.execute();
		assertEquals(numCycles, 33);
	}
	
	@Test
	public void b4() {
		Mipsr10k b4Test = new Mipsr10k("Sergio test b4");
		int numCycles = b4Test.execute();
		assertEquals(numCycles, 15);
	}
	
	@Test
	public void b5() {
		Mipsr10k b5Test = new Mipsr10k("Sergio test b5");
		int numCycles = b5Test.execute();
		assertEquals(numCycles, 22);
	}
	@Test
	public void b6() {
		Mipsr10k b6Test = new Mipsr10k("Sergio test b6");
		int numCycles = b6Test.execute();
		assertEquals(numCycles, 18);
	}

}
