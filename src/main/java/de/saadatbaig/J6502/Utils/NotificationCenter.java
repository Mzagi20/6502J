package de.saadatbaig.J6502.Utils;

import de.saadatbaig.J6502.Models.Bus;
import de.saadatbaig.J6502.Models.CPU;

import java.util.HashMap;

public class NotificationCenter {

    ///////////////////////////////////////////////////////////////////////////
    // MEMBERS
    ///////////////////////////////////////////////////////////////////////////
    private static NotificationCenter nc = null;
    private static HashMap<String, Object> map = null;


    ///////////////////////////////////////////////////////////////////////////
    // CTOR + INITS
    ///////////////////////////////////////////////////////////////////////////
    private NotificationCenter() { map = new HashMap<>(); }

    public static NotificationCenter sharedInstance() {
        if (nc == null) {
            nc = new NotificationCenter();
        }
        return nc;
    }


    ///////////////////////////////////////////////////////////////////////////
    // FUNCTIONS
    ///////////////////////////////////////////////////////////////////////////
    public void registerWithIdentifier(String _id, Object _obj) {
        map.put(_id, _obj);
        System.out.println("Added id: " + _id);
        System.out.println("Object: " + _obj.toString());
    }

    public void sendMessageToBus(String _id, String _msg) {
        if (map.containsKey(_id)) {
            ((Bus) map.get(_id)).receiveMessage(_msg);
        } else {
            System.out.println("key not found BUS");    // Debug
        }
    }

    public void sendMessageToCPU(String _id, String _msg) {
        if (map.containsKey(_id)) {
            ((CPU) map.get(_id)).receiveMessage(_msg);
        } else {
            System.out.println("key not found CPU");    // Debug
        }
    }


    /* End */
}
