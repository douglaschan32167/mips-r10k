package Register;

import instruction.ActiveList;
import instruction.Instruction;

import java.util.HashMap;
import java.util.LinkedList;

public class RegisterFile {
	
//	HashMap<Integer, Integer> registerMap = new HashMap<Integer, Integer>();
	HashMap<Integer, PhysicalRegister> registerMap_r;
	HashMap<Integer, PhysicalRegister> registerMap_n;
	HashMap<Integer, PhysicalRegister> speculativeRegMap_r;
	HashMap<Integer, PhysicalRegister> speculativeRegMap_n;
	LinkedList<PhysicalRegister> freeList_r;
	LinkedList<PhysicalRegister> freeList_n;
	ActiveList activeList_r;
	ActiveList activeList_n;
	boolean[] busyTable_r;
	boolean[] busyTable_n;
	
	public RegisterFile(){
		this.freeList_r = new LinkedList<PhysicalRegister>();
		this.registerMap_r = new HashMap<Integer, PhysicalRegister>();
		this.freeList_n = new LinkedList<PhysicalRegister>();
		this.registerMap_n = new HashMap<Integer, PhysicalRegister>();
		this.busyTable_r = new boolean[64];
		this.busyTable_n = new boolean[64];
		for(int i = 0; i < 64; i++) {
			freeList_r.add(new PhysicalRegister(i));
			busyTable_r[i] = true; //WHAT SHOULD I INITIALIZE THIS AS?
		}
		for (int i = 0; i < 31; i++) {
			registerMap_r.put(i, freeList_r.remove());
		}
		this.freeList_n = new LinkedList<PhysicalRegister>(this.freeList_r);
		this.registerMap_n = new HashMap<Integer, PhysicalRegister>(this.registerMap_r);
		this.speculativeRegMap_n = new HashMap<Integer, PhysicalRegister>(this.registerMap_r);
		this.speculativeRegMap_r = new HashMap<Integer, PhysicalRegister>(this.registerMap_r);
		this.activeList_r = new ActiveList();
		this.activeList_n = new ActiveList();

		
	}
	
	public boolean commitInstruction(Instruction inst) {
		//TODO: Implement!
		return true;
	}
	
	public int getPhysicalRegNum(int logicalRegNum) {
		return registerMap_r.get(logicalRegNum).getNumber();
	}
	
	/** Check if all the registers are ready in this instruction */
	public boolean checkRegisters(Instruction inst) {
		if(freeList_n.isEmpty()) {
			return false;
		}
		PhysicalRegister rtReg = speculativeRegMap_r.get(inst.getRt());
		PhysicalRegister rsReg = speculativeRegMap_r.get(inst.getRs());
		return (busyTable_r[rtReg.getNumber()] && busyTable_r[rsReg.getNumber()]);
	}
	
	public boolean hasFreePhysRegisters() {
		return !this.freeList_n.isEmpty();
	}
	public void calc() {
		//put stuff here
	}
	
	public void edge() {
		this.freeList_r = new LinkedList<PhysicalRegister>(this.freeList_n);
		this.registerMap_r = new HashMap<Integer, PhysicalRegister>(this.registerMap_n);
		for(int i = 0; i < 64; i++) {
			busyTable_r[i] = busyTable_n[i];
		}
		
	}
	
	//TODO: REMOVE ME ONLY FOR TESTING.
	public boolean makePhysRegBusy(int physRegNum) {
		return freeList_n.remove(new PhysicalRegister(physRegNum));
	}
	
	
	

}
