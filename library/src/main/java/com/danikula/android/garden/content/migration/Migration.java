package com.danikula.android.garden.content.migration;

import android.text.TextUtils;

import com.danikula.android.garden.io.IoUtils;
import com.google.common.collect.Lists;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Scanner;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * <p/>
 * Executes migration from one version on db to another.
 * <p/>
 * Each statement of migration (see {@link MigrationSource}) must contain symbol ';' in the end, it can be multiline.
 * Migration can contain comments, started from symbol '#'
 *
 * @author Alexey Danilov (danikula@gmail.com).
 */
public class Migration {

    private final MigrationSource migrationSource;
    private final MigrationExecutor migrationExecutor;

    public Migration(MigrationSource migrationSource, MigrationExecutor migrationExecutor) {
        this.migrationSource = checkNotNull(migrationSource);
        this.migrationExecutor = checkNotNull(migrationExecutor);
    }

    public void execute() throws MigrationException {
        InputStream source = null;
        try {
            source = new BufferedInputStream(migrationSource.open());
            Scanner scanner = new Scanner(source);
            List<String> statements = readSource(scanner);
            migrationExecutor.execute(statements);
        } finally {
            IoUtils.closeSilently(source);
        }
    }

    private List<String> readSource(Scanner scanner) throws MigrationException {
        List<String> statements = Lists.newArrayList();
        StringBuilder statement = new StringBuilder();
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine().trim();
            if (TextUtils.isEmpty(line) || line.startsWith("#")) {
                continue;
            }
            if (statement.length() != 0) {
                statement.append(' ');
            }
            statement.append(line);
            boolean statementCompleted = statement.charAt(statement.length() - 1) == ';';
            if (statementCompleted) {
                statements.add(statement.toString());
                statement = new StringBuilder();
            }
        }
        if (statement.length() != 0) {
            throw new MigrationException("Last statement '" + statement + "' hasn't ';' in the end!");
        }
        return statements;
    }
}
