package de.saadatbaig.J6502.Models;

import de.saadatbaig.J6502.Utils.Abstractions.NotificationCenterListener;
import de.saadatbaig.J6502.Utils.NotificationCenter;

import java.util.Arrays;

public class Bus implements NotificationCenterListener {

    ///////////////////////////////////////////////////////////////////////////
    // MEMBERS
    ///////////////////////////////////////////////////////////////////////////
    private CPU cpu;
    private int[] ram = new int[64*1024];


    ///////////////////////////////////////////////////////////////////////////
    // CTOR + INIT
    ///////////////////////////////////////////////////////////////////////////
    public Bus() {
        Arrays.fill(ram, 0x00);
    }

    public void connectCPU(CPU cpu) { this.cpu = cpu; }

    public CPU getCPU() { return cpu; }


    ///////////////////////////////////////////////////////////////////////////
    // FUNCTIONS
    ///////////////////////////////////////////////////////////////////////////
    public int read(int addr) {
        if (addr > 0x0 && addr <= 0xFFFF) {
            addr &= 0xFFFF;
            return (ram[addr] & 0xFF);
        } else {
            return 0x00;
        }
    }

    public void write(int addr, int data) {
        if ((addr > 0x0 && addr <= 0xFFFF) && (data >= 0x0 && data <= 0xFF)) {
            addr &= 0xFFFF;
            data &= 0xFF;
            ram[addr] = data;
        }
    }


    ///////////////////////////////////////////////////////////////////////////
    // INTERFACE OVERRIDES
    ///////////////////////////////////////////////////////////////////////////
    @Override public void registerAsListener(String _id) {
        NotificationCenter.sharedInstance().registerWithIdentifier(_id, this);
    }

    @Override public void sendMessage(String _id, String msg) {
        NotificationCenter.sharedInstance().sendMessageToCPU(_id , msg);
    }

    @Override public void receiveMessage(String msg) {
        System.out.println("Bus receiving: " + msg);    // Debug
    }


    /* End */
}
