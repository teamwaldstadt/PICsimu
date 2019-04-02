package de.teamwaldstadt.picsimu.command;

import de.teamwaldstadt.picsimu.storage.Status;

public enum Command {
	
	//schema: 
	//name (hexCode of command, mask for command, executor class name)
	ADDWF(0x0700, 0xFF00, CommandADDWF.class, Status.C, Status.DC, Status.Z),
	ANDWF(0x0500, 0xFF00, CommandANDWF.class, Status.Z),
	CLRF(0x0180, 0xFF80, CommandCLRF.class, Status.Z),
	CLRW(0x0100, 0xFF80, CommandCLRW.class, Status.Z),
	COMF(0x0900, 0xFF00, CommandCOMF.class, Status.Z),
	DECF(0x0300, 0xFF00, CommandDECF.class, Status.Z),
	DECFSZ(0x0B00, 0xFF00, CommandDECFSZ.class),
	INCF(0x0A00, 0xFF00, CommandINCF.class, Status.Z),
	INCFSZ(0x0F00, 0xFF00, CommandINCFSZ.class),
	IORWF(0x0400, 0xFF00, CommandIORWF.class, Status.Z),
	MOVF(0x0800, 0xFF00, CommandMOVF.class, Status.Z),
	MOVWF(0x0080, 0xFF80, CommandMOVWF.class),
	NOP(0x0000, 0xFF9F, CommandNOP.class),
	RLF(0x0D00, 0xFF00, CommandRLF.class, Status.C),
	RRF(0x0C00, 0xFF00, CommandRRF.class, Status.C),
	SUBWF(0x0200, 0xFF00, CommandSUBWF.class, Status.C, Status.DC, Status.Z),
	SWAPF(0x0E00, 0xFF00, CommandSWAPF.class),
	XORWF(0x0600, 0xFF00, CommandXORWF.class, Status.Z),
	
	BCF(0x1000, 0xFC00, CommandBCF.class),
	BSF(0x1400, 0xFC00, CommandBSF.class),
	BTFSC(0x1800, 0xFC00, CommandBTFSC.class),
	BTFSS(0x1C00, 0xFC00, CommandBTFSS.class),
	
	ADDLW(0x3E00, 0xFE00, CommandADDLW.class, Status.C, Status.DC, Status.Z),
	ANDLW(0x3900, 0xFF00, CommandANDLW.class, Status.Z),
	CALL(0x2000, 0xF800, CommandCALL.class),
	CLRWDT(0x0064, 0xFFFF, CommandCLRWDT.class, Status.TO_INV, Status.PD_INV),
	GOTO(0x2800, 0xF800, CommandGOTO.class),
	IORLW(0x3800, 0xFF00, CommandIORLW.class, Status.Z),
	MOVLW(0x3000, 0xFC00, CommandMOVLW.class),
	RETFIE(0x0009, 0xFFFF, CommandRETFIE.class),
	RETLW(0x3400, 0xFC00, CommandRETLW.class),
	RETURN(0x0008, 0xFFFF, CommandRETURN.class),
	SLEEP(0x0063, 0xFFFF, CommandSLEEP.class, Status.TO_INV, Status.PD_INV),
	SUBLW(0x3C00, 0xFE00, CommandSUBLW.class, Status.C, Status.DC, Status.Z),
	XORLW(0x3A00, 0xFF00, CommandXORLW.class, Status.Z);
	
	private int value;
	private int mask;
	private Class<?extends CommandExecutor> executor;
	private Status[] statusAffected;
	
	Command(int value, int mask, Class<?extends CommandExecutor> executor, Status... statusAffected) {
		this.value = value;
		this.mask = mask;
		this.executor = executor;
		this.statusAffected = statusAffected;
	}
	
	public int getValue() {
		return this.value;
	}
	
	public int getMask() {
		return this.mask;
	}
	
	public Class<?extends CommandExecutor> getExecutor() {
		return this.executor;
	}
	
	public Status[] getStatusAffected() {
		return this.statusAffected;
	}
}
