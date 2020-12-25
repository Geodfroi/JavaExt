//package ch.azure.aurore.javaxt.tuples;
//
//import com.fasterxml.jackson.annotation.JsonIgnore;
//import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
//
//public class KeyValuePair<T1, T2> extends Pair<T1,T2> {
//
//    public KeyValuePair(T1 o1, T2 o2){
//        super(o1, o2);
//    }
//
//    @JsonIgnore
//    public T1 getKey() {
//        return getVal0();
//    }
//
//    @JsonIgnore
//    public T2 getValue() {
//        return getVal1();
//    }
//}
