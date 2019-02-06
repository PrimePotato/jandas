package io.github.primepotato.jandas.demos;

import io.github.primepotato.jandas.column.DoubleColumn;
import io.github.primepotato.jandas.dataframe.DataFrame;
import io.github.primepotato.jandas.utils.Jandas;
import it.unimi.dsi.fastutil.ints.Int2DoubleOpenHashMap;
import org.ejml.equation.Equation;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Map;

public class JandasDemo {

    private DataFrame dataFrame;


    @Before
    public void setUp() {

        dataFrame = Jandas.readCsv("src/test/resources/freshman_kgs.csv");

    }

    @Test
    public void columnOperations() {

        DoubleColumn ask = dataFrame.column("BMI (Sep)");
        System.out.println(ask.scale(2));
    }

    @Test
    public void columnEquations() {

        DoubleColumn ask = dataFrame.column("BMI (Sep)");
        DoubleColumn bid = dataFrame.column("BMI (Apr)");
        DoubleColumn mid = new DoubleColumn("Mid", false, new double[0]);

        Equation eq = new Equation();
        eq.alias(ask.getMatrix(), "a", bid.getMatrix(), "b", mid.getMatrix(), "m");
        eq.process("m = (a+b)/2");

        dataFrame.addColumn(mid);
        dataFrame.print(20);

    }

    @Test
    public void groupBy() {

        Map<String, Int2DoubleOpenHashMap> grp = dataFrame.groupBy(Arrays.asList("Sex"), Arrays.asList("BMI (Apr)", "BMI (Sep)"));
        System.out.print(grp);
    }

    @Test
    public void quickJoin() {
//        DataFrame df =
//                Jandas.readCsv("src/test/resources/SpotEg.csv");
//        DataFrame dfJoin = dataFrame.join(Arrays.asList("CurrencyPair"), df);
//        dfJoin.print(20);
    }

}
