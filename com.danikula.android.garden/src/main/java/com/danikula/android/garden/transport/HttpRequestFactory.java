package com.danikula.android.garden.transport;

import java.util.HashMap;

import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;

import com.danikula.android.garden.transport.request.Method;

// TODO: make package private
public  class HttpRequestFactory {

    private static final HashMap<Method, HttpRequestBase> HTTP_REQUESTS = new HashMap<Method, HttpRequestBase>();

    static {
        HTTP_REQUESTS.put(Method.GET, new HttpGet());
        HTTP_REQUESTS.put(Method.POST, new HttpPost());
        HTTP_REQUESTS.put(Method.DELETE, new HttpDelete());
        HTTP_REQUESTS.put(Method.PUT, new HttpPut());
    }

    public static HttpRequestBase newRequestInstance(Method httpMethod) {
        try {
            return (HttpRequestBase) HTTP_REQUESTS.get(httpMethod).clone();
        }
        catch (CloneNotSupportedException e) {
            // it's nothing happen, HttpRequestBase is clonable
            throw new IllegalStateException("Can't clone request");
        }
    }
}


