package de.saadatbaig.J6502.Utils.Abstractions;

public interface NotificationCenterListener {

    ///////////////////////////////////////////////////////////////////////////
    // IMPL.
    ///////////////////////////////////////////////////////////////////////////
    void registerAsListener();
    void sendMessage(String msg);
    void receiveMessage(String msg);


    /* End */
}
