package com.danikula.android.garden.content.migration;

import android.content.res.Resources;

import java.io.IOException;
import java.io.InputStream;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Source for {@link Migration} that read migration from android assets.
 * <p/>
 * Name of assets resource must be in format {@link #MIGRATION_NAME_FORMAT}.
 *
 * @author Alexey Danilov (danikula@gmail.com).
 */
public class AssetsMigrationSource implements MigrationSource {

    public static final String MIGRATION_NAME_FORMAT = "migration/from_%d_to_%d.sql";

    private final Resources resources;
    private final int from;
    private final int to;

    public AssetsMigrationSource(Resources resources, int from, int to) {
        this.resources = checkNotNull(resources);
        this.from = from;
        this.to = to;
    }

    @Override
    public InputStream open() throws MigrationException {
        try {
            String fileName = String.format(MIGRATION_NAME_FORMAT, from, to);
            return resources.getAssets().open(fileName);
        } catch (IOException e) {
            throw new MigrationException("Error reading migration source for migration from " + from + " to " + to, e);
        }
    }
}