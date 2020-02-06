package io.github.primepotato.jandas;

import io.github.primepotato.jandas.dataframe.DataFrame;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class JandasTest {

    private DataFrame dataFrame;

    @Test
    public void readCsv() {

        DataFrame df = Jandas.readCsv("src/test/resources/csv/biostats.csv", Arrays.asList("Name", "Sex", "Age"));
        df.print();
    }

    @Test
    public void readCsv2() {

        DataFrame df = Jandas.readCsv("src/test/resources/csv/biostats.csv");
        df.print();
    }

    @Test
    public void readCsv3() {
        Map<String, Class> map = new HashMap<>();
        map.put("Name", String.class);
        map.put("Sex", String.class);
        map.put("Age", Double.class);

        DataFrame df = Jandas.readCsv("src/test/resources/csv/biostats.csv", Arrays.asList("Name", "Sex", "Age"), map);
        df.print();
    }


}