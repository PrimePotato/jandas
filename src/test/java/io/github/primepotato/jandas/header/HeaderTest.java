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
        header.addKey(k1);
        header.addKey(k2);
        header.addKey(k3);

        HeaderKey h = new HeaderKey("ABC");

        for (HeaderKey hk : header.columnPositions) {
            if (hk.equals(h)) {
                System.out.println(Arrays.toString(hk.key));
            }
        }


    }
}