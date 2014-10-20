package com.danikula.android.garden.content;

import com.danikula.android.garden.content.migration.*;
import com.google.common.collect.Lists;
import junit.framework.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.List;

import static org.fest.assertions.api.Assertions.assertThat;

/**
 * Tests {@link com.danikula.android.garden.content.migration.Migration}.
 *
 * @author Alexey Danilov (danikula@gmail.com).
 */
@Config(manifest = "test/src/main/AndroidManifest.xml")
@RunWith(RobolectricTestRunner.class)
public class MigrationTest {

    @Test
    public void testSingleLineMigration() throws Exception {
        List<String> statements = executeMigration("execute me!;");
        assertThat(statements).isEqualTo(Lists.newArrayList("execute me!;"));
    }

    @Test
    public void testMultiLineMigrations() throws Exception {
        List<String> statements = executeMigration("multiline\n\nstatement;\none\nmore;");
        List<String> expected = Lists.newArrayList("multiline statement;", "one more;");
        assertThat(statements).isEqualTo(expected);
    }

    @Test
    public void testTrimStatementsMigrations() throws Exception {
        List<String> statements = executeMigration("statements\n  \nwith\nempty \n    \nlines;\none more;");
        List<String> expected = Lists.newArrayList("statements with empty lines;", "one more;");
        assertThat(statements).isEqualTo(expected);
    }

    @Test
    public void testAssetsMigrationsSource() throws Exception {
        MigrationSource source = new AssetsMigrationSource(Robolectric.application.getResources(), 0, 1);
        SimpleMigrationExecutor executor = new SimpleMigrationExecutor();
        new Migration(source, executor).execute();
        List<String> expected = Lists.newArrayList("execute me, baby!;", "one more time...;", "wow!;");
        assertThat(executor.getStatements()).isEqualTo(expected);
    }

    @Test(expected = MigrationException.class)
    public void testNotExistedAssetsMigrationsSource() throws Exception {
        MigrationSource source = new AssetsMigrationSource(Robolectric.application.getResources(), -1, 0);
        new Migration(source, new SimpleMigrationExecutor()).execute();
        Assert.fail("not existed assets!");
    }

    @Test(expected = MigrationException.class)
    public void testValidationOfEndingsMigrations() throws Exception {
        MigrationSource source = new StringMigrationSource("correct;\n not correct statement without ending");
        new Migration(source, new SimpleMigrationExecutor()).execute();
        Assert.fail("last statement doesn't contain ';'!");
    }

    private List<String> executeMigration(String migrationStatements) throws MigrationException {
        MigrationSource source = new StringMigrationSource(migrationStatements);
        SimpleMigrationExecutor executor = new SimpleMigrationExecutor();
        new Migration(source, executor).execute();
        return executor.getStatements();
    }

}
