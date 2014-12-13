package tracereader;

import instruction.Instruction;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
	
	
	public ArrayList<Instruction> readTrace() {
		ArrayList<Instruction> result = new ArrayList<Instruction>();
		try {
			String nextLine = this.traceReader.readLine();
			int lineNumber = 1;
			while (nextLine != null) {
				System.out.println(nextLine);
				String[] nextCmdArray = nextLine.split(" ");
				System.out.println(nextCmdArray.length);
				String extraField = nextCmdArray.length == 4 ? "" : nextCmdArray[4];
//				System.out.println(extraField);
//				System.out.println("is the extra field");
				Instruction nextInstruction = new Instruction(
						nextCmdArray[0],
						new Integer(nextCmdArray[1]),
						new Integer(nextCmdArray[2]),
						new Integer(nextCmdArray[3]),
						extraField,
						lineNumber);
				result.add(nextInstruction);
				nextLine = this.traceReader.readLine();
				lineNumber += 1;
			}
		} catch (IOException ioe) {
			System.err.println("There was an IOException trying to read from the trace.");
			ioe.printStackTrace();
			System.exit(1);
		}
		return result;
	}

}
