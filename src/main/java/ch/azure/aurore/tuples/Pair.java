package ch.azure.aurore.tuples;

import java.util.Objects;

public class Pair<T1, T2> {

    private final T1 o1;
    private final T2 o2;

    public Pair(T1 o1, T2 o2) {
        this.o1 = o1;
        this.o2 = o2;
        if (o1 == null || o2 == null)
            throw new IllegalStateException();
    }

    public T1 getVal0(){
        return o1;
    }

    public T2 getVal1(){
        return o2;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pair<?, ?> pair = (Pair<?, ?>) o;
        return Objects.equals(o1, pair.o1) && Objects.equals(o2, pair.o2);
    }

    @Override
    public int hashCode() {
        return Objects.hash(o1, o2);
    }
}
