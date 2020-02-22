package com.gcs.wb.views.validations;

import com.gcs.wb.base.constant.Constants;
import com.gcs.wb.base.validator.LengthValidator;
import com.gcs.wb.jpa.entity.OutboundDelivery;
import com.gcs.wb.jpa.entity.Vehicle;
import com.gcs.wb.jpa.repositorys.VehicleRepository;
import java.awt.Color;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.stream.Stream;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JRootPane;
import org.jdesktop.application.ResourceMap;

/**
 *
 * @author thanghl.qx
 */
public class WeightTicketRegistrationValidation {

    JRootPane rootPane;
    ResourceMap resourceMapMsg;
    LengthValidator lengthValidator;
    VehicleRepository vehicleRepository = new VehicleRepository();

    public WeightTicketRegistrationValidation(JRootPane rootPane, ResourceMap resourceMapMsg) {
        this.rootPane = rootPane;
        this.resourceMapMsg = resourceMapMsg;
    }

    public boolean validateLength(String value, JComponent label, int min, int max) {
        value = value.trim();

        try {
            lengthValidator = new LengthValidator(min, max);
            lengthValidator.validate(value);
            label.setForeground(Color.black);
            return true;
        } catch (IllegalArgumentException ex) {
            label.setForeground(Color.red);
            return false;
        }
    }

    public boolean validatePlateNo(String value, JComponent label) {
        value = value.trim();

        Matcher matcher = Constants.TransportAgent.LICENSE_PLATE_PATTERN.matcher(value);
        boolean result = matcher.matches();

        label.setForeground(result ? Color.black : Color.red);

        return result;
    }
    
    public boolean validatePlateNoWithDB(String value, JComponent label) {
        value = value.trim();

        Matcher matcher = Constants.TransportAgent.LICENSE_PLATE_PATTERN.matcher(value);
        boolean result = matcher.matches();

        if (result) {
            Vehicle vehicle = vehicleRepository.findByPlateNo(value);

            if (vehicle == null) {
                result = false;
                JOptionPane.showMessageDialog(rootPane,
                        resourceMapMsg.getString("msg.errorLicensePlate"));
            } else if (vehicle.isProhibit()) {
                result = false;
                JOptionPane.showMessageDialog(rootPane,
                        resourceMapMsg.getString("msg.invalidLicensePlate"));
            } else {
                result = true;
            }

        }
        label.setForeground(result ? Color.black : Color.red);

        return result;
    }

    public boolean validateDO(String value, JComponent label) {
        boolean result = false;
        String[] values = value.split("-");

        for (String DONum : values) {
            DONum = DONum.trim();
            try {
                Long.parseLong(DONum);
                result = true;
            } catch (NumberFormatException ex) {
                result = false;
                break;
            }
        }

        label.setForeground(result ? Color.black : Color.red);

        return result;
    }

    public boolean validateCbxSelected(int selectedIndex, JComponent label) {
        boolean result = selectedIndex != -1;
        label.setForeground(result ? Color.black : Color.red);

        return result;
    }

    public boolean validatePO(String value, JComponent label) {
        boolean result;
        value = value.trim();

        try {
            Long.parseLong(value);
            result = true;
        } catch (NumberFormatException ex) {
            result = false;
        }

        label.setForeground(result ? Color.black : Color.red);

        return result;
    }
}
