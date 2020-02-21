package io.github.primepotato.jandas.dataframe;

public class ColumnPosition<T>  {
    public int pos;
    public T key;

    public ColumnPosition(T key, int pos) {
        this.key = key;
        this.pos = pos;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof ColumnPosition)){
            return false;
        }
        if ((key == ((ColumnPosition)o).key) && (pos == ((ColumnPosition)o).pos)){
            return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
