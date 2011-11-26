package com.danikula.android.garden.transport;

import java.io.IOException;
import java.io.InputStream;

import org.json.JSONException;
import org.json.JSONObject;

import com.danikula.android.garden.io.IoUtils;

public abstract class JsonAbstractResponseParser<T> extends AbstractResponseParser<T> {

    public T parseResponse(InputStream serverResponse) throws ResponseParsingException {
        try {
            String response = IoUtils.streamToString(serverResponse);
            JSONObject responseJson = new JSONObject(response);
            return parseJson(responseJson);
        }
        catch (IOException e) {
            throw new ResponseParsingException("Error parsing json based server response", e);
        }
        catch (JSONException e) {
            throw new ResponseParsingException("Error parsing json based server response", e);
        }
    }

    protected abstract T parseJson(JSONObject jsonObject) throws JSONException;

}