package de.saadatbaig.J6502.Utils.Abstractions;

public interface NotificationCenterListener {

    ///////////////////////////////////////////////////////////////////////////
    // IMPL.
    ///////////////////////////////////////////////////////////////////////////
    void registerAsListener(String _id);
    void sendMessage(String _id, String msg);
    void receiveMessage(String msg);


    /* End */
}
