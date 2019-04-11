package de.teamwaldstadt.picsimu.utils;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

import de.teamwaldstadt.picsimu.CodeExecutor;
import de.teamwaldstadt.picsimu.Main;

public class FrequencyGenerator implements ActionListener {
	
	private static FrequencyGenerator instance;
	
	Timer t;
	CodeExecutor codeExecutor;
	int pin = 0;
	int reg = 0;
	
	public FrequencyGenerator() {
		t = new Timer(1000, this);
	}
	
	public void setCodeExecutor(CodeExecutor codeExecutor) {
		this.codeExecutor = codeExecutor;
	}
	
	public static FrequencyGenerator getInstance() {
		if (FrequencyGenerator.instance == null) {
			FrequencyGenerator.instance = new FrequencyGenerator();
		}
		return FrequencyGenerator.instance;
	}

	public void runWithFreq(int delay, int regAddr, int pin) {
		if (t.isRunning()) stop();
	
		this.pin = pin;
		this.reg = regAddr;
		t.setDelay(delay);
		t.start();
	}
	
	public void stop() {
		t.stop();
		try {
			Main.STORAGE.setBitOfRegister(reg, pin, false, true);
		} catch (Exception e) {
			e.printStackTrace();
		}
		codeExecutor.updateRegisters();
		codeExecutor.updateStorage();
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		try {
			if (Main.STORAGE.isBitOfRegisterSet(reg, pin, true)) {
				Main.STORAGE.setBitOfRegister(reg, pin, false, true);
			} else {
				Main.STORAGE.setBitOfRegister(reg, pin, true, true);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		codeExecutor.updateRegisters();
		codeExecutor.updateStorage();
		
	}
}
