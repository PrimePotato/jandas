package io.github.primepotato.jandas.header;

import java.util.*;
import java.util.stream.Collectors;

public class Header {

    public List<HeaderKey> columnPositions = new ArrayList<>();
    public int level = 0;

    public Header(String... headers) {
        for (String h : headers) {
            addKey(h);
        }
    }

    public Header(List<HeaderKey> columnPositions) {
        this.columnPositions = columnPositions;
    }

    public void addKey(String... key) {
        addKey(columnPositions.size(), key);
    }

    public void addKey(int position, String... key) {
        addKey(position, new HeaderKey(key));
    }

    public void addKey(int position, HeaderKey key) {
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
        return columnPositions.indexOf(new HeaderKey(colName));
    }

    public int indexOf(HeaderKey hk){
        return columnPositions.indexOf(hk);
    }

    public int getLevel() {
        return columnPositions.toArray(new HeaderKey[0])[0].level;
    }

    public boolean wellFormed() {
        Set levels = columnPositions.stream().map(x -> x.level).collect(Collectors.toSet());
        return levels.size() <= 1;
    }

    public List<String> toList(){
        return columnPositions.stream().map(HeaderKey::toString).collect(Collectors.toList());
    }

}
