package ch.azure.aurore.javaxt.sqlite.wrapper;

import ch.azure.aurore.javaxt.sqlite.*;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;

class SQLiteTest {

    private static final String DATABASE_PATH = "roster.sqlite";
    private static SQLite sqlite;

    @BeforeAll
    static void beforeAll() {
        sqlite = SQLite.connect(DATABASE_PATH);
    }

    @AfterAll
    static void afterAll() {
        if (sqlite != null)
            sqlite.close();
        //Disk.removeFile(DATABASE_PATH);
    }

    @Test
    void insert_simple() {
        GameObject gameObject = new GameObject();
        gameObject.setName("Hero");
        gameObject.getTr().setPosX(24);
        gameObject.getTr().setPosY(14);
        gameObject.getTags().add("slayer");
        gameObject.getTags().add("tourist");
        gameObject.setPos(new double[]{2.3, 4.5});

        assert sqlite.updateItem(gameObject);
    }

    @Test
    void insert_plural() {
        GameObject hero = new GameObject();
        hero.setName("Hero");
        GameObject pirate = new GameObject();
        pirate.setName("pirate");

        assert sqlite.updateItems(hero, pirate);
    }

    @Test
    void insert_one_to_one() {
        GameObject protagonist = new GameObject();
        protagonist.setName("Fred");
        World world = new World();
        world.setName("Fantasia");
        protagonist.setWorld(world);

        sqlite.updateItem(protagonist);
        assert world.get_id() != 0;
    }

    @Test
    void query_simple() {
        GameObject hero = new GameObject();
        hero.setName("Hero");
        hero.getTr().setPosX(24);
        hero.getTr().setPosY(14);
        hero.getTags().add("slayer");
        hero.getTags().add("tourist");
        assert sqlite.updateItem(hero);
        int id = hero.get_id();
        sqlite.clearMemory();

        GameObject q = sqlite.queryItem(GameObject.class, id);
        Assertions.assertEquals(24, q.getTr().getPosX());
        Assertions.assertEquals("tourist", q.getTags().get(1));
    }

    @Test
    void query_one_to_one() {
        GameObject adventurer = new GameObject();
        adventurer.setName("Adventurer");

        World w = new World();
        w.setName("Fantasia");
        adventurer.setWorld(w);

        sqlite.updateItem(w);

        assert sqlite.updateItem(adventurer);
        int id = adventurer.get_id();

        GameObject i = sqlite.queryItem(GameObject.class, id);
        assert i.getWorld() == w;
    }

    @Test
    void query_one_to_one_circular() {
        GameObject bilbo = new GameObject();
        bilbo.setName("Bilbo");

        World w = new World();
        w.setName("Middle-Earth");
        bilbo.setWorld(w);
        w.setProtagonist(bilbo);

        assert sqlite.updateItem(bilbo);
        int id = bilbo.get_id();

        GameObject i = sqlite.queryItem(GameObject.class, id);
        assert i.getWorld() == w;
    }

    @Test
    void query_one_to_one_read_from_db() {
        GameObject adventurer = new GameObject();
        adventurer.setName("Adventurer");

        World w = new World();
        w.setName("Catacombs");
        adventurer.setWorld(w);
        sqlite.updateItem(w);
        sqlite.clearMemory();

        assert sqlite.updateItem(adventurer);
        int id = adventurer.get_id();
        GameObject i = sqlite.queryItem(GameObject.class, id);

        assert i.getWorld().get_id() == w.get_id();
    }

    @Test
    void query_one_to_many_circular() {
        GameObject knight = new GameObject();
        knight.setName("Knight");
        World world = new World();
        world.setProtagonist(knight);
        world.setName("fantasia");
        knight.setWorld(world);

        knight.getEnemies().add(new Enemy("bandit"));
        Enemy dragon = new Enemy("dragon");
        dragon.setWorld(world);
        knight.getEnemies().add(dragon);

        assert sqlite.updateItem(knight);
        int knightID = knight.get_id();

        sqlite.clearMemory();
        GameObject query = sqlite.queryItem(GameObject.class, knightID);
        assert query.getEnemies().get(1).getWorld() != null;
    }

    @Test
    void query_many_to_many_array_prior_insert() {
        GameObject knight = new GameObject();
        knight.setName("Knight");

        Attack slash = new Attack("Slash", 10);
        sqlite.updateItem(slash);

        knight.setAttacks(new Attack[]{slash});
        assert sqlite.updateItem(knight);
        int id = knight.get_id();
        sqlite.clearMemory();

        GameObject query = sqlite.queryItem(GameObject.class, id);
        assert slash.get_id() == query.getAttacks()[0].get_id();
    }

    @Test
    void delete_simple() {
        GameObject wizard = new GameObject();
        wizard.setName("wizard");

        assert sqlite.updateItem(wizard);
        int id = wizard.get_id();

        assert sqlite.removeItem(wizard);

        GameObject q = sqlite.queryItem(GameObject.class, id);
        assert q == null;
    }

    @Test
    void a() {
        World world = new World();
        world.setName("village");
        world.setTime(14555);
        world.getTags().add("camp");

        sqlite.updateItem(world);
        sqlite.clearMemory();

        World q = sqlite.queryItem(World.class, world.get_id());
        assert q.getTags().size() > 0;
    }

    @Test
    void b() {
        Item a = new Item("a");
        Item b = new Item("b");
        Item c = new Item("c");

        new Link(a, b);
        new Link(a, c);

        sqlite.updateItem(a);
    }

    @Test
    void map_primitives() {
        AccountTable r = new AccountTable();

        r.getAccount().put(25, true);
        r.getAccount().put(65, false);

        sqlite.updateItem(r);
        sqlite.clearMemory();

        AccountTable q = sqlite.queryItem(AccountTable.class, r.get_id());
        assert q.getAccount().get(25);
    }

    @Test
    void map_objects() {
        Register r = new Register();
        r.getEntries().put(2345, new Employee(1526, "Homer", "a", "b"));
        sqlite.updateItem(r);
        sqlite.clearMemory();

        List<Register> q = sqlite.queryItems(Register.class);
        assert q.size() > 0;
    }

    @Test
    void map_ref() {
        Weapon w = new Weapon();
        Attack attack1 = new Attack("Slash", 45);
        Attack attack2 = new Attack("Throw", 60);
        w.getAttacks().put(0, attack1);
        w.getAttacks().put(1, attack2);

        sqlite.updateItem(w);
        sqlite.clearMemory();

        List<Weapon> f = sqlite.queryItems(Weapon.class);
        assert f != null;
    }

    @Test
    void insert_integerList() {
        IDCollection ids = new IDCollection();
        ids.getIdList().add(17);
        ids.getIdList().add(23);
        ids.getIdList().add(29);

        sqlite.updateItem(ids);
        sqlite.clearMemory();

        IDCollection q = sqlite.queryItem(IDCollection.class, ids.get_id());
        assert q.getIdList().size() == 3;
    }

    @Test
    void query_multiple() {
        Party party = new Party();

        World world = new World();
        world.setName("village");
        world.setTime(14555);

        GameObject wizard = new GameObject();
        wizard.setName("wizard");
        GameObject priest = new GameObject();
        priest.setName("priest");
        priest.setWorld(world);
        GameObject warrior = new GameObject();
        warrior.setName("warrior");
        warrior.setWorld(world);

        party.getMembers().add(wizard);
        party.getMembers().add(priest);
        party.getMembers().add(warrior);

        assert sqlite.updateItem(party);
        sqlite.clearMemory();

        List<GameObject> r = sqlite.queryItems(GameObject.class);
        assert r.size() > 0;
    }
}