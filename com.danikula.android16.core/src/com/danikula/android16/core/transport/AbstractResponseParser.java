package com.danikula.android16.core.transport;

import java.io.IOException;
import java.io.InputStream;

import org.json.JSONException;
import org.json.JSONObject;

import com.danikula.android16.core.utils.IoUtils;

public abstract class AbstractResponseParser<T> {

    public T parseResponse(InputStream serverResponse) throws ResponseParsingException {
        try {
            String response = IoUtils.streamToString(serverResponse);
            JSONObject responseJson = new JSONObject(response);
            return parseJson(responseJson);
        }
        catch (IOException e) {
            throw new ResponseParsingException("IO error!", e);
        }
        catch (JSONException e) {
            throw new ResponseParsingException("Error parsing", e);
        }
    }
    
    protected abstract T parseJson(JSONObject jsonObject) throws JSONException ;

}
