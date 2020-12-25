package ch.azure.aurore.javaxt.fxml;

import ch.azure.aurore.javaxt.IO.API.Disk;
import ch.azure.aurore.javaxt.json.API.JSON;
import ch.azure.aurore.javaxt.strings.Strings;

import java.nio.file.Path;

public abstract class AppState {

    static final Path path = Path.of("local.json");
    private static Class<?> stateClass;

    private double[] windowSize;
    private boolean initialised; //<- block calls to modified() when Jackson create the obj from string

    static AppState createInstance(Class<? extends AppState> clazz) {
        String str = Disk.readFile(path);
        AppState item;
        if (Strings.isNullOrEmpty(str)) {
            try {
                item = clazz.getDeclaredConstructor().newInstance();
            } catch (ReflectiveOperationException e) {
                e.printStackTrace();
                throw new IllegalStateException("Failed to instantiate [" + clazz.getSimpleName() + "] AppState");
            }
        }else
            item = JSON.readValue(clazz, str);

        if (item == null)
            throw new IllegalStateException("Error loading AppState");

        item.initialised = true;
        return item;
    }

    public void setWindowSize(double x, double y) {
        windowSize = new double[]{x, y};
    }

    public double[] getWindowSize() {
        return windowSize;
    }

    public void setWindowSize(double[] windowSize) {
        this.windowSize = windowSize;
        modified();
    }

    protected void modified() {
        if (!initialised)
            return;

        String str = JSON.toJSON(this);
        Disk.writeFile(path, str);
    }


}
