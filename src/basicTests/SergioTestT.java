package basicTests;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import tracereader.Mipsr10k;

public class SergioTestT {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void sergioTestT1() {
		Mipsr10k t1 = new Mipsr10k("Sergio test t1");
		int numCycles = t1.execute();
		assertEquals(numCycles, 202);
	}
	
	@Test
	public void sergioTestT2() {
		Mipsr10k t2 = new Mipsr10k("Sergio test t2");
		int numCycles = t2.execute();
		assertEquals(numCycles, 145);
	}
	
	@Test
	public void sergioTestT5() {
		Mipsr10k t5 = new Mipsr10k("Sergio test t5");
		int numCycles = t5.execute();
		assertEquals(numCycles, 172);
	}

}
