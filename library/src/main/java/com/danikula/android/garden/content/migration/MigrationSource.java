package com.danikula.android.garden.content.migration;

import java.io.InputStream;

/**
 * Source for migration.
 *
 * @author Alexey Danilov (danikula@gmail.com).
 */
public interface MigrationSource {

    InputStream open() throws MigrationException;

}
