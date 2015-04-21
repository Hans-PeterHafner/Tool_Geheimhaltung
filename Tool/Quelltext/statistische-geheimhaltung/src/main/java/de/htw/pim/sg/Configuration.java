package de.htw.pim.sg;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.Properties;

/**
 * Konfiguration für die Dezimalzahlen
 * Created with IntelliJ IDEA.
 * User: Kirill Povolotskyy
 */
public class Configuration {
    public static final NumberFormat numberFormat;

    private static final String defaultNumberFormat = "DE";
    private static final int defaultMaximumFractionDigits = 5;
    private static final String configFile = "config.properties";

    private static final String numberFormatString = "numberFormat";
    private static final String maximumFractionDigitsString = "numberFormat";


    static {
        Properties defaultProps = createDefaultProps();
        Properties props = new Properties(defaultProps);
        FileInputStream in;
        try {
            in = new FileInputStream(configFile);
            props.load(in);
            in.close();
        } catch (Exception e) {
        }

        numberFormat = NumberFormat.getNumberInstance(new Locale(props.getProperty(numberFormatString).toString()));

        int maximumFractionDigits;
        try {
            maximumFractionDigits = Integer.parseInt(props.getProperty(maximumFractionDigitsString).toString());
        } catch (Exception e) {
            maximumFractionDigits = defaultMaximumFractionDigits;
            props.put("maximumFractionDigits", defaultMaximumFractionDigits);
        }
        numberFormat.setMaximumFractionDigits(maximumFractionDigits);

        if(props.isEmpty()){
            saveDefaultProperties(defaultProps);
        }

    }

    private static void saveDefaultProperties(Properties props) {
        FileOutputStream out;
        try {
            out = new FileOutputStream(configFile);
            props.store(out, "---Konfigurationsdatei---");
            out.close();
        } catch (Exception e) {
        }
    }


    private static Properties createDefaultProps() {
        Properties defaultProps = new Properties();
        defaultProps.put(numberFormatString, defaultNumberFormat);
        defaultProps.put(maximumFractionDigitsString, Integer.toString(defaultMaximumFractionDigits));
        return defaultProps;
    }
}
