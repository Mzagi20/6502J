package de.saadatbaig.J6502.Models;

import de.saadatbaig.J6502.Utils.Abstractions.NotificationCenterListener;
import de.saadatbaig.J6502.Utils.NotificationCenter;

public class CPU implements NotificationCenterListener {

    ///////////////////////////////////////////////////////////////////////////
    // MEMBERS
    ///////////////////////////////////////////////////////////////////////////
    public int regA = 0x00;     // uint8_t
    public int regX = 0x00;     // uint8_t
    public int regY = 0x00;     // uint8_t
    public int stackPtr = 0x00; // uint8_t
    public int pc = 0x0000;     // uint16_t
    public int status = 0x00;   // uint8_t
    public enum FLAGS {
        C(1 << 0),  // Carry
        Z(1 << 1),  // Zero
        I(1 << 2),  // Disable Interrupts
        D(1 << 3),  // Decimal Mode (Unused)
        B(1 << 4),  // Break
        U(1 << 5),  // Unused
        V(1 << 6),  // Overflow
        N(1 << 7)   // Negative
        ;

        private final int i;
        FLAGS (int i) { this.i = i; }
        public int asInt() { return i; }
    };
    private Bus bus;
    private int fetched = 0x00;     // uint8_t
    private int tmp = 0x0000;       // uint16_t
    private int addr_abs = 0x0000;  // uint16_t
    private int addr_rel = 0x0000;  // uint16_t
    private int opcode = 0x00;         // uint8_t
    private int cycles = 0;      // uint8_t


    ///////////////////////////////////////////////////////////////////////////
    // CTOR + INIT
    ///////////////////////////////////////////////////////////////////////////
    public CPU() {/*code*/}

    public void connectToBus(Bus bus) {
        this.bus = bus;
        this.bus.connectCPU(this);
    }


    ///////////////////////////////////////////////////////////////////////////
    // FUNCTIONS
    ///////////////////////////////////////////////////////////////////////////
    public int read(int addr) {
        return bus.read(addr);
    }

    public void write(int addr, int data) {
        bus.write(addr, data);
    }

    public int getFlag(CPU.FLAGS flag) {
        return ((status & flag.asInt()) > 0) ? 1 : 0;
    }

    public void setFlag(CPU.FLAGS flag, boolean toggledON) {
        if (toggledON) {
            status |= flag.asInt();
        } else {
            status &= ~flag.asInt();
        }
    }


    ///////////////////////////////////////////////////////////////////////////
    // ADDRESSING MODES
    ///////////////////////////////////////////////////////////////////////////


    ///////////////////////////////////////////////////////////////////////////
    // INSTRUCTIONS
    ///////////////////////////////////////////////////////////////////////////



    ///////////////////////////////////////////////////////////////////////////
    // UTILITY + HELPERS
    ///////////////////////////////////////////////////////////////////////////



    ///////////////////////////////////////////////////////////////////////////
    // INTERFACE OVERRIDE
    ///////////////////////////////////////////////////////////////////////////
    @Override
    public void registerAsListener(String _id) {
        NotificationCenter.sharedInstance().registerWithIdentifier(_id, this);
    }

    @Override
    public void sendMessage(String _id, String msg) {
        NotificationCenter.sharedInstance().sendMessageToBus(_id, msg);
    }

    @Override
    public void receiveMessage(String msg) {
        System.out.println("CPU receiving: " + msg);    // Debug
    }


    /* End */
}
