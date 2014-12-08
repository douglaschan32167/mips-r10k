package Register;

import java.util.HashMap;

public class RegisterFile {
	
//	HashMap<Integer, Integer> registerMap = new HashMap<Integer, Integer>();
	int[] registerMap_r;
	int[] registerMap_n;
	boolean[] freeList_r;
	boolean[] freeList_n;
	boolean[] busyTable_r;
	boolean[] busyTable_n;
	
	public RegisterFile(){
		this.freeList_r = new boolean[64];
		this.registerMap_r = new int[31];
		this.freeList_n = new boolean[64];
		this.registerMap_n = new int[31];
		for (int i = 0; i < 32; i++) {
			registerMap_r[i] = i;
		}
		for(int i = 0; i < 64; i++) {
			freeList_r[i] = true;
			busyTable_r[i] = true;
		}
		
	}
	
	public int getPhysicalRegNum(int logicalRegNum) {
		return registerMap_r[logicalRegNum];
	}
	
	public void calc() {
		//put stuff here
	}
	
	public void edge() {
		registerMap_r = registerMap_n;
		freeList_r = freeList_n;
		busyTable_r = busyTable_n;
	}
	

}
