package com.danikula.android.garden.content;

/*package private*/class Item {

    private String table;

    private boolean single;

    /* package private */Item(String table, boolean single) {
        this.table = table;
        this.single = single;
    }

    public String getTable() {
        return table;
    }

    public boolean isSingle() {
        return single;
    }

}
