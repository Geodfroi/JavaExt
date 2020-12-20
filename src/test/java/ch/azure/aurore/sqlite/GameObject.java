package ch.azure.aurore.sqlite;

import ch.azure.aurore.sqlite.wrapper.annotations.DatabaseClass;
import ch.azure.aurore.sqlite.wrapper.annotations.PrimaryKey;

import java.util.ArrayList;
import java.util.List;

@DatabaseClass
public class GameObject {
    @PrimaryKey
    private int _id;
    private String name;

    private Transform tr = new Transform();

    private List<String> tags = new ArrayList<>();

    private List<Enemy> enemies = new ArrayList<>();

    private Attack[] attacks;

    public Attack[] getAttacks() {
        return attacks;
    }

    public void setAttacks(Attack[] attacks) {
        this.attacks = attacks;
    }

    public List<Enemy> getEnemies() {
        return enemies;
    }

    public void setEnemies(List<Enemy> enemies) {
        this.enemies = enemies;
    }

    private double[] pos = new double[0];

    public double[] getPos() {
        return pos;
    }

    public void setPos(double[] pos) {
        this.pos = pos;
    }

    private World world;

    public int get_id() {
        return _id;
    }

    public Transform getTr() {
        return tr;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTr(Transform tr) {
        this.tr = tr;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public World getWorld() {
        return world;
    }

    public void setWorld(World world) {
        this.world = world;
    }
}