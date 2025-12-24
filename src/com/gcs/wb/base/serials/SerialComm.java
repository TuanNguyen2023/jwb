/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.base.serials;

import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortInvalidPortException;
import com.gcs.wb.base.exceptions.IllegalPortException;
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
    private InputStream inStream = null;
    private SerialPort serialPort = null;

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

    public void connect() throws SerialPortInvalidPortException, IllegalPortException, IOException, TooManyListenersException {
        try {
            serialPort = SerialPort.getCommPort(portName);
        } catch (SerialPortInvalidPortException ex) {
            Logger.getLogger(SerialPort.class.getName()).error(ex.getLocalizedMessage(), ex);
            throw ex;
        }
        if (serialPort.isOpen()) {
            String msg = "Cổng " + portName + " đang được sử dụng. Vui lòng kiểm tra lại.";
            Logger.getLogger(SerialPort.class.getName()).error(msg);
            throw new SerialPortInvalidPortException(msg);
        } else {
            serialPort.setComPortParameters(speed, dataBits, stopBits, parity);
            serialPort.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, 2000, 0);
            if (serialPort.openPort()) {
                try {
                    inStream = serialPort.getInputStream();
                    serialPort.addDataListener(new SerialReaderEventBased(inStream, control));
                } catch (Exception ex) {
                    Logger.getLogger(SerialPort.class.getName()).error(null, ex);
                    throw ex;
                }
            } else {
                String msg = "Kết nối tới cầu cân không thành công. Vui lòng kiểm tra lại.";
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
        if (serialPort != null) {
            serialPort.closePort();
        }
    }
}
