/*
 * This driver is written for Openbravo POS to communicate with Mettler Scales
 * The Protocol used on Mettler scales is: MT - SICS
 * ScaleSamsungEsp.java modified by Shameer C K, shameerck@gmail.com
 * 12/24/2009
 *
 *
 */
package com.gcs.wb.base.serials;

import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;
import com.fazecast.jSerialComm.SerialPortInvalidPortException;
import com.gcs.wb.WeighBridgeApp;
import com.gcs.wb.base.exceptions.IllegalPortException;
import com.gcs.wb.jpa.entity.Configuration;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.TooManyListenersException;


import java.math.BigInteger;
import java.util.HashMap;
import javax.swing.JFormattedTextField;
import org.apache.log4j.Logger;

public class ScaleMettler implements SerialPortDataListener {

    private String portName = null;
    private Integer speed = null;
    private Short dataBits = null;
    private Short stopBits = null;
    private Short parity = null;
    private JFormattedTextField control = null;
    private SerialPort serialPort = null;
    private OutputStream out;
    private InputStream in;
    private int count = 0;
    private StringBuilder buffer = new StringBuilder();
    private Configuration configuration = WeighBridgeApp.getApplication().getConfig().getConfiguration();
    
    /** Creates a new instance of ScaleComm */
    public ScaleMettler(String portName, Integer speed, Short dataBits, Short stopBits, Short parity, JFormattedTextField control) {
        this.portName = portName;
        this.speed = speed;
        this.dataBits = dataBits;
        this.stopBits = stopBits;
        this.parity = parity;
        this.control = control;
    }

    public void connect() throws SerialPortInvalidPortException, IOException, TooManyListenersException, IllegalPortException {
        if (out == null) {
            try {
                serialPort = SerialPort.getCommPort(portName);

                Logger.getLogger("......");
            } catch (SerialPortInvalidPortException ex) {
                Logger.getLogger(ScaleMettler.class.getName()).error(ex.getLocalizedMessage(), ex);
                throw ex;
            }

            if (serialPort.isOpen()) {
                String msg = "Error: Port " + portName + " is currently in use";
                Logger.getLogger(SerialPort.class.getName()).error(msg);
                throw new SerialPortInvalidPortException();
            } else {
                serialPort.setComPortParameters(speed, dataBits, stopBits, parity);
                serialPort.setComPortTimeouts(SerialPort.TIMEOUT_WRITE_BLOCKING, 2000, 0);
                if (serialPort.openPort()) {
                    try {
                        in = serialPort.getInputStream();
                        out = serialPort.getOutputStream();
                        serialPort.addDataListener(this);
                    } catch (Exception ex) {
                        Logger.getLogger(ScaleMettler.class.getName()).error(null, ex);
                        throw ex;
                    }
                } else {
                    String msg = "Error ";
                    Logger.getLogger(ScaleMettler.class.getName()).error(msg);
                    throw new IllegalPortException(msg);
                }
            }
        }
        write(new byte[]{0x53}); //S
        write(new byte[]{0x0D}); //S
        write(new byte[]{0x0A}); //S

        flush();
    }

    private void flush() {
        try {
            out.flush();
        } catch (IOException e) {
        }
    }

    private void write(byte[] data) {
        try {
            out.write(data);
        } catch (IOException ex) {
            Logger.getLogger(ScaleMettler.class.getName()).error(null, ex);
        }
    }

    public String hexToAscii(String hexStr) {
        StringBuilder output = new StringBuilder("");

        for (int i = 0; i < hexStr.length(); i += 2) {
            String str = hexStr.substring(i, i + 2);
            output.append((char) Integer.parseInt(str, 16));
        }

        return output.toString();
    }
    
    public void serialEvent(SerialPortEvent e) {
        HashMap<Character, String> numbers = new HashMap<Character, String>();
        //  String remove_string = "00000010";

        String[] arrNum = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "+", "."};

        for (int i = 0; i < arrNum.length - 2; i++) {
            numbers.put(arrNum[i].charAt(0), String.valueOf(i));
        }
        numbers.put("+".charAt(0), "+");
        numbers.put(".".charAt(0), ".");

        if (e.getEventType() != SerialPort.LISTENING_EVENT_DATA_AVAILABLE) {
            return;
        }
        try {
            while (in.available() > 0) {
                int b = in.read();
                if (b == 0x0002 || b == 0x0003) // || b == 0x002E || !(b > 0x002F && b < 0x003A)) {
                {
                    buffer = new StringBuilder();
                    continue;
                }
                Character c = (char) b;
                String val = numbers.get(c);
                if (val == null) {
                    buffer = new StringBuilder();
                    continue;
                }
                buffer.append(val);
                String strVal = buffer.toString().trim();
                
                String wb1MettlerParam = configuration.getWb1MettlerParam();
                String[] mettlerParam = wb1MettlerParam.split("-");
                
                int size = Integer.parseInt(mettlerParam[0]);
                if(strVal.length() == size) {
                    Logger.getLogger(this.getClass()).error("@ScaleMettler, Value@" + strVal);
                    strVal = strVal.substring(Integer.parseInt(mettlerParam[1]), Integer.parseInt(mettlerParam[2]));
                    BigInteger intVal = new BigInteger(strVal);
                    control.setValue(intVal);
                    control.repaint(90);
                    buffer = new StringBuilder();
                    strVal = null;

                }
            }
            
        } catch (IOException ex) {
            Logger.getLogger(ScaleMettler.class.getName()).error(null, ex);
        }
    }
     
    public void disconnect() throws IOException {
        if (in != null) {
            try {
                in.close();
            } catch (IOException ex) {
                Logger.getLogger(ScaleMettler.class.getName()).error(null, ex);
                throw ex;
            }
        }
        if (out != null) {
            try {
                out.close();
            } catch (IOException ex) {
                Logger.getLogger(ScaleMettler.class.getName()).error(null, ex);
                throw ex;
            }
        }
        if (serialPort != null) {
            serialPort.closePort();
        }
    }

    @Override
    public int getListeningEvents() {
        return SerialPort.LISTENING_EVENT_DATA_AVAILABLE;
    }
}
