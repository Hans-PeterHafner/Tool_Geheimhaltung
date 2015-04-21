package de.htw.pim.sg.table.transformer;

import com.google.common.base.Function;
import de.htw.pim.sg.Configuration;

import java.text.NumberFormat;
import java.text.ParseException;

public class StringToDoubleTransformer implements Function<String, Double> {

    NumberFormat nf;

    public StringToDoubleTransformer (NumberFormat nf){
        this.nf = nf;
    }

    @Override
    public Double apply(String input) {
        
        try {
        
            return nf.parse(input).doubleValue();
            
        } catch (NumberFormatException e) {
            
            return Double.NaN;
        } catch (ParseException e) {
            return Double.NaN;
        }
    }
    
}
