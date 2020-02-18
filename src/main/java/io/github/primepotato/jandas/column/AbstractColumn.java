package io.github.primepotato.jandas.column;

import io.github.primepotato.jandas.header.Heading;
import io.github.primepotato.jandas.index.ColIndex;

import java.text.Normalizer;
import java.util.Set;
import java.util.stream.Collectors;
//import org.apache.commons.lang.ClassUtils;

@SuppressWarnings("unchecked")
public abstract class AbstractColumn<T> implements Column<T> {

    public ColIndex index;
    public Heading heading;
    public Class<?> dataType;
    public Boolean indexed;

    public abstract void rebuildIndex();

    public boolean unique() {
        return index().unique();
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    public Set<?> uniqueSet() {

        return index().positions()
                .values()
                .stream()
                .map(x -> getObject(x.getInt(0)))
                .collect(Collectors.toSet());
    }

    public Heading heading() {

        return heading;
    }

    public String cleanName() {

        return Normalizer.normalize(heading.toString(), Normalizer.Form.NFD);
    }

    public ColIndex index() {
        if (index == null) {
            rebuildIndex();
            return index();
        } else {
            return index;
        }
    }

    public T firstValue() {

        int idx = 0;
        T t = null;
        while (t == null) {
            t = (T)getObject(idx);
            idx++;
        }
        return t;
    }

    public String name() {
        return cleanName();
    }

    @Override
    public String toString(){
        return name() + rawData().toString();
    }

}
