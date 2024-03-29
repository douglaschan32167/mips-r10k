package basicTests;

import static org.junit.Assert.*;

import org.junit.Test;

import tracereader.Mipsr10k;

public class Mipsr10ktest {

	@Test
	public void test() {
		Mipsr10k intTest = new Mipsr10k("RAWtrace");
		int intTestCycles = intTest.execute();
		assertEquals(intTestCycles, 6);
	}
	
	@Test
	public void maxCommitTest() {
		Mipsr10k maxCommitTest = new Mipsr10k("maxcommittracecopy.txt");
		int maxCommitCycles = maxCommitTest.execute();
		assertEquals(maxCommitCycles, 14); //Shouldn't it be 21?
	}
	
	@Test
	public void fpTest() {
		Mipsr10k fpTest = new Mipsr10k("fptesttrace");
		int fpTestCycles = fpTest.execute();
		assertEquals(fpTestCycles, 7);
	}
	
	@Test
	public void loadTest() {
		Mipsr10k loadTest = new Mipsr10k("loadtesttrace");
		int loadTestCycles = loadTest.execute();
		assertEquals(loadTestCycles,6);
	}
	
	@Test
	public void sergioTestLs1(){
		Mipsr10k sergioLs1Test = new Mipsr10k("Sergio test ls1");
		int ls1Cycles = sergioLs1Test.execute();
		assertEquals(ls1Cycles, 11);
	}
	
	@Test
	public void sergioTestLs2(){
		Mipsr10k sergioLs2Test = new Mipsr10k("Sergio test ls2");
		int numCycles = sergioLs2Test.execute();
		assertEquals(numCycles, 13);
	}

	@Test
	public void sergioTestLs3(){
		Mipsr10k sergioLs3Test = new Mipsr10k("Sergio test ls3");
		int numCycles = sergioLs3Test.execute();
		assertEquals(numCycles, 16);
	}
	
	@Test
	public void sergioTestLs4(){
		Mipsr10k sergioLs4Test = new Mipsr10k("Sergio test ls4");
		int numCycles = sergioLs4Test.execute();
		assertEquals(numCycles, 15);
	}

}
