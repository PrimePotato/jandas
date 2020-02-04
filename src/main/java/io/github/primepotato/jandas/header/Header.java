package io.github.primepotato.jandas.header;

import java.util.*;
import java.util.stream.Collectors;

public class Header{

    public List<Heading> columnPositions = new ArrayList<>();
    public int level = 0;

    public Header(String... headings) {
        for (String h : headings) {
            addKey(h);
        }
    }

    public Header(List<Heading> columnPositions) {
        this.columnPositions = columnPositions;
    }

    public void addKey(String... key) {
        addKey(columnPositions.size(), key);
    }

    public void addKey(Heading hdr) {
        addKey(columnPositions.size(), hdr);
    }

    public void addKey(int position, String... key) {
        addKey(position, new Heading(key));
    }

    public void addKey(int position, Heading key) {
        if (level == 0) {
            level = key.level;
        } else {
            if (level != key.level) {
                throw new RuntimeException("Invalid Key " + key.toString() + " not same level as header");
            }
        }
        columnPositions.add(position, key);
    }

    public int indexOf(String... colName){
        return columnPositions.indexOf(new Heading(colName));
    }

    public int indexOf(Heading hk){
        return columnPositions.indexOf(hk);
    }

    public int getLevel() {
        return columnPositions.toArray(new Heading[0])[0].level;
    }

    public boolean wellFormed() {
        Set levels = columnPositions.stream().map(x -> x.level).collect(Collectors.toSet());
        return levels.size() <= 1;
    }

    public List<String> toList(){
        return columnPositions.stream().map(Heading::toString).collect(Collectors.toList());
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
