/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.base.serials;

import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.util.HashMap;
import javax.swing.JFormattedTextField;
import com.gcs.wb.WeighBridgeApp;
import com.gcs.wb.jpa.entity.Configuration;
import org.apache.log4j.Logger;

/**
 * Handles the input coming from the serial port. A new line character
 * is treated as the end of a block in this example.
 * @author Tran-Vu
 */
public class SerialReaderEventBased implements SerialPortDataListener {

    private InputStream in;
    private JFormattedTextField control;
    private int times_delay;
    private int count;
    private Configuration configuration = WeighBridgeApp.getApplication().getConfig().getConfiguration();

    public SerialReaderEventBased(InputStream in, JFormattedTextField control) throws IOException {
        this.in = in;
        this.control = control;
        this.times_delay = WeighBridgeApp.TIME_DELAY;
        this.count = 0;
    }

    @Override
    public int getListeningEvents() {
        return SerialPort.LISTENING_EVENT_DATA_AVAILABLE;
    }

    public String getWeight(String result) {
        int len = result.length();
        int count = 0;
        char character = ' ';
        Character char_string = (char) ' ';
        String old_number = "";
        String new_number = "";
        char flag = 'X';
        //Example 'ASA12345AAD8888AS12345A' --> old_number = 8888
        while (count < len) {
            try {
                character = result.charAt(count);
            } catch (Exception ex) {
                continue;
            }
            char_string = null;
            char_string = character;
            if (char_string.toString().matches("[0-9]")) {
                if (flag == '1') {
                    new_number = new_number.concat(char_string.toString());
                } else {
                    new_number = "";
                    new_number = new_number.concat(char_string.toString());
                }
                flag = '1';
            } else {
                if (flag == '1') {
                    old_number = new_number;
                    new_number = "";
                } else {
                }
                flag = '0';
            }
            count++;
        }
        //if (current_number == null ? old_number != null : !current_number.equals(old_number)){
        if (old_number == null ? "" == null : old_number.equals("")) {
            result = new_number;
        } else {
            result = old_number;
        }
//        if (WeighBridgeApp.getApplication().getLast().intValue() != 0 && WeighBridgeApp.getApplication().getNow().intValue() != 0) {

        if ((Integer.parseInt(result) == 0 || result == null) && !(WeighBridgeApp.getApplication().getNow().doubleValue() < WeighBridgeApp.getApplication().getMax().doubleValue()) && (WeighBridgeApp.getApplication().getNow().doubleValue() != 0)) {
            result = WeighBridgeApp.getApplication().getLast().toString();
            WeighBridgeApp.getApplication().setNow(WeighBridgeApp.getApplication().getLast());
        }




        return result;
    }

    private void delay(long time) {
        long start = System.currentTimeMillis();
        while(System.currentTimeMillis() - start < time){
            // delay
        }
    }

    private int getSeqSignalSize() {
        try {
            String[] mettlerParam = configuration.getWb1MettlerParam().split("-");
            return Integer.parseInt(mettlerParam[0]);
        } catch (Exception e) {}
        return 0;
    }

    private void processSignals() {
        // we get here if data has been received
        byte[] readBuffer = new byte[20];
        try {
            // read data
            while (in.available() > 0) {
                int numBytes = in.read(readBuffer);
            }
            // print data
            String result = new String(readBuffer);

            //result = result.replaceAll( "[^\\d]", "" );
            result = this.getWeight(result);

            BigInteger ival = BigInteger.ZERO;
            try {
                ival = new BigInteger(result);

            } catch (Exception ex) {
                ival = BigInteger.ZERO;
            }
            //control.setValue(ival);

            //Times of delay to refresh screen
            if (this.count > this.times_delay) {
                Logger.getLogger(this.getClass()).error("@jSerialComm, value@" + result);
                WeighBridgeApp.getApplication().setLast(WeighBridgeApp.getApplication().getNow());
                WeighBridgeApp.getApplication().setNow(ival);
                if (WeighBridgeApp.getApplication().getNow().doubleValue() > WeighBridgeApp.getApplication().getMax().doubleValue()) {
                    WeighBridgeApp.getApplication().setMax(WeighBridgeApp.getApplication().getNow());
                }
                control.setValue(ival);
                control.repaint(500);
                this.count = 0;
            } else {
                this.count++;
            }

            delay(configuration.getWb1Delay());
        }
        catch (IOException ex) {
        }
    }

    // Fix for Tay Ninh - Tram Can Mo
    private void processSeqSignals(int size) {
        // we get here if data has been received
        HashMap<Character, String> numbers = new HashMap<Character, String>();
        String result = "";
        StringBuilder buffer = new StringBuilder();
        String[] arrNum = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "+", "."};

        for (int i = 0; i < arrNum.length; i++) {
            numbers.put(arrNum[i].charAt(0), String.valueOf(i));
        }
        numbers.put("+".charAt(0), "+");
        numbers.put(".".charAt(0), ".");

        try {
            while (in.available() > 0) {
                int b = in.read();
                if (b == 0x0002 || b == 0x0003) // || b == 0x002E || !(b > 0x002F && b < 0x003A)) {
                {
                    buffer = new StringBuilder();
                    continue;
                }
                Character c = (char) b;
                String val = c.toString();
                if (val == null) {
                    buffer = new StringBuilder();
                    continue;
                }
                buffer.append(val);
                String strVal = buffer.toString().trim();
                result = strVal;

                if(strVal.length() >= size) {
                    result = this.getWeight(result);
                    BigInteger ival = BigInteger.ZERO;
                    try {
                        ival = new BigInteger(result);

                    } catch (Exception ex) {
                        ival = BigInteger.ZERO;
                    }

                    //Times of delay to refresh screen
                    if (this.count > this.times_delay) {
                        Logger.getLogger(this.getClass()).error("@jSerialComm, seq value@ " + result);
                        WeighBridgeApp.getApplication().setLast(WeighBridgeApp.getApplication().getNow());
                        WeighBridgeApp.getApplication().setNow(ival);
                        if (WeighBridgeApp.getApplication().getNow().doubleValue() > WeighBridgeApp.getApplication().getMax().doubleValue()) {
                            WeighBridgeApp.getApplication().setMax(WeighBridgeApp.getApplication().getNow());
                        }
                        control.setValue(ival);
                        control.repaint(500);
                        this.count = 0;
                    } else {
                        this.count++;
                    }
                }
            }
        } catch (IOException ex) {}
    }

    @Override
    public void serialEvent(SerialPortEvent event) {
        if (event.getEventType() == SerialPort.LISTENING_EVENT_DATA_AVAILABLE) {
            int size = getSeqSignalSize();
            if (size > 0) {
                processSeqSignals(size);
            } else {
                processSignals();
            }
        }
    }
}
