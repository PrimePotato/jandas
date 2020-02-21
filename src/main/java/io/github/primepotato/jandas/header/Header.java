package io.github.primepotato.jandas.header;


import java.util.*;
import java.util.stream.Collectors;


public class Header extends ArrayList<Heading>{

    private int level = 0;

    public Header(String... headers) {
        for (String h : headers) {
            add(new Heading(h));
        }
    }

    public void add(String key){
        this.add(new Heading(key));
    }

    public void add(String... key){
        this.add(new Heading(key));
    }

    @Override
    public boolean add(Heading key){
        try {
            errorCheck(key);
        } catch (RuntimeException e) {
            return false;
        }
        super.add(key);
        return true;
    }


    @Override
    public void add(int position, Heading key){
        errorCheck(key);
        super.add(position, key);
    }

    private void errorCheck(Heading key){
        if (level == 0) {
            level = key.level;
        } else {
            if (level != key.level) {
                throw new RuntimeException("Invalid Key " + key.toString() + " not same level as header");
            }
        }
    }
    public int getLevel() {
        return this.toArray(new Heading[0])[0].level;
    }

    public boolean wellFormed() {
        Set levels = this.stream().map(x -> x.level).collect(Collectors.toSet());
        return levels.size() <= 1;
    }

    public String[] toStringArray(){
        String[] str = new String[this.size()];
        for (int i=0; i<this.size() ;i++){
            str[i] = this.get(i).toString();
        }
        return str;
    }
}
