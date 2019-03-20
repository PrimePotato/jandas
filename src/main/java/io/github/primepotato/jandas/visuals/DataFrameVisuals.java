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

    public DataFrameVisuals() throws IOException {
        config = createConfig();
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

    public void linePlot() throws IOException, TemplateException {
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("message", "Hello World!");

        List<String> countries = new ArrayList<String>();
        countries.add("India");
        countries.add("United States");
        countries.add("Germany");
        countries.add("France");

        data.put("countries", countries);

        Template template = config.getTemplate("lineChart.ftl");

        File file = new File("output.html");

        Writer fileWriter = new FileWriter(file);
        try {
            template.process(data, fileWriter);
        } finally {
            fileWriter.close();
        }

        try {
            Desktop.getDesktop().browse(file.toURI());
        } catch (IOException e) {

        }

    }

    public void dtPlot(Map data) throws IOException, TemplateException {

//        data.put("countries", countries);

        Template template = config.getTemplate("test.ftl");

        File file = new File("output.html");

        Writer fileWriter = new FileWriter(file);
        try {
            template.process(data, fileWriter);
        } finally {
            fileWriter.close();
        }

        try {
            Desktop.getDesktop().browse(file.toURI());
        } catch (IOException e) {

        }

    }

    public static void main(String[] args) throws IOException, TemplateException {

        DataFrame df = Jandas.readCsv("src/test/resources/csv/freshman_kgs.csv");
        Map map = df.toMap();
        System.out.println(System.getProperty("java.io.tmpdir"));
        DataFrameVisuals dfv = new DataFrameVisuals();
        dfv.dtPlot(map);
    }

}
