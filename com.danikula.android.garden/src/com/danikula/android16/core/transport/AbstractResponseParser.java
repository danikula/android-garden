package com.danikula.android16.core.transport;

import java.io.InputStream;

public abstract class AbstractResponseParser<T> {

    public abstract T parseResponse(InputStream serverResponse) throws ResponseParsingException;

}
