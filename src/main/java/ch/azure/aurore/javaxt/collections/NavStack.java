package ch.azure.aurore.javaxt.collections;
import java.util.ArrayList;
import java.util.List;

/**
 * Allow navigation backward and forward between item. A move forward will reduce the list to the current index before adding the new element.
 * @param <T> item inside stack
 */
public class NavStack<T> {

    private List<T> list = new ArrayList<>();
    private int index = -1;

    public void add(T value) {

        //skip if it is the current item
        if (index != -1 && list.get(index) == value)
            return;

        //shorten list to current index
        if (hasNext())
            list = list.subList(0, index + 1);

        list.add(value);
        index = list.size()-1;
    }

    public boolean canNavigate(Directions direction){
        if (direction == Directions.backward){
            return hasFormer();
        }
        return hasNext();
    }

    public void clear() {
        list.clear();
        index =-1;
    }

    public boolean hasFormer() {
        return list.size() >=1 && index >0;
    }

    public boolean hasNext() {
        return index < list.size()-1;
    }

    public T navigateStack(Directions direction) {
        if (direction == Directions.backward){
            return toFormer();
        }
        else{
            return toNext();
        }
    }

    public T toNext(){
        if (hasNext()) {
            index++;
            return list.get(index);
        }
        return null;
    }
    public T toFormer() {
        if (hasFormer()){
            index--;
            return list.get(index);
        }
        return null;
    }
}