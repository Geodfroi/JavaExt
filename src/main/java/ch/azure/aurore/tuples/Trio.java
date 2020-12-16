package ch.azure.aurore.tuples;

import java.util.Objects;

public class Trio<T1, T2, T3> {

    private final T1 o1;
    private final T2 o2;
    private final T3 o3;

    public Trio(T1 o1, T2 o2, T3 o3) {
        this.o1 = o1;
        this.o2 = o2;
        this.o3 = o3;
    }

    public T1 getVal0(){
        return o1;
    }

    public T2 getVal1(){
        return o2;
    }

    public T3 getVal2() {
        return o3;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Trio<?, ?, ?> trio = (Trio<?, ?, ?>) o;
        return Objects.equals(o1, trio.o1) && Objects.equals(o2, trio.o2) && Objects.equals(o3, trio.o3);
    }

    @Override
    public int hashCode() {
        return Objects.hash(o1, o2, o3);
    }
}