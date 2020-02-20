package io.github.primepotato.jandas.column;

import io.github.primepotato.jandas.Jandas;
import io.github.primepotato.jandas.dataframe.DataFrame;
import io.github.primepotato.jandas.header.Heading;
import io.github.primepotato.jandas.index.ColIndex;
import io.github.primepotato.jandas.io.parsers.DoubleParser;
import io.github.primepotato.jandas.io.parsers.IntParser;
import io.github.primepotato.jandas.io.parsers.StringParser;
import org.checkerframework.checker.units.qual.A;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

public class ColumnTest {

    private Column<String> strColumn;
    private Column<Integer> intColumn;
    private Column<Double> dblColumn;

    @Before
    public void setUp() throws Exception {
        DataFrame dfFreshmen = Jandas.readCsv("src/test/resources/csv/freshman_kgs.csv");
        strColumn = dfFreshmen.column("Sex");
        intColumn = dfFreshmen.column("Weight (Sep)");
        dblColumn = dfFreshmen.column("BMI (Apr)");
    }

    @Test
    public void rebuildIndex() {
        Assert.assertNotNull(strColumn.getIndex());
        Assert.assertNotNull(intColumn.getIndex());
        Assert.assertNotNull(dblColumn.getIndex());
    }

    @Test
    public void unique() {
        Assert.assertFalse(strColumn.unique());
        Assert.assertFalse(intColumn.unique());
        Assert.assertFalse(dblColumn.unique());
    }

    @Test
    public void uniqueSet() {
        Assert.assertEquals(2, strColumn.uniqueSet().size());
        Assert.assertEquals(36, intColumn.uniqueSet().size());
        Assert.assertEquals(63, dblColumn.uniqueSet().size());
    }

    @Test
    public void cleanName() {
        Assert.assertEquals("Sex", strColumn.cleanName());
        Assert.assertEquals("Weight (Sep)", intColumn.cleanName());
        Assert.assertEquals("BMIApr", dblColumn.cleanName());
    }

    @Test
    public void subColumn() {
        Assert.assertEquals(3, strColumn.subColumn("Bob", new int[]{0, 2, 4}).size());
        Assert.assertEquals(3, intColumn.subColumn("Bob", new int[]{0, 2, 4}).size());
        Assert.assertEquals(3, dblColumn.subColumn("Bob", new int[]{0, 2, 4}).size());
    }

    @Test
    public void getString() {
        Assert.assertEquals("M", strColumn.getString(1));
        Assert.assertEquals("97", intColumn.getString(1));
        Assert.assertEquals("17.44", dblColumn.getString(1));
    }

    @Test
    public void appendString() {
        strColumn.appendString("Bob", new StringParser());
        intColumn.appendString("7", new IntParser());
        dblColumn.appendString("5.4", new DoubleParser());
        Assert.assertEquals(68, strColumn.size());
        Assert.assertEquals(68, intColumn.size());
        Assert.assertEquals(68, dblColumn.size());
    }

    @Test
    public void getObject() {
        Assert.assertEquals("M", strColumn.getObject(0));
        Assert.assertEquals(18.14, dblColumn.getObject(0), 1e-16);
        Assert.assertEquals(72, (int)intColumn.getObject(0));
    }

    @Test
    public void heading() {
        Assert.assertEquals(new Heading("Sex"), strColumn.getHeading());
        Assert.assertEquals(new Heading("Weight (Sep)"), intColumn.getHeading());
        Assert.assertEquals(new Heading("BMI (Apr)"), dblColumn.getHeading());
    }

    @Test
    public void getIndex() {
        Assert.assertNotNull(strColumn.getIndex());
        Assert.assertNotNull(intColumn.getIndex());
        Assert.assertNotNull(dblColumn.getIndex());
    }

    @Test
    public void firstValue() {
        Assert.assertEquals(strColumn.firstValue(), strColumn.getObject(0));
        Assert.assertEquals(dblColumn.firstValue(), dblColumn.getObject(0));
        Assert.assertEquals(intColumn.firstValue(), intColumn.getObject(0));
    }

    @Test
    public void appendAll() {
        strColumn.appendAll(Arrays.asList("1", "2", "3", "4"));
        intColumn.appendAll(Arrays.asList(1, 2, 3, 4));
        dblColumn.appendAll(Arrays.asList(0.1, 0.2, 0.3, 0.4));
    }

    @Test
    public void newDataContainer() {
        Assert.assertNotNull(strColumn.newDataContainer(10));
        Assert.assertNotNull(dblColumn.newDataContainer(10));
        Assert.assertNotNull(intColumn.newDataContainer(10));
    }

    @Test
    public void equals() {
        Assert.assertEquals(strColumn, strColumn);
        Assert.assertEquals(dblColumn, dblColumn);
        Assert.assertEquals(intColumn, intColumn);
    }

    @Test
    public void createEmpty() {
        Assert.assertNotNull(strColumn.createEmpty());
        Assert.assertNotNull(dblColumn.createEmpty());
        Assert.assertNotNull(intColumn.createEmpty());
    }

}