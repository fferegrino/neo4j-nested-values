package org.neo4j.maps;

import org.junit.*;
import org.junit.rules.ExpectedException;
import org.junit.rules.TemporaryFolder;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Result;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseSettings;
import org.neo4j.kernel.configuration.Settings;
import org.neo4j.kernel.impl.core.NodeProxy;
import org.neo4j.kernel.impl.enterprise.configuration.OnlineBackupSettings;
import org.neo4j.test.TestGraphDatabaseFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class InsertMapValueTest
{

    private GraphDatabaseService db;

    @Before
    public void setUp() throws IOException
    {
        db = new TestGraphDatabaseFactory()
                .newImpermanentDatabaseBuilder()
                .setConfig( GraphDatabaseSettings.plugin_dir, plugins.getRoot().getAbsolutePath() )
                .setConfig( OnlineBackupSettings.online_backup_enabled, Settings.FALSE )
                .newGraphDatabase();

    }

    @After
    public void tearDown()
    {
        if ( this.db != null )
        {
            this.db.shutdown();
        }
    }

    public static HashMap<String, Object> createHashMap(Object... keyOrVal)
    {
        HashMap<String, Object> retVal = new HashMap<>();

        assert keyOrVal.length % 2 == 0;
        for (int i = 0; i < keyOrVal.length; i += 2) {
            String key = (String) keyOrVal[i];
            Object value = keyOrVal[i + 1];
            retVal.put(key, value);
        }

        return retVal;
    }

    @Rule
    public TemporaryFolder plugins = new TemporaryFolder();

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void shouldTestInsertStrings()
    {
        Transaction tx = db.beginTx();

        db.execute("CREATE (w:Book{" +
                "title:'Harry Potter y la Piedra Filosofal', " +
                "author:{name:'Joanne', lastname:'Rowling'}, " +
                "array: [10,20,30,40,50,60]" +
                "})\n" +
                "RETURN w");

        HashMap<String, Object> expected = createHashMap(
                "name", "Joanne",
                "lastname", "Rowling");

        Result result = db.execute("MATCH (w:Book {title: 'Harry Potter y la Piedra Filosofal'})\n" +
                "RETURN w");

        if (result.hasNext())
        {
            Map<String, Object> firstRow = result.next();
            NodeProxy node = (NodeProxy) firstRow.get("w");
            Object property = node.getProperty("author");
            assertTrue("Not an instance of HashMap", property instanceof HashMap);
            HashMap<String, Object> map = (HashMap<String, Object>) property;
            assertThat(expected,equalTo(map));
        }
        tx.success();
        tx.close();
    }

    @Test
    public void shouldTestInsertInts()
    {
        Transaction tx = db.beginTx();
        db.execute("CREATE (p:Person{" +
                "dob:{year:1991, month:12, day:6} " +
                "})\n" +
                "RETURN p");

        HashMap<String, Object> expected = createHashMap(
                "year", 1991l,
                "month", 12l,
                "day", 6l);

        Result result = db.execute("MATCH (p:Person)\n" +
                "RETURN p");

        if (result.hasNext())
        {
            Map<String, Object> firstRow = result.next();
            NodeProxy node = (NodeProxy) firstRow.get("p");
            Object property = node.getProperty("dob");
            assertTrue("Not an instance of HashMap", property instanceof HashMap);
            HashMap<String, Object> map = (HashMap<String, Object>) property;
            assertTrue(map.equals(expected));
        }
        tx.success();
        tx.close();
    }

    @Test
    public void shouldTestInsertNullValues()
    {


        Transaction tx = db.beginTx();
        db.execute("CREATE (p:Person{" +
                "this_contains_nulls:{n1:null, n2:null, n3:null} " +
                "})\n" +
                "RETURN p");

        HashMap<String, Object> expected = createHashMap(
                "n1", null,
                "n2", null,
                "n3", null);

        Result result = db.execute("MATCH (p:Person)\n" +
                "RETURN p");

        if (result.hasNext())
        {
            Map<String, Object> firstRow = result.next();
            NodeProxy node = (NodeProxy) firstRow.get("p");
            Object property = node.getProperty("this_contains_nulls");
            assertTrue("Not an instance of HashMap", property instanceof HashMap);
            HashMap<String, Object> map = (HashMap<String, Object>) property;
            assertTrue(map.equals(expected));
        }
        tx.success();
        tx.close();
    }

    @Test
    public void shouldTestInsertMixedValues()
    {
        Transaction tx = db.beginTx();
        db.execute("CREATE (p:Person{" +
                "mixed:{n1:'Neo4j', n2:true, n3:1.5} " +
                "})\n" +
                "RETURN p");

        HashMap<String, Object> expected = createHashMap(
                "n1", "Neo4j",
                "n2", true,
                "n3", 1.5);

        Result result = db.execute("MATCH (p:Person)\n" +
                "RETURN p");

        if (result.hasNext())
        {
            Map<String, Object> firstRow = result.next();
            NodeProxy node = (NodeProxy) firstRow.get("p");
            Object property = node.getProperty("mixed");
            assertTrue("Not an instance of HashMap", property instanceof HashMap);
            HashMap<String, Object> map = (HashMap<String, Object>) property;
            assertTrue(map.equals(expected));
        }
        tx.success();
        tx.close();
    }

    @Test
    @Ignore("Test takes too long to run, not sure why")
    public void shouldTestInsertNested()
    {
        try(Transaction tx = db.beginTx())
        {
            db.execute("CREATE (p:Person{" +
                    "nested:{n1:'Neo4j', n2:true, n3:1.5, inner:{i:'familymart'}} " +
                    "})\n" +
                    "RETURN p");

            HashMap<String, Object> expected = createHashMap(
                    "n1", "Neo4j",
                    "n2", true,
                    "n3", 1.5,
                    "inner", createHashMap("i", "familymart"));

            Result result = db.execute("MATCH (p:Person)\n" +
                    "RETURN p");

            if (result.hasNext()) {
                Map<String, Object> firstRow = result.next();
                NodeProxy node = (NodeProxy) firstRow.get("p");
                Object property = node.getProperty("nested");
                HashMap<String, Object> map = (HashMap<String, Object>) property;
                assertEquals(expected, map);
            }
            tx.success();
        }
    }
}
