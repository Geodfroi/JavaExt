package ch.azure.aurore.fxml;

import ch.azure.aurore.IO.API.LocalSave;
import ch.azure.aurore.IO.API.Settings;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public abstract class FXApplication extends Application {

    private static final String WINDOW_SIZE = "windowSize";
    private static final String APP_NAME = "appName";
    private static final String APP_ICON = "file:icon.png";

    public static FXApplication getInstance() {
        return instance;
    }

    private static FXApplication instance;

    protected Scene scene;
    private IController controller;
    protected Stage stage;

    @Override
    public void init() throws Exception {
        super.init();
    }

    @Override
    public final void start(Stage stage) throws IOException {
        instance = this;
        this.stage = stage;
        stage.setTitle(Settings.getInstance().getString(APP_NAME));

        stage.getIcons().add(new Image(APP_ICON));

        List<Integer> size = getSize();
        FXMLLoader fxmlLoader = getLoader();

        scene =  new Scene(fxmlLoader.load(),size.get(0), size.get(1)); //getScene(fxmlLoader, size.get(0), size.get(1));
        stage.setScene(scene);

        controller = fxmlLoader.getController();
        controller.start();

        stage.show();
        stage.widthProperty().addListener((observableValue, number, t1) ->
                LocalSave.getInstance().setDoubles(WINDOW_SIZE, t1.doubleValue(), stage.getHeight()));
        stage.heightProperty().addListener((observableValue, number, t1) ->
                LocalSave.getInstance().setDoubles(WINDOW_SIZE, stage.getWidth(), t1.doubleValue()));
    }

    protected abstract FXMLLoader getLoader();

    private List<Integer> getSize() {
        Optional<List<Integer>> size = LocalSave.getInstance().getIntegers(WINDOW_SIZE);
        return size.orElseGet(() -> Settings.getInstance().getIntegers(WINDOW_SIZE));
    }

    public Scene getScene() {
        return scene;
    }

    public Stage getStage() {
        return stage;
    }

    @Override
    public void stop() throws Exception {
        controller.quit();
        super.stop();
    }
}
