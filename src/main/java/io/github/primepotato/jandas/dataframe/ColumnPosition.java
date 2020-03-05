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
        return (key == ((ColumnPosition) o).key) && (pos == ((ColumnPosition) o).pos);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
