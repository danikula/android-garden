package com.danikula.android.garden.content.migration;

import java.util.List;

/**
 * Executes statements of migration.
 *
 * @author Alexey Danilov (danikula@gmail.com).
 */
public interface MigrationExecutor {

    void execute(List<String> statements) throws MigrationException;
}
