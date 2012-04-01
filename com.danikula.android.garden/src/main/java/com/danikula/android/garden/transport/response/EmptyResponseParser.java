package com.danikula.android.garden.transport.response;


public class EmptyResponseParser extends AbstractResponseParser<Void> {

    public Void parseResponse(String serverResponse) throws ResponseParsingException {
        return null;
    }
}
