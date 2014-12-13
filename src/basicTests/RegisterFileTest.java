package basicTests;

import static org.junit.Assert.*;

import org.junit.Test;

import Register.RegisterFile;

public class RegisterFileTest {

	@Test
	public void makeRegFileBusyTest() {
		RegisterFile regFile = new RegisterFile();
		assert(regFile.makePhysRegBusy(33));
	}

}
