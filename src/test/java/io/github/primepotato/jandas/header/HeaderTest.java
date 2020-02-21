package io.github.primepotato.jandas.header;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;

public class HeaderTest {

    private Header header;

    @Before
    public void init() {

        header = new Header();
        String[] k1 = {"ABC", "ASEF"};
        String[] k2 = {"ABC", "AsdfgSEF"};
        String[] k3 = {"AsdfgsdBC", "ABC"};
        header.add(k1);
        header.add(k2);
        header.add(k3);

    }

    @Test
    public void equals() {
        Heading h = new Heading("ABC");
        for (Heading hk : header) {
            if (hk.equals(h)) {
                System.out.println(Arrays.toString(hk.keys));
            }
        }
    }

    @Test
    public void add() {
        header.add(new Heading("abc"));
    }

    @Test
    public void add1() {
        header.add("abc");
    }

    @Test
    public void add2() {
        header.add(0, new Heading("abc", "1"));
    }

    @Test
    public void add3() {
        header.add("1", "2", "3");
    }

    @Test
    public void getLevel() {
        header.getLevel();
    }

    @Test
    public void wellFormed() {
        header.wellFormed();
    }

    @Test
    public void toStringArray() {
        header.toStringArray();
    }
}