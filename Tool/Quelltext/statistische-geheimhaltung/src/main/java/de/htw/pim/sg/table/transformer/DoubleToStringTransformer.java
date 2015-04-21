package de.htw.pim.sg.table.transformer;

import com.google.common.base.Function;

import java.text.NumberFormat;

/**
 * @author Kirill Povolotskyy
 *         Date: 09/07/12
 */
public class DoubleToStringTransformer implements Function<Double,String> {


    public String apply(Double input) {

        return Double.toString(input);
    }

}