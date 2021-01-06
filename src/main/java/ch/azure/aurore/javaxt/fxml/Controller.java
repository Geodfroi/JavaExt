package ch.azure.aurore.javaxt.fxml;

import javafx.fxml.Initializable;
import javafx.scene.Scene;

public abstract class Controller implements Initializable {

    private boolean firstRun = true;
    private Scene scene;

    public void run() {
        if (firstRun)
            start();
        else
            resume();
        firstRun = false;
    }

    protected abstract void resume();

    protected abstract void start();

    public abstract void quit();

    public Scene getScene() {
        return scene;
    }

    public void setScene(Scene scene) {
        this.scene = scene;
    }

    public void pause() {
    }
}
