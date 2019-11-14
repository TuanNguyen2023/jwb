/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.rs232;

import com.gcs.wb.rs232.exceptions.IllegalPortException;
import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.UnsupportedCommOperationException;
import java.io.IOException;
import java.io.InputStream;
import java.util.TooManyListenersException;
import javax.swing.JFormattedTextField;
import org.apache.log4j.Logger;

/**
 *
 * @author Tran-Vu
 */
public class SerialComm {

    private String portName = null;
    private Integer speed = null;
    private Short dataBits = null;
    private Short stopBits = null;
    private Short parity = null;
    private JFormattedTextField control = null;
    private CommPortIdentifier portIdentifier = null;
    private CommPort commPort = null;
    private InputStream inStream = null;

    public SerialComm() {
    }

    public SerialComm(String portName, Integer speed, Short dataBits, Short stopBits, Short parity, JFormattedTextField control) {
        this.portName = portName;
        this.speed = speed;
        this.dataBits = dataBits;
        this.stopBits = stopBits;
        this.parity = parity;
        this.control = control;
    }

    public void connect() throws PortInUseException, IllegalPortException, NoSuchPortException, UnsupportedCommOperationException, IOException, TooManyListenersException {

        try {
            portIdentifier = CommPortIdentifier.getPortIdentifier(portName);
        } catch (NoSuchPortException ex) {
            Logger.getLogger(SerialComm.class.getName()).error(ex.getLocalizedMessage(), ex);
            throw ex;
        }
        if (portIdentifier.isCurrentlyOwned()) {
            String msg = "Error: Port " + portName + " is currently in use";
            Logger.getLogger(SerialComm.class.getName()).error(msg);
            throw new PortInUseException();
        } else {

            commPort = portIdentifier.open(this.getClass().getName() + "_" + commPort, 2000);
            if (commPort instanceof gnu.io.SerialPort) {
                try {
                    SerialPort serialPort = (SerialPort) commPort;
                    serialPort.setSerialPortParams(speed.intValue(), dataBits.intValue(), stopBits.intValue(), parity.intValue());
                    inStream = serialPort.getInputStream();
                    //inStream.reset();
                    serialPort.addEventListener(new SerialReaderEventBased(inStream, control));
                    serialPort.notifyOnDataAvailable(true);
                } catch (UnsupportedCommOperationException ex) {
                    Logger.getLogger(SerialComm.class.getName()).error(null, ex);
                    throw ex;
                } catch (IOException ex) {
                    Logger.getLogger(SerialComm.class.getName()).error(null, ex);
                    throw ex;
                } catch (TooManyListenersException ex) {
                    Logger.getLogger(SerialComm.class.getName()).error(null, ex);
                    throw ex;
                }
            } else {
                String msg = "Error: Only serial ports are handled by this example.";
                Logger.getLogger(SerialComm.class.getName()).error(msg);
                throw new IllegalPortException(msg);
            }
        }
    }

    public void disconnect() throws IOException {
        if (inStream != null) {
            try {
                inStream.close();
            } catch (IOException ex) {
                Logger.getLogger(SerialComm.class.getName()).error(null, ex);
                throw ex;
            }
        }
        if (commPort != null) {
            commPort.close();
        }
    }
}
