package de.htw.pim.sg.table.transformer;

import com.google.common.base.Function;
import de.htw.pim.sg.Configuration;

import java.text.NumberFormat;


public class CustomDoubleToStringTransformer implements Function<Double,String> {

    private NumberFormat nf;

    public CustomDoubleToStringTransformer(NumberFormat nf){
        this.nf = nf;
    }

    public String apply(Double input) {
        
        return nf.format(input);
    }
    
}




