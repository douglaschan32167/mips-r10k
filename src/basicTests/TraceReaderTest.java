package basicTests;

import static org.junit.Assert.*;

import org.junit.Test;

import tracereader.TraceReader;

public class TraceReaderTest {

	@Test
	public void test() {
		TraceReader tr = new TraceReader("testTrace");
		tr.readTrace();

	}

}
