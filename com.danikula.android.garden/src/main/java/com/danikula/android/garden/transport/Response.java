package com.danikula.android.garden.transport;

import java.io.InputStream;

public class Response {
    
    public static final long UNKNOW_SIZE = -1;

    private long size = UNKNOW_SIZE;
    
    private InputStream content;
    
    public Response(InputStream content) {
        this.content = content;
    }

    public Response(InputStream content, long size) {
        this.content = content;
        this.size = size;
    }

    public long getSize() {
        return size;
    }

    public InputStream getContent() {
        return content;
    }

    public boolean isSizeKnown () {
        return size != UNKNOW_SIZE;
    }
    
}
