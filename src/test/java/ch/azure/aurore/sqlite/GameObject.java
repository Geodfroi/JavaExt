package ch.azure.aurore.sqlite;

import ch.azure.aurore.sqlite.wrapper.annotations.DatabaseClass;
import ch.azure.aurore.sqlite.wrapper.annotations.PrimaryKey;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@DatabaseClass
public class GameObject {
    @PrimaryKey
    private int _id;
    private String name;
    private boolean _modified = true;
    private double[] pos = new double[0];

    private byte[] image;

    private Transform tr = new Transform();

    private List<String> tags = new ArrayList<>();

    private List<Enemy> enemies = new ArrayList<>();

    private Attack[] attacks;

    public Attack[] getAttacks() {
        if (attacks == null)
            return null;

        return Arrays.copyOf(attacks, attacks.length);
    }

    public byte[] getImage() {
        return image;
    }

    public List<Enemy> getEnemies() {
        return enemies;
    }

    public void setImage(byte[] image) {
        this.image = image;
        _modified = true;
    }

    public void setAttacks(Attack[] attacks) {
        this.attacks = attacks;
        _modified = true;
    }

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
        _modified = true;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
        _modified = true;
    }

    public World getWorld() {
        return world;
    }

    public void setWorld(World world) {
        this.world = world;
        _modified = true;
    }

    public boolean isModified() {
        return _modified;
    }
}