package com.danikula.android.garden.content.migration;

import com.google.common.collect.Lists;

import java.util.Collections;
import java.util.List;

/**
 * Migration executor that does nothing excepts storing statements.
 * <p/>
 * Useful for tests.
 *
 * @author Alexey Danilov (danikula@gmail.com).
 */
public class SimpleMigrationExecutor implements MigrationExecutor {

    private final List<String> statements = Lists.newArrayList();

    public List<String> getStatements() {
        return Collections.unmodifiableList(statements);
    }

    @Override
    public void execute(List<String> statements) throws MigrationException {
        this.statements.addAll(statements);
    }
}
