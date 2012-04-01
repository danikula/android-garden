package com.danikula.android.garden.transport;

import java.io.InputStream;

public abstract class ResponseHandler<T> {
    
    public abstract T proceed(InputStream inputStream);

}
