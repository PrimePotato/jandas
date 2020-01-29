package io.github.primepotato.jandas.header;

import java.util.Arrays;
import java.util.Objects;

public class Heading implements Comparable<Heading>{
    public int level;
    public String[] keys;

    public Heading(String... keys) {
        this.level = keys.length;
        this.keys = keys;
    }

    public Heading newKeyHeading(String k){
        String[] newKeys = new String[this.level+1];
        System.arraycopy(keys,0,newKeys,0,this.level);
        newKeys[this.level] = k;
        return new Heading(newKeys);
    }

    @Override
    public String toString() {
        return Arrays.toString(keys);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Heading heading = (Heading) o;
        int minLevel  = Math.min(heading.level, this.level);
        for (int i=0;i<minLevel;++i){
            if (!(heading.keys[i].equals(this.keys[i]))){
                return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(level);
        result = 31 * result + Arrays.hashCode(keys);
        return result;
    }

    @Override
    public int compareTo(Heading heading) {
        return String.CASE_INSENSITIVE_ORDER.compare(this.toString(), heading.toString());
    }
}