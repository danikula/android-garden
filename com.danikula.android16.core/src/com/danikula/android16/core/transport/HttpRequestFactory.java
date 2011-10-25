package com.danikula.android16.core.transport;

import java.util.HashMap;

import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;

/* package private */ class HttpRequestFactory {

    private static final HashMap<HttpMethod, HttpRequestBase> REQUEST_POOL = new HashMap<HttpMethod, HttpRequestBase>();

    static {
        REQUEST_POOL.put(HttpMethod.GET, new HttpGet());
        REQUEST_POOL.put(HttpMethod.POST, new HttpPost());
        REQUEST_POOL.put(HttpMethod.DELETE, new HttpDelete());
        REQUEST_POOL.put(HttpMethod.PUT, new HttpPut());
    }

    /* package private */static HttpRequestBase newRequestInstance(HttpMethod httpMethod) {
        try {
            return (HttpRequestBase) REQUEST_POOL.get(httpMethod).clone();
        }
        catch (CloneNotSupportedException e) {
            // it's nothing happen, HttpRequestBase is clonable
            throw new IllegalStateException("Can't clone request");
        }
    }
}
