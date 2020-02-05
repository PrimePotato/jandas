package io.github.primepotato.jandas.header;


import java.util.*;
import java.util.stream.Collectors;


public class Header extends ArrayList<HeaderKey>{

    private int level = 0;

    public Header(String... headers) {
        for (String h : headers) {
            add(new HeaderKey(h));
        }
    }

    public void add(String key){
        this.add(new HeaderKey(key));
    }

    public void add(String... key){
        this.add(new HeaderKey(key));
    }

    @Override
    public boolean add(HeaderKey key){
        try {
            errorCheck(key);
        } catch (RuntimeException e) {
            return false;
        }
        super.add(key);
        return true;
    }

    @Override
    public void add(int position, HeaderKey key){
        errorCheck(key);
        super.add(position, key);
    }

    private void errorCheck(HeaderKey key){
        if (level == 0) {
            level = key.level;
        } else {
            if (level != key.level) {
                throw new RuntimeException("Invalid Key " + key.toString() + " not same level as header");
            }
        }
    }
//
//    public void addKey(String... key) {
//        addKey(this.size(), key);
//    }
//
//    public void addKey(int position, String... key) {
//        addKey(position, new HeaderKey(key));
//    }
//
//    public void addKey(int position, HeaderKey key) {
//        if (level == 0) {
//            level = key.level;
//        } else {
//            if (level != key.level) {
//                throw new RuntimeException("Invalid Key " + key.toString() + " not same level as header");
//            \}
//        }
//        this.add(position, key);
//    }
//
//    public int indexOf(String... colName){
//        return columnPositions.indexOf(new HeaderKey(colName));
//    }
//
//    public int indexOf(HeaderKey hk){
//        return columnPositions.indexOf(hk);
//    }
//
    public int getLevel() {
        return this.toArray(new HeaderKey[0])[0].level;
    }

    public boolean wellFormed() {
        Set levels = this.stream().map(x -> x.level).collect(Collectors.toSet());
        return levels.size() <= 1;
    }

//    public List<String> toList(){
//        return columnPositions.stream().map(HeaderKey::toString).collect(Collectors.toList());
//    }

}
