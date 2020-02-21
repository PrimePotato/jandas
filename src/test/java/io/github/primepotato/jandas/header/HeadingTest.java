package io.github.primepotato.jandas.header;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class HeadingTest {

    Heading heading;
    Heading heading2;

    @Before
    public void setUp(){
        heading = new Heading("1", "3", "4");
        heading2 = new Heading("1", "3", "5");
    }

    @Test
    public void newKeyHeading() {
        heading.newKeyHeading("2");
    }


    @Test
    public void equals() {
        heading.equals(heading);
        heading.equals(heading2);
    }


    @Test
    public void compareTo() {
        heading.compareTo(heading);
        heading.compareTo(heading2);
    }

    @Test
    public void tToString() {
        heading.toString();

    }

    @Test
    public void tHashCode() {
        heading.hashCode();
    }
}