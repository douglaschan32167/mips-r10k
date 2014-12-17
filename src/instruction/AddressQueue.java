package instruction;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import Register.PhysicalRegister;
import Register.RegisterFile;

public class AddressQueue {
	
	final int MAX_ADDRESS_QUEUE_SIZE = 16;
	
	LinkedList<MemoryInstruction> memoryInstructions_n;
	LinkedList<MemoryInstruction> memoryInstructions_r;
	HashSet<MemoryInstruction> addressCalculatedInstrs_n;
	HashSet<MemoryInstruction> addressCalculatedInstrs_r;
	HashMap<MemoryInstruction, String> committingInstructions_r;
	HashMap<MemoryInstruction, String> committingInstructions_n;
//	LoadALU loadAlu;
//	StoreALU storeAlu;
	MemoryALU memAlu;
	RegisterFile regFile;
	
	public AddressQueue(RegisterFile regFile){
		this.addressCalculatedInstrs_n = new HashSet<MemoryInstruction>();
		this.addressCalculatedInstrs_r = new HashSet<MemoryInstruction>();
		this.memoryInstructions_n = new LinkedList<MemoryInstruction>();
		this.memoryInstructions_r = new LinkedList<MemoryInstruction>();
//		this.loadAlu = new LoadALU();
//		this.storeAlu = new StoreALU();
		this.memAlu = new MemoryALU();
		this.regFile = regFile;
		this.committingInstructions_n = new HashMap<MemoryInstruction, String>();
		this.committingInstructions_r = new HashMap<MemoryInstruction, String>();
		
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
	
	public void calc(int cycleNum){
//		LoadInstruction completedLoad = loadAlu.executeInstruction();
//		StoreInstruction completedStore = storeAlu.executeInstruction();
//		if(cycleNum > 180) {
//			int a = 1;
//		}
//		if(completedLoad != null) {
//			completedLoad.setExecuteCycleNum(cycleNum);
//			this.regFile.setReadyForCommit(completedLoad);
//		}
//		if(completedStore != null) {
//			completedStore.setExecuteCycleNum(cycleNum);
//			this.regFile.setStoreReadyForCommit(completedStore);
//		}
		if(this.regFile.mustPurgeMispredict()) {
			purgeMispredict(this.regFile.getMispredictedInstruction());
			return;
		}
		HashSet<String> previousAddresses = new HashSet<String>();
		boolean prevLoadsAddrCalculated = true;
		boolean isFirstStore = true;
		for(MemoryInstruction inst : memoryInstructions_r) {
			if(inst.isLoadInstruction() && !addressCalculatedInstrs_r.contains(inst)) {
				prevLoadsAddrCalculated = false;
			}
			if(!this.addressCalculatedInstrs_r.contains(inst) && this.regFile.checkRegisters(inst)) {
				this.addressCalculatedInstrs_n.add(inst);
				inst.setAddrCalcCycleNum(cycleNum);
				if(inst.isStoreInstruction()) {
					isFirstStore = false;
				}
				continue;
			}
			List<Integer> physDeps = regFile.getPhysDeps(inst);
			if(inst.isLoadInstruction() && memAlu.canTakeDispatch() && prevLoadsAddrCalculated) {
				if(regFile.checkRegisters(inst) && !previousAddresses.contains(inst.getExtraField()) && !this.committingInstructions_r.containsValue(inst.getExtraField())) {
					memAlu.execute( inst);
					this.memoryInstructions_n.remove(inst);
					inst.setExecuteCycleNum(cycleNum);
					this.addressCalculatedInstrs_n.remove(inst);
					this.committingInstructions_n.put(inst, inst.getExtraField());
					this.regFile.setReadyForCommit(inst);
				}
				previousAddresses.add(inst.getExtraField());
			} else if (inst.isStoreInstruction()) {
				if(memAlu.canTakeDispatch() && isFirstStore) {
					if(regFile.checkRegisters(inst)&&!previousAddresses.contains(physDeps.get(0)) && isFirstStore && prevLoadsAddrCalculated) {
						memAlu.execute(inst);
						this.memoryInstructions_n.remove(inst);
						inst.setExecuteCycleNum(cycleNum);
						this.addressCalculatedInstrs_n.remove(inst);
						this.committingInstructions_n.put(inst, inst.getExtraField());
						this.regFile.setStoreReadyForCommit((StoreInstruction) inst);
					}
					if(physDeps.get(0) != 0) {
						previousAddresses.add(inst.getExtraField());
					}
				}
				isFirstStore = false;
			}
		}
	}

	public void edge() {
		this.memoryInstructions_r = new LinkedList<MemoryInstruction>(this.memoryInstructions_n);
		this.addressCalculatedInstrs_r = new HashSet<MemoryInstruction>(this.addressCalculatedInstrs_n);
		memAlu.edge();
		for(MemoryInstruction m : this.committingInstructions_r.keySet()) {
			if(m.getCommitCycleNum() != 0) {
				this.committingInstructions_n.remove(m);
			}
		}
		this.committingInstructions_r = new HashMap<MemoryInstruction, String>(this.committingInstructions_n);
	}
	
	public void purgeMispredict(BranchInstruction branch) {
		for(Instruction inst : this.memoryInstructions_n){
			if(inst.dependsOn(branch)) {
				this.memoryInstructions_n.remove(inst);
				if(this.addressCalculatedInstrs_n.contains(inst)) {
					this.addressCalculatedInstrs_n.remove(inst);
				}
			}
		}
	}
}
