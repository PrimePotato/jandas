package io.github.primepotato.jandas.column;

import io.github.primepotato.jandas.column.impl.ObjectColumn;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;

public class ColumnTest {

    @Before
    public void setUp() {

    }

    @Test
    public void objectColumn() {
        String[] strData = {"a", "b", "c", "d"};

        ObjectColumn<String> sc = new ObjectColumn<>("1", false, strData, String.class);

        LocalDate[] dateData = {LocalDate.of(2019, 2, 1),
                LocalDate.of(2019, 2, 1),
                LocalDate.of(2019, 2, 1),
                LocalDate.of(2019, 2, 1)};

        ObjectColumn<LocalDate> dc = new ObjectColumn<>("1", false, dateData, LocalDate.class);

    }


}