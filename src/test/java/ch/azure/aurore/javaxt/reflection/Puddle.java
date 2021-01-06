package ch.azure.aurore.javaxt.reflection;


public class Puddle {
    private String name;

    public boolean isTrained() {
        return trained;
    }
    public Puddle(String name){
        this.name = name;
    }

    public void setTrained(boolean trained) {
        this.trained = trained;
    }

    boolean trained;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
