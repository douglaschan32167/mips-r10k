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
		assert(maxCommitTest.execute() == 22);
	}
	
	@Test
	public void fpTest() {
		Mipsr10k fpTest = new Mipsr10k("fptesttrace");
		assert(fpTest.execute() == 6);
	}

}
