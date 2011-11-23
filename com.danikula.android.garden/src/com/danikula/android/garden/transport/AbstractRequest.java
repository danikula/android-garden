package com.danikula.android.garden.transport;

import java.io.InputStream;
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
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import com.danikula.android.garden.utils.TextUtils;

public abstract class AbstractRequest<T> {

    private HttpMethod httpMethod = HttpMethod.GET;

    private String baseUrl;

    private String urlContext;

    private List<NameValuePair> params = new LinkedList<NameValuePair>();

    private List<ByteArrayBody> attaches = new LinkedList<ByteArrayBody>();
    
    protected abstract AbstractResponseParser<T> getResponseParser();

    public T parseServerResponse(InputStream serverResponse) throws ResponseParsingException {
        return getResponseParser().parseResponse(serverResponse);
    }

    public void addParameter(String name, String value) {
        params.add(new BasicNameValuePair(name, value));
    }

    public void addParameter(String name, int value) {
        addParameter(name, Integer.toString(value));
    }

    public HttpRequestBase getRequest() {
        HttpRequestBase request = HttpRequestFactory.newRequestInstance(httpMethod);
        if (HttpMethod.POST.equals(httpMethod) || HttpMethod.PUT.equals(httpMethod)) {
            request.setURI(getURI(getUrl()));
            if (attaches.isEmpty()) {
                UrlEncodedFormEntity entity = newUrlEncodedFormEntity(params);
                ((HttpEntityEnclosingRequestBase) request).setEntity(entity);
            }
            else {
                setMultiPartEntity((HttpEntityEnclosingRequestBase) request);
            }
        }
        else {
            if (!attaches.isEmpty()) {
                throw new IllegalStateException("Attaches are permitted only for POST and PUT methods!");
            }
            request.setURI(getURI(getUrlWithParams()));
        }
        return request;
    }
    
    public String getId(){
        return TextUtils.computeMD5(httpMethod + getUrlWithParams());
    }

    protected void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    protected void setUrlContext(String urlContext) {
        this.urlContext = urlContext;
    }

    protected void setHttpMethod(HttpMethod httpMethod) {
        this.httpMethod = httpMethod;
    }

    protected void addAttach(String fileName, byte[] attach) {
        attaches.add(new ByteArrayBody(attach, fileName));
    }

    private String getUrl() {
        return baseUrl + (!TextUtils.isEmpty(urlContext) ? "/" + urlContext : "");
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

    private StringBody newStringBody(NameValuePair param) {
        try {
            return new StringBody(param.getValue());
        }
        catch (UnsupportedEncodingException e) {
            // this code will not be invoked
            throw new IllegalArgumentException("Can't create a new StringBody with encoding " + HTTP.UTF_8, e);
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

    private void setMultiPartEntity(HttpEntityEnclosingRequestBase request) {
        MultipartEntity multipartEntity = new MultipartEntity();
        for (NameValuePair param : params) {
            multipartEntity.addPart(param.getName(), newStringBody(param));
        }
        for (ByteArrayBody attach : attaches) {
            multipartEntity.addPart(attach.getFilename(), attach);
        }
        request.setEntity(multipartEntity);
    }
    
    @Override
    public String toString() {
        return String.format("%s: %s %s", getClass().getSimpleName(), httpMethod, getUrlWithParams());
    }
}
