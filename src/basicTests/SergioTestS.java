package basicTests;

import static org.junit.Assert.*;

import org.junit.Test;

import tracereader.Mipsr10k;

public class SergioTestS {
	@Test
	public void sergioTestS1(){
		Mipsr10k sergioS1Test = new Mipsr10k("Sergio test s1");
		int s1Cycles = sergioS1Test.execute();
		assertEquals(s1Cycles, 7);
	}


	@Test
	public void sergioTestS2(){
		Mipsr10k sergioS2Test = new Mipsr10k("Sergio test s2");
		int s2Cycles = sergioS2Test.execute();
		assertEquals(s2Cycles, 73);
	}
	
	@Test
	public void sergioTestS3(){
		Mipsr10k sergioS3Test = new Mipsr10k("Sergio test s3");
		int s3Cycles = sergioS3Test.execute();
		assertEquals(s3Cycles, 68);
	}
	
	@Test
	public void sergioTestS4(){
		Mipsr10k sergioS4Test = new Mipsr10k("Sergio test s4");
		int s4Cycles = sergioS4Test.execute();
		assertEquals(s4Cycles, 126);
	}
	
	@Test
	public void sergioTestS5(){
		Mipsr10k sergioS5Test = new Mipsr10k("Sergio test s5");
		int s5Cycles = sergioS5Test.execute();
		assertEquals(s5Cycles, 24);
	}

}
