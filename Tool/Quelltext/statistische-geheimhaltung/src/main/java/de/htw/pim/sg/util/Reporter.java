package de.htw.pim.sg.util;

import de.htw.pim.sg.gui.methods.AbstractMethod;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author Kirill Povolotskyy
 *         Date: 07/07/12
 */
public class Reporter {

    private static final String fileName = "report.htm";
    private static final DateFormat dateFormat = new SimpleDateFormat("MM.dd.yy HH:mm:ss");

    public static void createReport(Map<String, Map<String, String>> methods, String path){
        StringBuilder report = new StringBuilder("<html>");

        report.append("<h1>").append("Anonymisierungsbericht - ").append(dateFormat.format(new Date())).append("</h1>");
        report.append("<h2>").append("Angewandte Anonymisierungsmethoden:").append("</h2>");

        for (Map.Entry<String, Map<String, String>> method : methods.entrySet()) {
            report.append("<h3>").append(method.getKey()).append("</h3>");
            for (Map.Entry<String, String> parameter : method.getValue().entrySet()) {
                report.append(parameter.getKey()).append(": ").append(parameter.getValue()).append("<br>");
            }


        }

        report.append("</html>");

        try {
            String filePath = path + "\\" + fileName;
            FileWriter writer = new FileWriter( new File( filePath ));

            writer.write( replaceSpecialChars(report));
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }


    private static String replaceSpecialChars(StringBuilder str){
        String result = str.toString();
        result = result.replace("ö", "&ouml");
        result = result.replace("ä", "&auml");
        result = result.replace("ü", "&uuml");
        result = result.replace("ß", "&szlig");
        return result;
    }
}
