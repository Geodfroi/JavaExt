package ch.azure.aurore.tuples;

public class KeyValuePair<T1, T2> extends Pair<T1,T2> {

    public KeyValuePair(T1 o1, T2 o2){
        super(o1, o2);
    }

    public T1 getKey() {
        return getVal0();
    }

    public T2 getValue() {
        return getVal1();
    }
}
