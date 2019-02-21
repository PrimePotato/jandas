package io.github.primepotato.jandas.demos;

import io.github.primepotato.jandas.column.DoubleColumn;
import io.github.primepotato.jandas.dataframe.DataFrame;
import io.github.primepotato.jandas.dataframe.DataFrameGroupBy;
import io.github.primepotato.jandas.index.meta.JoinType;
import io.github.primepotato.jandas.utils.DoubleAggregateFunc;
import io.github.primepotato.jandas.Jandas;
import org.ejml.equation.Equation;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

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

        DoubleColumn a = dataFrame.column("BMI (Sep)");
        DoubleColumn b = dataFrame.column("BMI (Apr)");
        DoubleColumn c = new DoubleColumn("Mid", false, new double[0]);

        Equation eq = new Equation();
        eq.alias(a.getMatrix(), "a", b.getMatrix(), "b", c.getMatrix(), "c");
        eq.process("c = (a+b)/2");


        dataFrame.addColumn(c);
        dataFrame.print();

    }


    @Test
    public void equations() {
        dataFrame.createColumn("avg", "(BMISep+BMIApr)/2.0");
        dataFrame.print();
    }

    @Test
    public void groupBy() {

        DataFrameGroupBy grp = dataFrame.groupBy(Arrays.asList("Sex"), Arrays.asList("BMI (Apr)", "BMI (Sep)"));
        DataFrame df = grp.aggregate(DoubleAggregateFunc.MEAN);
        df.print();
    }

    @Test
    public void quickJoin() {
        DataFrame dfLdn = Jandas.readCsv("src/test/resources/general-elections-votes-party-2015.csv");
        DataFrame dfAll = Jandas.readCsv("src/test/resources/party_constituency_vote_shares.csv");

        DataFrame dfJoin = dfLdn.quickJoin(Arrays.asList("Constituency"), dfAll, JoinType.LEFT);
        dfJoin.print();

    }

    @Test
    public void join() {
        DataFrame dfLdn = Jandas.readCsv("src/test/resources/general-elections-votes-party-2015.csv");
        DataFrame dfAll = Jandas.readCsv("src/test/resources/party_constituency_vote_shares.csv");
        DataFrame dfJoin;

        dfJoin = dfLdn.join(Arrays.asList("Constituency"), dfAll, JoinType.INNER);
        dfJoin.print();

        dfJoin = dfLdn.join(Arrays.asList("Constituency"), dfAll, JoinType.LEFT);
        dfJoin.print();

        dfJoin = dfLdn.join(Arrays.asList("Constituency"), dfAll, JoinType.RIGHT);
        dfJoin.print();

    }

}
