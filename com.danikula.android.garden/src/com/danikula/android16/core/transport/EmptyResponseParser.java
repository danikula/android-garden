package com.danikula.android16.core.transport;

import java.io.InputStream;

public class EmptyResponseParser extends AbstractResponseParser<Void> {

    public Void parseResponse(InputStream serverResponse) throws ResponseParsingException {
        return null;
    }
}
