package com.danikula.android.garden.transport.request;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.LinkedList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import com.danikula.android.garden.transport.HttpRequestFactory;
import com.danikula.android.garden.transport.response.AbstractResponseParser;
import com.danikula.android.garden.transport.response.ResponseParsingException;
import com.danikula.android.garden.utils.StringUtils;

public abstract class AbstractRequest<T> {
    
    public static final String CONTENT_TYPE_JSON = "application/json";

    private Method httpMethod = Method.GET;

    private String baseUrl;

    private String urlContext;

    private List<NameValuePair> params = new LinkedList<NameValuePair>();

    private String stringBody;
    
    private String contentType;

    protected abstract AbstractResponseParser<T> getResponseParser();

    public T parseServerResponse(String serverResponse) throws ResponseParsingException {
        return getResponseParser().parseResponse(serverResponse);
    }

    public void addParameter(String name, String value) {
        params.add(new BasicNameValuePair(name, value));
    }

    public void addParameter(String name, int value) {
        addParameter(name, Integer.toString(value));
    }

    public void setStringBody(String body) {
        this.stringBody = body;
    }
    
    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public HttpRequestBase getRequest() {
        HttpRequestBase request = HttpRequestFactory.newRequestInstance(httpMethod);
        if (Method.POST.equals(httpMethod) || Method.PUT.equals(httpMethod)) {
            request.setURI(getURI(getUrl()));
            HttpEntityEnclosingRequestBase entityRequest = ((HttpEntityEnclosingRequestBase) request);
            StringEntity entity = stringBody != null ? newStringBodyEntity(stringBody) : newUrlEncodedFormEntity(params);
            if (contentType != null) {
                entity.setContentType(contentType);
            }
            entityRequest.setEntity(entity);
        }
        else {
            if (stringBody != null) {
                throw new IllegalArgumentException("String body is permitted only for POST requests");
            }
            request.setURI(getURI(getUrlWithParams()));
        }
        return request;
    }

    public String getId() {
        return StringUtils.computeMD5(httpMethod + getUrlWithParams());
    }
    
    public boolean isCachable() {
        return true;
    }

    protected void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    protected void setUrlContext(String urlContext) {
        this.urlContext = urlContext;
    }

    protected void setHttpMethod(Method httpMethod) {
        this.httpMethod = httpMethod;
    }

    protected Method getHttpMethod() {
        return httpMethod;
    }

    protected List<NameValuePair> getParams() {
        return params;
    }

    private String getUrl() {
        return baseUrl + (!StringUtils.isEmpty(urlContext) ? "/" + urlContext : "");
    }

    private String getUrlWithParams() {
        StringBuffer fullUrl = new StringBuffer(getUrl());
        if (!params.isEmpty()) {
            fullUrl.append("?");
            for (NameValuePair param : params) {
                addStringParameter(fullUrl, param.getName(), param.getValue());
            }
        }
        return fullUrl.toString();
    }

    private void addStringParameter(StringBuffer url, String name, String value) {
        url.append(urlEncode(name));
        url.append("=");
        url.append(urlEncode(value));
        url.append("&");
    }

    private String urlEncode(String string) {
        try {
            return URLEncoder.encode(string, HTTP.UTF_8);
        }
        catch (UnsupportedEncodingException e) {
            // this code will not be invoked
            throw new IllegalStateException("Can't encode url with encoding " + HTTP.UTF_8, e);
        }
    }

    private URI getURI(String uri) {
        try {
            return new URI(uri);
        }
        catch (URISyntaxException e) {
            throw new IllegalArgumentException("Incorrect format of uri: " + uri);
        }
    }

    private UrlEncodedFormEntity newUrlEncodedFormEntity(List<NameValuePair> params) {
        try {
            return new UrlEncodedFormEntity(params, HTTP.UTF_8);
        }
        catch (UnsupportedEncodingException e) {
            // this code will not be invoked
            throw new IllegalArgumentException("Can't encode params with encoding " + HTTP.UTF_8, e);
        }
    }

    private StringEntity newStringBodyEntity(String body) {
        try {
            return new StringEntity(body, HTTP.UTF_8);
        }
        catch (UnsupportedEncodingException e) {
            // this code will not be invoked
            throw new IllegalArgumentException("Can't encode params with encoding " + HTTP.UTF_8, e);
        }
    }

    @Override
    public String toString() {
        return String.format("%s: %s %s", getClass().getSimpleName(), httpMethod, getUrlWithParams());
    }
}
