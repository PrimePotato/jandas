package io.github.primepotato.jandas.visuals;


import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import io.github.primepotato.jandas.Jandas;
import io.github.primepotato.jandas.dataframe.DataFrame;

import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataFrameVisuals {

    Configuration config;
    DataFrame dataFrame;

    public DataFrameVisuals(DataFrame dataFrame) throws IOException {
        config = createConfig();
        this.dataFrame = dataFrame;
    }

    public static Configuration createConfig() throws IOException {
        Configuration cfg = new Configuration(Configuration.VERSION_2_3_28);
        cfg.setDirectoryForTemplateLoading(new File("src/main/java/io/github/primepotato/jandas/visuals/templates"));
        cfg.setDefaultEncoding("UTF-8");
        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        cfg.setLogTemplateExceptions(false);
        cfg.setWrapUncheckedExceptions(true);
        return cfg;
    }


    public void dtPlot(String templatePath, String outputPath) throws IOException, TemplateException {
        if (outputPath==null) {
            outputPath = "output.html";
        }
        Template template = config.getTemplate(templatePath);
        File file = new File(outputPath);
        Writer fileWriter = new FileWriter(file);

        try {
            template.process(dataFrame.toMap(), fileWriter);
        } finally {
            fileWriter.close();
        }

        try {
            Desktop.getDesktop().browse(file.toURI());
        } catch (IOException e) {

        }

    }


}
