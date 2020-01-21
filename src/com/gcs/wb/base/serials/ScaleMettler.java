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
import com.gcs.wb.base.exceptions.IllegalPortException;
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

    /*
     * 
    is
     */
//    @SuppressWarnings("element-type-mismatch")
    //  @Override
        /*
    public void serialEvent(SerialPortEvent e) {
    HashMap<Character, String> numbers = new HashMap<Character, String>();
    //    String remove_string = "00000010" twb;       
    String remove_string1 = "01D01D" + hexToAscii("0x0003") + hexToAscii("0x0002") ; //  twb;       
    String remove_string2 = hexToAscii("0x0003");  
    // String remove_string = "012012"+ hexToAscii("0x0003") + hexToAscii("0x0002");
    //  String remove_string =  hexToAscii("0x0003") + hexToAscii("0x0002");
    
    for (int i = 0; i < 10; i++) {
    numbers.put(String.valueOf(i).charAt(0), String.valueOf(i));
    }
    
    switch (e.getEventType()) {
    case SerialPortEvent.BI:
    case SerialPortEvent.OE:
    case SerialPortEvent.FE:
    case SerialPortEvent.PE:
    case SerialPortEvent.CD:
    case SerialPortEvent.CTS:
    case SerialPortEvent.DSR:
    case SerialPortEvent.RI:
    case SerialPortEvent.OUTPUT_BUFFER_EMPTY:
    break;
    case SerialPortEvent.DATA_AVAILABLE:
    try {
    while (in.available() > 0) {
    int b = in.read();
    //   if (b == 0x000D || b == 0x002E || !(b > 0x002F && b < 0x003A)) 
    if (b == 0x002B || b == 0x002E || !(b > 0x002F && b < 0x003A)) 
    {
    Logger.getLogger ( " move >>>>");
    // buffer.append("x"); // tuanna 
    continue;
    
    }
    Character c = (char) b;
    String val = numbers.get(c);
    if (val == null) {
    continue;
    }
    buffer.append(val);
    String strVal = buffer.toString().trim();
    int found1= strVal.indexOf(remove_string1);
    int found2  =  strVal.indexOf(remove_string2);
    int found = found1 > found2 ? found1 : found2; 
    
    if (found > 0) {
    count++;
    int bIdx = found - 6;
    if (bIdx >= 0) {
    strVal = strVal.substring(bIdx, found);
    Logger.getLogger ( strVal ); 
    BigInteger intVal = new BigInteger(strVal);
    control.setValue(intVal);
    control.repaint(90);                        
    buffer = new StringBuilder();
    Logger.getLogger ( " move >>>>" +intVal.toString() );
    strVal = null;
    }
    }
    
    }
    } catch (IOException ex) {
    Logger.getLogger(ScaleMettler.class.getName()).error(null, ex);
    }
    break;
    }
    
    }
     */
    public void serialEvent(SerialPortEvent e) {
        HashMap<Character, String> numbers = new HashMap<Character, String>();
        //  String remove_string = "00000010";
        String remove_string = "01A";
        String[] arrStr = {"013", "014", "015", "018", "012", "011", "01A", "01B", "01C", "01D", "01E", "01F"};


        //   String remove_string = "019019"+ hexToAscii("0x0003") + hexToAscii("0x0002");
        //  String remove_string =  hexToAscii("0x0003") + hexToAscii("0x0002");

        String[] arrNum = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "+"};

        for (int i = 0; i < arrNum.length - 1; i++) {
            //  numbers.put(String.valueOf(i).charAt(0), String.valueOf(i));
            numbers.put(arrNum[i].charAt(0), String.valueOf(i));
        }
        numbers.put("+".charAt(0), "+");

        if (e.getEventType() != SerialPort.LISTENING_EVENT_DATA_AVAILABLE) {
            return;
        }
        try {
            while (in.available() > 0) {
                int b = in.read();
                //     if (b == 0x000D || b == 0x002E || !(b > 0x002F && b < 0x003A)) {
                if (b == 0x0002 || b == 0x0003) // || b == 0x002E || !(b > 0x002F && b < 0x003A)) {
                {
                    continue;
                }
                Character c = (char) b;
                String val = numbers.get(c);
                if (val == null) {
                    continue;
                }
                buffer.append(val);
                String strVal = buffer.toString().trim();
                /*
                int min = strVal.indexOf(remove_string);
                int found ;
                int ismax = -1  ;
                int k = -1; 
                int kfound = -1 ; 
                int bIdx  ;
                for (int i = 0 ; i < 12 ; i++  )
                {
                found =  strVal.indexOf(arrStr[i]); 
                
                bIdx = found - 6; 
                if ( bIdx >= ismax  && bIdx >=5  && found > 0 )
                {
                ismax = bIdx ;
                k= i; 
                kfound = found ; 
                }                           
                }
                
                if (kfound > 0  ) {
                count++;
                bIdx = kfound - 6;
                // int bIdx = found - 12 ; 
                if (bIdx >= 0) {
                strVal = strVal.substring(ismax, kfound);
                //   strVal = strVal.substring(found+1, end);
                BigInteger intVal = new BigInteger(strVal);
                control.setValue(intVal);
                control.repaint(90);                        
                buffer = new StringBuilder();
                
                strVal = null;
                }
                }
                
                 */

                /* 
                int found = strVal.indexOf(remove_string);
                if (found > 0) {
                count++;
                int bIdx = found - 6;
                if (bIdx >= 0) {
                strVal = strVal.substring(bIdx, found);
                BigInteger intVal = new BigInteger(strVal);
                control.setValue(intVal);
                control.repaint(90);                        
                buffer = new StringBuilder();
                
                strVal = null;
                }
                }
                 */


                //Whatever the file path is.

                /* File statText = new File("D:\\log.txt"); 
                FileOutputStream is = new FileOutputStream(statText);
                OutputStreamWriter osw = new OutputStreamWriter(is);    
                Writer w = new BufferedWriter(osw);
                w.write("POTATO!!!");
                 */
                int found = strVal.indexOf("+");
                if (found >= 0) {
                    int len = 6;
                    strVal = strVal.substring(found + 1, found + 1 + len);
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
