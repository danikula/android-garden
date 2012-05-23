package com.danikula.android.garden.content;

import java.util.List;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;

public class MultiTablesBuilder {
    
    private List<TableBuilder> tableBuilders = Lists.newArrayList();
    
    public TableBuilder table(String table) {
        return new TableBuilder(this, table);
    }
    
    /** package private */ MultiTablesBuilder add(TableBuilder tableBuilder) {
        tableBuilders.add(tableBuilder);
        return this;
    }
    
    public String build() {
        List<String> sqls = Lists.newArrayList();
        for (TableBuilder tableBuilder : tableBuilders) {
            sqls.add(tableBuilder.buildSql());
        }
        return Joiner.on(';').join(sqls);
    }
}
