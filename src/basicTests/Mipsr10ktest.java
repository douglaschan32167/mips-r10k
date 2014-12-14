package basicTests;

import static org.junit.Assert.*;

import org.junit.Test;

import tracereader.Mipsr10k;

public class Mipsr10ktest {

	@Test
	public void test() {
		Mipsr10k intTest = new Mipsr10k("RAWtrace");
		System.out.println(intTest.execute());
	}

}
