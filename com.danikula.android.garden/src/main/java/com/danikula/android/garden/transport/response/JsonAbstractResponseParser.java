package com.danikula.android.garden.transport.response;

import org.json.JSONException;
import org.json.JSONObject;


public abstract class JsonAbstractResponseParser<T> extends AbstractResponseParser<T> {

    public T parseResponse(String serverResponse) throws ResponseParsingException {
        try {
            JSONObject responseJson = new JSONObject(serverResponse);
            return parseJson(responseJson);
        }
        catch (JSONException e) {
            throw new ResponseParsingException("Error parsing json based server response", e);
        }
    }

    protected abstract T parseJson(JSONObject jsonObject) throws JSONException;

}
