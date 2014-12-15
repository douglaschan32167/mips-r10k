package basicTests;

import static org.junit.Assert.*;

import org.junit.Test;

import tracereader.Mipsr10k;

public class Mipsr10ktest {

	@Test
	public void test() {
		Mipsr10k intTest = new Mipsr10k("RAWtrace");
		assert(intTest.execute() == 7);
	}
	
	@Test
	public void maxCommitTest() {
		Mipsr10k maxCommitTest = new Mipsr10k("maxcommittracecopy.txt");
		int maxCommitCycles = maxCommitTest.execute();
		assert(maxCommitCycles == 22); //Shouldn't it be 21?
	}
	
	@Test
	public void fpTest() {
		Mipsr10k fpTest = new Mipsr10k("fptesttrace");
		assert(fpTest.execute() == 7);
	}
	
	@Test
	public void loadTest() {
		Mipsr10k fpTest = new Mipsr10k("loadtesttrace");
		assert(fpTest.execute() == 6);
	}

}
