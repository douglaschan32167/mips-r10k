package tracereader;

import instruction.FpInstruction;
import instruction.Instruction;
import instruction.IntInstruction;

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
				String[] nextCmdArray = nextLine.split(" ");
				String extraField = nextCmdArray.length == 4 ? "" : nextCmdArray[4];
				int rd = nextCmdArray[3].equals("xx") || nextCmdArray[3].equals("XX") ? 0 : Integer.parseInt(nextCmdArray[3], 16);

				Instruction nextInstruction = new Instruction(
						nextCmdArray[0],
						Integer.parseInt(nextCmdArray[1], 16),
						Integer.parseInt(nextCmdArray[2], 16),
						rd,
						extraField,
						lineNumber);
				nextInstruction.setInstString(nextLine.toString());
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
