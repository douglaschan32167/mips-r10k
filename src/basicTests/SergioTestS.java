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

}
