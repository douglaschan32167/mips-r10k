package instruction;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import Register.RegisterFile;

public class AddressQueue {
	
	final int MAX_ADDRESS_QUEUE_SIZE = 16;
	
	LinkedList<MemoryInstruction> memoryInstructions_n;
	LinkedList<MemoryInstruction> memoryInstructions_r;
	HashSet<MemoryInstruction> addressCalculatedInstrs_n;
	HashSet<MemoryInstruction> addressCalculatedInstrs_r;
	LoadALU loadAlu;
	StoreALU storeAlu;
	RegisterFile regFile;
	
	public AddressQueue(RegisterFile regFile){
		this.addressCalculatedInstrs_n = new HashSet<MemoryInstruction>();
		this.addressCalculatedInstrs_r = new HashSet<MemoryInstruction>();
		this.memoryInstructions_n = new LinkedList<MemoryInstruction>();
		this.memoryInstructions_r = new LinkedList<MemoryInstruction>();
		this.loadAlu = new LoadALU();
		this.storeAlu = new StoreALU();
		this.regFile = regFile;
	}
	
	public boolean addInstruction(MemoryInstruction memInst) {
		if (this.isFull()) {
			return false;
		} 
		if(memInst.isLoadInstruction()) {
			if(regFile.addLoadToActiveList((LoadInstruction) memInst)) {
				memoryInstructions_n.add(memInst);
				return true;
			} else {
				return false;
			}
		}
		if(memInst.isStoreInstruction()) {
			if(regFile.addStoreToActiveList((StoreInstruction)memInst)){
				memoryInstructions_n.add(memInst);
				return true;
			} else {
				return false;
			}
		}
		System.err.println("There was an memory instruction that is neither a load or a store");
		return false;
//		else if (regFile.addToActiveList(memInst)) {
//			memoryInstructions_n.add(memInst);
//			return true;
//		} else {
//			return false;
//		}
		
	}
	
	public int size() {
		return memoryInstructions_n.size();
	}
	
	public boolean isEmpty() {
		return memoryInstructions_n.isEmpty();
	}
	
	public boolean isFull() {
		return this.memoryInstructions_n.size() >= MAX_ADDRESS_QUEUE_SIZE;
	}
	
	public void calc(){
		LoadInstruction completedLoad = loadAlu.executeInstruction();
		StoreInstruction completedStore = storeAlu.executeInstruction();
		if(completedLoad != null) {
			this.regFile.setReadyForCommit(completedLoad);
		}
		if(completedStore != null) {
			this.regFile.setReadyForCommit(completedStore);
		}
		HashSet<Integer> previousAddresses = new HashSet<Integer>();
		for(MemoryInstruction inst : memoryInstructions_r) {
			if(!this.addressCalculatedInstrs_r.contains(inst)) {
				this.addressCalculatedInstrs_n.add(inst);
				continue;
			}
			List<Integer> physDeps = regFile.getPhysDeps(inst);
			if(inst.isLoadInstruction() && loadAlu.canTakeDispatch()) {
				if(regFile.checkRegisters(inst) && !previousAddresses.contains(physDeps.get(0))) {
					loadAlu.setNextInstruction((LoadInstruction)inst);
					this.memoryInstructions_n.remove(inst);
					this.addressCalculatedInstrs_n.remove(inst);
				}
				previousAddresses.add(physDeps.get(0));
			} else if (inst.isStoreInstruction() && storeAlu.canTakeDispatch()) {
				if(regFile.checkRegisters(inst)&&!previousAddresses.contains(physDeps.get(0))) {
					storeAlu.setNextInstruction((StoreInstruction) inst);
					this.memoryInstructions_n.remove(inst);
					this.addressCalculatedInstrs_n.remove(inst);
				}
				previousAddresses.add(physDeps.get(0));
			}
		}
	}

	public void edge() {
		this.memoryInstructions_r = new LinkedList<MemoryInstruction>(this.memoryInstructions_n);
		this.addressCalculatedInstrs_r = new HashSet<MemoryInstruction>(this.addressCalculatedInstrs_n);
		loadAlu.edge();
		storeAlu.edge();
	}
}
