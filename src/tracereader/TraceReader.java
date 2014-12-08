package tracereader;

import instruction.Instruction;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class TraceReader {
	
	String tracePath;
	BufferedReader traceReader;
	
	public TraceReader(String tracePath) {
		this.tracePath = tracePath;
		try {
			FileReader traceFileReader = new FileReader(tracePath);
			this.traceReader = new BufferedReader(traceFileReader);
		} catch (FileNotFoundException fnfe) {
			System.err.println("The file was not found");
			fnfe.printStackTrace();
			System.exit(1);
		}
	}
	
	
	public void readTrace() {
		try {
			String nextLine = this.traceReader.readLine();
			while (nextLine != null) {
				System.out.println(nextLine);
				String[] nextCmdArray = nextLine.split(" ");
				System.out.println(nextCmdArray.length);
				String extraField = nextCmdArray.length == 4 ? "" : nextCmdArray[4];
//				System.out.println(extraField);
//				System.out.println("is the extra field");
				Instruction nextInstruction = new Instruction(
						nextCmdArray[0],
						nextCmdArray[1],
						nextCmdArray[2],
						nextCmdArray[3],
						extraField);
				nextLine = this.traceReader.readLine();
			}
		} catch (IOException ioe) {
			System.err.println("There was an IOException trying to read from the trace.");
			ioe.printStackTrace();
			System.exit(1);
		}
	}

}
