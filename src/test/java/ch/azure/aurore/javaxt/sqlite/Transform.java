package ch.azure.aurore.javaxt.sqlite;

public class Transform {

    private int posX;
    private int posY;

    public void setPosX(int posX) {
        this.posX = posX;
    }

    public void setPosY(int posY) {
        this.posY = posY;
    }

    public int getPosX() {
        return posX;
    }

    public int getPosY() {
        return posY;
    }

    @Override
    public String toString() {
        return "Transform{" +
                ", posX=" + posX +
                ", posY=" + posY +
                '}';
    }
}
