package ch.azure.aurore.javaxt.sqlite;

import ch.azure.aurore.javaxt.sqlite.wrapper.SQLiteData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SuppressWarnings("unused")

public class GameObject extends SQLiteData {

    private String name;
    private double[] pos = new double[0];

    private Transform tr = new Transform();

    private List<String> tags = new ArrayList<>();

    private List<Enemy> enemies = new ArrayList<>();

    private Attack[] attacks;
    private World world;

    public Attack[] getAttacks() {
        if (attacks == null)
            return null;

        return Arrays.copyOf(attacks, attacks.length);
    }

    public void setAttacks(Attack[] attacks) {
        this.attacks = attacks;
        set_modified(true);
    }

    public List<Enemy> getEnemies() {
        return enemies;
    }

    public void setEnemies(List<Enemy> enemies) {
        this.enemies = enemies;
    }

    public double[] getPos() {
        return pos;
    }

    public void setPos(double[] pos) {
        this.pos = pos;
    }

    public Transform getTr() {
        return tr;
    }

    public void setTr(Transform tr) {
        this.tr = tr;
        set_modified(true);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
        set_modified(true);
    }

    public World getWorld() {
        return world;
    }

    public void setWorld(World world) {
        this.world = world;
        set_modified(true);
    }
}