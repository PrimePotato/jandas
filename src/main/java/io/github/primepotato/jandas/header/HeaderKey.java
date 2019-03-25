package io.github.primepotato.jandas.header;

import java.util.Arrays;
import java.util.Objects;

public class HeaderKey {
    public int level;
    public String[] key;

    HeaderKey(String... key) {
        this.level = key.length;
        this.key = key;
    }

    @Override
    public String toString() {
        return Arrays.toString(key);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HeaderKey headerKey = (HeaderKey) o;
        int minLevel  = Math.min(headerKey.level, this.level);
        for (int i=0;i<minLevel;++i){
            if (!(headerKey.key[i].equals(this.key[i]))){
                return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(level);
        result = 31 * result + Arrays.hashCode(key);
        return result;
    }
}