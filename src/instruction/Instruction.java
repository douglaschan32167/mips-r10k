package instruction;

public abstract class Instruction {
	
	abstract void execute(int rt, int rs, int rd, int extraField);

}
