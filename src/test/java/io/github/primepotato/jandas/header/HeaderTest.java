package io.github.primepotato.jandas.header;

import org.junit.Test;

import java.util.Arrays;

public class HeaderTest {

    @Test
    public void init() {

        Header header = new Header();
        String[] k1 = {"ABC", "ASEF"};
        String[] k2 = {"ABC", "AsdfgSEF"};
        String[] k3 = {"AsdfgsdBC", "ABC"};
        header.addKey(k1);
        header.addKey(k2);
        header.addKey(k3);

        Heading h = new Heading("ABC");

        for (Heading hk : header.columnPositions) {
            if (hk.equals(h)) {
                System.out.println(Arrays.toString(hk.keys));
            }
        }


    }

    @Test
    public void addKey() {
    }

    @Test
    public void addKey1() {
    }

    @Test
    public void addKey2() {
    }

    @Test
    public void addKey3() {
    }

    @Test
    public void indexOf() {
    }

    @Test
    public void indexOf1() {
    }

    @Test
    public void getLevel() {
    }

    @Test
    public void wellFormed() {
    }

    @Test
    public void toList() {
    }
}