package io.github.primepotato.jandas.header;

import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;

public class HeaderTest {

    @Test
    public void init() {

        Header header = new Header();
        String[] k1 = {"ABC", "ASEF"};
        String[] k2 = {"ABC", "AsdfgSEF"};
        String[] k3 = {"AsdfgsdBC", "ABC"};
        header.add(k1);
        header.add(k2);
        header.add(k3);

        Heading h = new Heading("ABC");

        for (Heading hk : header) {
            if (hk.equals(h)) {
                System.out.println(Arrays.toString(hk.keys));
            }
        }


    }
}