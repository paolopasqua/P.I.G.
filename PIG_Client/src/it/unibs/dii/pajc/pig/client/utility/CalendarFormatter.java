package it.unibs.dii.pajc.pig.client.utility;

import javax.swing.text.DateFormatter;
import java.text.ParseException;
import java.util.Calendar;

public class CalendarFormatter extends DateFormatter {

    @Override
    public String valueToString(Object value) throws ParseException {
        if (value != null) {
            if (value instanceof Calendar) {
                Calendar cal = (Calendar) value;
                return getFormat().format(cal.getTime());
            }
        }
        return super.valueToString(value);
    }
}
