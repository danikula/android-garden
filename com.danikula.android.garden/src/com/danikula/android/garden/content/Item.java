package com.danikula.android.garden.content;

/*package private*/class Item {

    private String table;

    private String idPropertyName;

    private boolean single;

    /* package private */Item(String table, String idPropertyName, boolean single) {
        this.table = table;
        this.idPropertyName = idPropertyName;
        this.single = single;
    }

    public String getTable() {
        return table;
    }

    public String getIdPropertyName() {
        return idPropertyName;
    }

    public boolean isSingle() {
        return single;
    }

}
