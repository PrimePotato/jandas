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

    private DataFrame dfFreshmen;
    private DataFrame dfLdnElection;
    private DataFrame dfVoteShare;


    @Before
    public void setUp() {

        dfFreshmen = Jandas.readCsv("src/test/resources/csv/freshman_kgs.csv");
        dfLdnElection = Jandas.readCsv("src/test/resources/csv/general-elections-votes-party-2015.csv");
        dfVoteShare = Jandas.readCsv("src/test/resources/csv/party_constituency_vote_shares.csv");
    }

    @Test
    public void columnOperations() {

        DoubleColumn dc = dfFreshmen.column("BMI (Sep)");
        System.out.println(dc.scale(2));
    }

    @Test
    public void columnEquations() {

        DoubleColumn a = dfFreshmen.column("BMI (Sep)");
        DoubleColumn b = dfFreshmen.column("BMI (Apr)");
        DoubleColumn c = new DoubleColumn("Mid", false, new double[0]);

        Equation eq = new Equation();
        eq.alias(a.getMatrix(), "a", b.getMatrix(), "b", c.getMatrix(), "c");
        eq.process("c = (a+b)/2");

        dfFreshmen.addColumn(c);
        dfFreshmen.print();

    }

    @Test
    public void equations() {
        dfFreshmen.createColumn("avg", "(BMISep+BMIApr)/2.0");
        dfFreshmen.print();
    }

    @Test
    public void groupBy() {

        DataFrameGroupBy grp = dfFreshmen.groupBy(Arrays.asList("Sex"), Arrays.asList("BMI (Apr)", "BMI (Sep)"));
        DataFrame df = grp.aggregate(DoubleAggregateFunc.MEAN);
        df.print();
    }

    @Test
    public void quickJoin() {

        DataFrame dfJoin = dfVoteShare.quickJoin(Arrays.asList("Constituency"), dfLdnElection, JoinType.LEFT);
        dfJoin.print();

    }

    @Test
    public void createColumn() {
        dfVoteShare.createColumn("Signal", "exp(Con)+log(Lab)");
        dfVoteShare.print();
    }

    @Test
    public void print() {
        dfFreshmen.print();
    }

    @Test
    public void join() {
        DataFrame dfJoin;

        dfJoin = dfLdnElection.join(Arrays.asList("Constituency"), dfVoteShare, JoinType.INNER);
        dfJoin.print();

        dfJoin = dfLdnElection.join(Arrays.asList("Constituency"), dfVoteShare, JoinType.LEFT);
        dfJoin.print();

        dfJoin = dfLdnElection.join(Arrays.asList("Constituency"), dfVoteShare, JoinType.RIGHT);
        dfJoin.print();
    }

}
