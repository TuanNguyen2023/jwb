/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.base.serials;

import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import javax.swing.JFormattedTextField;
import org.apache.log4j.Logger;
import com.gcs.wb.WeighBridgeApp;
import javax.swing.JOptionPane;

/**
 * Handles the input coming from the serial port. A new line character
 * is treated as the end of a block in this example.
 * @author Tran-Vu
 */
public class SerialReaderEventBased implements SerialPortEventListener {

    private InputStream in;
    private JFormattedTextField control;
    private int times_delay;
    private int count;

    public SerialReaderEventBased(InputStream in, JFormattedTextField control) throws IOException {
        this.in = in;
        this.control = control;
        this.times_delay = WeighBridgeApp.time_delay;
        this.count = 0;
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

    @Override
    public void serialEvent(SerialPortEvent e) {
        /*
        //comment out 20101209#01 re-write receiving data from weighing kit
        try {
        byte[] b = new byte[1024];
        int len = -1;
        BigInteger ival = BigInteger.ZERO;
        while ((len = this.in.read(b)) > -1) {
        String val = new String(b, 0, len);
        //try{
        val = val.substring(7, 14);
        //} catch (Exception ex) {
        val = val.toString();
        //}
        //try {
        ival = new BigInteger(val);
        // } catch (Exception ex){
        ival = BigInteger.ZERO;;
        // }
        control.setValue(ival);
        control.repaint(500);
        //hau
        //String msg = ival.toString();
        //Logger.getLogger(ScaleMettler.class.getName()).error(msg);
        //JOptionPane.showMessageDialog(WeighBridgeApp.getApplication().getMainFrame(), msg);
        //                Logger.getLogger(SerialReaderEventBased.class.getName()).error("control value:"+control.getValue());
        System.out.println("control value:"+control.getValue());
        //                Logger.getLogger(SerialReaderEventBased.class.getName()).error("control text:"+control.getText());
        System.out.println("control text:"+control.getText());
        }} catch (IndexOutOfBoundsException ex) {
        //JOptionPane.showMessageDialog(WeighBridgeApp.getApplication().getMainFrame(), 'out of bound');
        Logger.getLogger(SerialReaderEventBased.class.getName()).error(null, ex);
        } catch (IOException ex) {
        Logger.getLogger(SerialReaderEventBased.class.getName()).error(null, ex);
        }
         */    //comment out 20101209#01
//+{20101209#01 re-write receiving data from weighing kit

        switch (e.getEventType()) {
            case SerialPortEvent.BI:
                break;
            case SerialPortEvent.OE:
                break;
            case SerialPortEvent.FE:
                break;
            case SerialPortEvent.PE:
                break;
            case SerialPortEvent.CD:
                break;
            case SerialPortEvent.CTS:
                break;
            case SerialPortEvent.DSR:
                break;
            case SerialPortEvent.RI:
                break;
            case SerialPortEvent.OUTPUT_BUFFER_EMPTY:
                break;
            case SerialPortEvent.DATA_AVAILABLE:
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

                        //System.out.println(WeighBridgeApp.getApplication().getLast());
                    } catch (Exception ex) {
                        ival = BigInteger.ZERO;
                    }
                    //control.setValue(ival);

                    //Times of delay to refresh screen
                    if (this.count > this.times_delay) {
                        WeighBridgeApp.getApplication().setLast(WeighBridgeApp.getApplication().getNow());
                        WeighBridgeApp.getApplication().setNow(ival);
                        if (WeighBridgeApp.getApplication().getNow().doubleValue() > WeighBridgeApp.getApplication().getMax().doubleValue()) {
                            WeighBridgeApp.getApplication().setMax(WeighBridgeApp.getApplication().getNow());
                        }
                        control.setValue(ival);
                        control.repaint(500);
                        //for (int i = 1 ; i <= 1500 ; i++)
                        // {
                        // double garbage = Math.PI * Math.PI;
                        // }
                        this.count = 0;
                    } else {
                        this.count++;
                    }
                    //System.out.println("Read: "+result);
                    //try
                    //{
                    //Thread.sleep(500); // do nothing for 1000 miliseconds (1 second)
                    //}
                    //catch(InterruptedException ex)
                    //{
                    //}
                } catch (IOException ex) {
                }

                break;
        }

//+}20101209#01


    }
}
