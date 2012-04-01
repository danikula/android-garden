package com.danikula.android.garden.transport;

import java.io.InputStream;

public class EmptyResponseHandler<T> extends ResponseHandler<T> {

    @Override
    public T proceed(InputStream inputStream) {
        return null;
    }

}
