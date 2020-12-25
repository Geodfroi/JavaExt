package ch.azure.aurore.javaxt.fxml;

import ch.azure.aurore.javaxt.IO.API.Disk;
import ch.azure.aurore.javaxt.IO.API.Settings;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

@SuppressWarnings("unused") //<- abstract methods used in other applications
public abstract class FXApplication extends Application {

    private static final String APP_NAME = "appName";
    private static final String APP_ICON = "file:icon.png";
    private static FXApplication instance;
    protected Scene scene;
    protected Stage stage;
    private IController controller;
    private AppState state;

    public static FXApplication getInstance() {
        return instance;
    }

    @Override
    public void init() throws Exception {
        super.init();
    }

    public AppState getState() {
        return state;
    }

    @Override
    public final void start(Stage stage) throws IOException {
        instance = this;
        this.stage = stage;
        state = AppState.createInstance(getStateType());

        stage.setTitle(Settings.getInstance().getString(APP_NAME));

        stage.getIcons().add(new Image(APP_ICON));

        double[] size = getSize();
        FXMLLoader fxmlLoader = getLoader();

        scene = new Scene(fxmlLoader.load(), size[0], size[1]); //getScene(fxmlLoader, size.get(0), size.get(1));
        stage.setScene(scene);

        controller = fxmlLoader.getController();
        controller.start();

        stage.show();
        stage.widthProperty().addListener((observableValue, number, t1) ->
                state.setWindowSize(t1.doubleValue(), stage.getHeight()));
        // LocalSave.getInstance().setDoubles(WINDOW_SIZE, t1.doubleValue(), stage.getHeight()));
        stage.heightProperty().addListener((observableValue, number, t1) ->
                state.setWindowSize(stage.getWidth(), t1.doubleValue()));
        //  LocalSave.getInstance().setDoubles(WINDOW_SIZE, stage.getWidth(), t1.doubleValue()));
    }

    protected abstract FXMLLoader getLoader();

    private double[] getSize() {
        double[] size = state.getWindowSize();
        if (size == null || size.length != 2) {
            List<Double> s = Settings.getInstance().getDoubles("windowSize");
            return new double[]{s.get(0), s.get(1)};
        }
        return size;
    }

    public Scene getScene() {
        return scene;
    }

    public Stage getStage() {
        return stage;
    }

    public void clearState() {
        state = AppState.createInstance(getStateType());
        Disk.writeFile(AppState.path, "");
    }

    @Override
    public void stop() throws Exception {
        controller.quit();
        super.stop();
    }

    protected abstract Class<? extends AppState> getStateType();
}
