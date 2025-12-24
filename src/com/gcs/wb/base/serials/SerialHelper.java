/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.base.serials;

import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortInvalidPortException;
import java.util.HashSet;
import org.apache.log4j.Logger;

/**
 *
 * @author Tran-Vu
 */
public class SerialHelper {

    public static HashSet<SerialPort> getAvailableSerialPorts() {
        HashSet<SerialPort> h = new HashSet<>();
        SerialPort[] serialPorts = SerialPort.getCommPorts();
        for (int i = 0; i < serialPorts.length; i++) {
            SerialPort serialPort = serialPorts[i];

            try {
                serialPort.openPort();
                serialPort.closePort();
                h.add(serialPort);
            } catch (SerialPortInvalidPortException e) {
                Logger.getLogger(SerialHelper.class.getName()).error("Port, " + serialPort.getSystemPortName() + ", is in use.", e);
            } catch (Exception e) {
                Logger.getLogger(SerialHelper.class.getName()).error("Failed to open port " + serialPort.getSystemPortName(), e);
            }
        }
        return h;
    }
}
