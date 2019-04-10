package de.teamwaldstadt.picsimu.command;

import de.teamwaldstadt.picsimu.storage.Status;

public enum Command {
	
	//schema: 
	//name (hexCode of command, mask for command, executor class name)
	ADDWF(0x0700, 0xFF00, 4, CommandADDWF.class, Status.C, Status.DC, Status.Z),
	ANDWF(0x0500, 0xFF00, 4, CommandANDWF.class, Status.Z),
	CLRF(0x0180, 0xFF80, 4, CommandCLRF.class, Status.Z),
	CLRW(0x0100, 0xFF80, 4, CommandCLRW.class, Status.Z),
	COMF(0x0900, 0xFF00, 4, CommandCOMF.class, Status.Z),
	DECF(0x0300, 0xFF00, 4, CommandDECF.class, Status.Z),
	DECFSZ(0x0B00, 0xFF00, 4, CommandDECFSZ.class),
	INCF(0x0A00, 0xFF00, 4, CommandINCF.class, Status.Z),
	INCFSZ(0x0F00, 0xFF00, 4, CommandINCFSZ.class),
	IORWF(0x0400, 0xFF00, 4, CommandIORWF.class, Status.Z),
	MOVF(0x0800, 0xFF00, 4, CommandMOVF.class, Status.Z),
	MOVWF(0x0080, 0xFF80, 4, CommandMOVWF.class),
	NOP(0x0000, 0xFF9F, 4, CommandNOP.class),
	RLF(0x0D00, 0xFF00, 4, CommandRLF.class, Status.C),
	RRF(0x0C00, 0xFF00, 4, CommandRRF.class, Status.C),
	SUBWF(0x0200, 0xFF00, 4, CommandSUBWF.class, Status.C, Status.DC, Status.Z),
	SWAPF(0x0E00, 0xFF00, 4, CommandSWAPF.class),
	XORWF(0x0600, 0xFF00, 4, CommandXORWF.class, Status.Z),
	
	BCF(0x1000, 0xFC00, 4, CommandBCF.class),
	BSF(0x1400, 0xFC00, 4, CommandBSF.class),
	BTFSC(0x1800, 0xFC00, 4, CommandBTFSC.class),
	BTFSS(0x1C00, 0xFC00, 4, CommandBTFSS.class),
	
	ADDLW(0x3E00, 0xFE00, 4, CommandADDLW.class, Status.C, Status.DC, Status.Z),
	ANDLW(0x3900, 0xFF00, 4, CommandANDLW.class, Status.Z),
	CALL(0x2000, 0xF800, 8, CommandCALL.class),
	CLRWDT(0x0064, 0xFFFF, 4, CommandCLRWDT.class, Status.TO_INV, Status.PD_INV),
	GOTO(0x2800, 0xF800, 8, CommandGOTO.class),
	IORLW(0x3800, 0xFF00, 4, CommandIORLW.class, Status.Z),
	MOVLW(0x3000, 0xFC00, 4, CommandMOVLW.class),
	RETFIE(0x0009, 0xFFFF, 4, CommandRETFIE.class),
	RETLW(0x3400, 0xFC00, 4, CommandRETLW.class),
	RETURN(0x0008, 0xFFFF, 8, CommandRETURN.class),
	SLEEP(0x0063, 0xFFFF, 4, CommandSLEEP.class, Status.TO_INV, Status.PD_INV),
	SUBLW(0x3C00, 0xFE00, 4, CommandSUBLW.class, Status.C, Status.DC, Status.Z),
	XORLW(0x3A00, 0xFF00, 4, CommandXORLW.class, Status.Z);
	
	private int value;
	private int mask;
	private int quartzTacts;
	private Class<?extends CommandExecutor> executor;
	private Status[] statusAffected;
	
	Command(int value, int mask, int quartzTacts, Class<?extends CommandExecutor> executor, Status... statusAffected) {
		this.value = value;
		this.mask = mask;
		this.quartzTacts = quartzTacts;
		this.executor = executor;
		this.statusAffected = statusAffected;
	}
	
	public int getValue() {
		return this.value;
	}
	
	public int getMask() {
		return this.mask;
	}
	
	public int getQuartzTacts() {
		return this.quartzTacts;
	}
	
	public Class<?extends CommandExecutor> getExecutor() {
		return this.executor;
	}
	
	public Status[] getStatusAffected() {
		return this.statusAffected;
	}
}
