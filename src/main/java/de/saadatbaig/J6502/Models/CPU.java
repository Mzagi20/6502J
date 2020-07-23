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
    public int IMP() {
        fetched = regA;
        return 0;
    }

    public int IMM() {
        addr_abs = pc++;
        return 0;
    }

    public int ZP0() {
        addr_abs = read(pc);
        pc++;
        addr_abs &= 0x00FF;
        return 0;
    }

    public int ZPX() {
        addr_abs = (read(pc) + regX);
        pc++;
        addr_abs &= 0x00FF;
        return 0;
    }

    public int ZPY() {
        addr_abs = (read(pc) + regY);
        pc++;
        addr_abs &= 0x00FF;
        return 0;
    }

    public int REL() {
        addr_rel = read(pc);
        pc++;
        if ((addr_rel & 0x80) == (1 << 7)) {
            addr_rel |= 0xFF00;
        }
        return 0;
    }

    public int ABS() {
        int lo = read(pc);
        pc++;
        int hi = read(pc);
        pc++;
        addr_abs = (hi << 8) | lo;
        return 0;
    }

    public int ABX() {
        int lo = read(pc);
        pc++;
        int hi = read(pc);
        pc++;
        addr_abs = (hi << 8) | lo;
        addr_abs += regX;
        return ((addr_abs & 0xFF00) != (hi << 8)) ? 1 : 0;
    }

    public int ABY() {
        int lo = read(pc);
        pc++;
        int hi = read(pc);
        pc++;
        addr_abs = (hi << 8) | lo;
        addr_abs += regY;
        return ((addr_abs & 0xFF00) != (hi << 8)) ? 1 : 0;
    }

    public int  IND() {
        int lo_ptr = read(pc);
        pc++;
        int hi_ptr = read(pc);
        pc++;
        int ptr = (hi_ptr << 8) | lo_ptr;
        if (lo_ptr == 0x00FF) {
            addr_abs = (read(ptr & 0xFF00) << 8) | read(ptr + 0);
        } else {
            addr_abs = (read(ptr +1) << 8) | read(ptr + 0);
        }
        return 0;
    }

    public int IZX() {
        int orig = read(pc);
        pc++;
        int lo = read((orig + regX) & 0x00FF);
        int hi = read((orig + regX + 0x1) & 0x00FF);
        addr_abs = (hi << 8) | lo;
        return 0;
    }

    public int IZY() {
        int orig = read(pc);
        pc++;
        int lo = read(orig & 0x00FF);
        int hi = read((orig + 0x1) & 0x00FF);
        addr_abs = (hi << 8) | lo;
        addr_abs += regY;
        return ((addr_abs & 0xFF00) != (hi << 8)) ? 1 : 0;
    }


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
