package ch.azure.aurore.javaxt.fxml;

import ch.azure.aurore.javaxt.IO.API.Disk;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("unused") //<- abstract methods used in other applications
public abstract class FXApplication extends Application {

    private static final String APP_NAME = "appName";
    private static final String APP_ICON = "file:icon.png";
    private static FXApplication instance;
    private final Map<String, Controller> scenes = new HashMap<>();
    protected Stage stage;
    private String current;
    private AppState state;
    private AppSetting settings;

    public static FXApplication getInstance() {
        return instance;
    }

    public void switchScene(String token) {
        if (!scenes.containsKey(token))
            scenes.put(token, loadScene(getLoader(token), token));

        boolean firstScene = current == null;
        if (!firstScene)
            scenes.get(current).pause();

        current = token;

        Controller controller = scenes.get(token);
        stage.setScene(controller.getScene());
        controller.run();
        if (firstScene)
            stage.show();
    }

    protected abstract FXMLLoader getLoader(String token);

    @Override
    public void init() throws Exception {
        super.init();
    }

    public AppState getState() {
        return state;
    }

    public AppSetting getSettings() {
        return settings;
    }

    @Override
    public final void start(Stage stage) {
        instance = this;
        this.stage = stage;
        state = AppState.createInstance(getStateType());
        settings = AppSetting.createInstance(getSettingType());
        stage.setTitle(settings.getAppName());
        //stage.setTitle(Settings.getInstance().getString(APP_NAME));

        double[] size = getSize();
        stage.setWidth(size[0]);
        stage.setHeight(size[1]);

        stage.getIcons().add(new Image(APP_ICON));

        stage.widthProperty().addListener((observableValue, number, t1) ->
                state.setWindowSize(t1.doubleValue(), stage.getHeight()));
        stage.heightProperty().addListener((observableValue, number, t1) ->
                state.setWindowSize(stage.getWidth(), t1.doubleValue()));

        this.start();
    }

    protected abstract void start();

    protected Controller loadScene(FXMLLoader loader, String token) {
        Scene scene;
        try {
            scene = new Scene(loader.load());
        } catch (IOException e) {
            e.printStackTrace();
            throw new IllegalStateException("Failed to load [" + token + "] scene");
        }
        Controller c = loader.getController();
        c.setScene(scene);
        return c;
    }

    private double[] getSize() {
        double[] size = state.getWindowSize();
        if (size == null || size.length != 2) {
             size = settings.getWindowSize();
           // List<Double> s = Settings.getInstance().getDoubles("windowSize");
           // return new double[]{s.get(0), s.get(1)};
        }
        return size;
    }

    public Stage getStage() {
        return stage;
    }

    public void clearState() {
        state = AppState.createInstance(getStateType());
        Disk.writeFile(AppState.path, "");
    }

    protected Class<? extends AppSetting> getSettingType(){
        return AppSetting.class;
    }

    @Override
    public final void stop() throws Exception {
        state.record();
        for (Controller c : scenes.values())
            c.quit();
        quit();
        super.stop();
    }

    protected abstract void quit();

    protected abstract Class<? extends AppState> getStateType();
}
