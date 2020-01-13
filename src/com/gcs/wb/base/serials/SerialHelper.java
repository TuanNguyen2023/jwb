/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.base.serials;

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.PortInUseException;
import java.util.Enumeration;
import java.util.HashSet;
import org.apache.log4j.Logger;

/**
 *
 * @author Tran-Vu
 */
public class SerialHelper {

    public static HashSet<CommPortIdentifier> getAvailableSerialPorts() {
        HashSet<CommPortIdentifier> h = new HashSet<CommPortIdentifier>();
        Enumeration thePorts = CommPortIdentifier.getPortIdentifiers();
        while (thePorts.hasMoreElements()) {
            CommPortIdentifier com = (CommPortIdentifier) thePorts.nextElement();
            switch (com.getPortType()) {
                case CommPortIdentifier.PORT_SERIAL:
                    try {
                        CommPort thePort = com.open("SerialHelper", 50);
                        thePort.close();
                        h.add(com);
                    } catch (PortInUseException e) {
                        Logger.getLogger(SerialHelper.class.getName()).error("Port, " + com.getName() + ", is in use.", e);
                    } catch (Exception e) {
                        Logger.getLogger(SerialHelper.class.getName()).error("Failed to open port " + com.getName(), e);
                    }
            }
        }
        return h;
    }
}
