package org.neo4j.maps;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.rules.TemporaryFolder;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Result;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseSettings;
import org.neo4j.kernel.configuration.Settings;
import org.neo4j.kernel.impl.enterprise.configuration.OnlineBackupSettings;
import org.neo4j.kernel.impl.proc.JarBuilder;
import org.neo4j.test.TestGraphDatabaseFactory;

import java.io.IOException;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertFalse;
import static org.neo4j.helpers.collection.MapUtil.map;

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

    @Rule
    public TemporaryFolder plugins = new TemporaryFolder();

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void shouldTestInsert()
    {
        // Given
        try ( Transaction tx = db.beginTx() )
        {
            db.execute("CREATE (w:Wizard{name:'Harry', lastname:'Potter'})\nRETURN w");
            db.execute("MATCH (w:Wizard { name: 'Harry' })\n" +
                    "SET w.lastname={l:'Potta'}");
            tx.success();
        }

        try (Transaction tx = db.beginTx())
        {
            tx.success();
        }

    }
}
