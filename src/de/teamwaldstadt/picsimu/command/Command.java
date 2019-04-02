package de.teamwaldstadt.picsimu.command;

public enum Command {
	
	//schema: 
	//name (hexCode of command, mask for command, executor class name)
	ADDWF(0x0700, 0xFF00, CommandADDWF.class),
	ANDWF(0x0500, 0xFF00, CommandANDWF.class),
	CLRF(0x0180, 0xFF80, CommandCLRF.class),
	CLRW(0x0100, 0xFF80, CommandCLRW.class),
	COMF(0x0900, 0xFF00, CommandCOMF.class),
	DECF(0x0300, 0xFF00, CommandDECF.class),
	DECFSZ(0x0B00, 0xFF00, CommandDECFSZ.class),
	INCF(0x0A00, 0xFF00, CommandINCF.class),
	INCFSZ(0x0F00, 0xFF00, CommandINCFSZ.class),
	IORWF(0x0400, 0xFF00, CommandIORWF.class),
	MOVF(0x0800, 0xFF00, CommandMOVF.class),
	MOVWF(0x0080, 0xFF80, CommandMOVWF.class),
	NOP(0x0000, 0xFF9F, CommandNOP.class),
	RLF(0x0D00, 0xFF00, CommandRLF.class),
	RRF(0x0C00, 0xFF00, CommandRRF.class),
	SUBWF(0x0200, 0xFF00, CommandSUBWF.class),
	SWAPF(0x0E00, 0xFF00, CommandSWAPF.class),
	XORWF(0x0600, 0xFF00, CommandXORWF.class),
	
	BCF(0x1000, 0xFC00, CommandBCF.class),
	BSF(0x1400, 0xFC00, CommandBSF.class),
	BTFSC(0x1800, 0xFC00, CommandBTFSC.class),
	BTFSS(0x1C00, 0xFC00, CommandBTFSS.class),
	
	ADDLW(0x3E00, 0xFE00, CommandADDLW.class),
	ANDLW(0x3900, 0xFF00, CommandANDLW.class),
	CALL(0x2000, 0xF800, CommandCALL.class),
	CLRWDT(0x0064, 0xFFFF, CommandCLRWDT.class),
	GOTO(0x2800, 0xF800, CommandGOTO.class),
	IORLW(0x3800, 0xFF00, CommandIORLW.class),
	MOVLW(0x3000, 0xFC00, CommandMOVLW.class),
	RETFIE(0x0009, 0xFFFF, CommandRETFIE.class),
	RETLW(0x3400, 0xFC00, CommandRETLW.class),
	RETURN(0x0008, 0xFFFF, CommandRETURN.class),
	SLEEP(0x0063, 0xFFFF, CommandSLEEP.class),
	SUBLW(0x3C00, 0xFE00, CommandSUBLW.class),
	XORLW(0x3A00, 0xFF00, CommandXORLW.class);
	
	private int value;
	private int mask;
	private Class<?extends CommandExecutor> executor;
	
	Command(int value, int mask, Class<?extends CommandExecutor> executor) {
		this.value = value;
		this.mask = mask;
		this.executor = executor;
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
}
