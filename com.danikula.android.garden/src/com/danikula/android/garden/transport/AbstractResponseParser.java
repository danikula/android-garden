package com.danikula.android.garden.transport;

public abstract class AbstractResponseParser<T> {

    public abstract T parseResponse(String serverResponse) throws ResponseParsingException;

}
