package com.danikula.android.garden.transport;

import java.io.UnsupportedEncodingException;
import java.util.LinkedList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.protocol.HTTP;

public abstract class AbstractMultiPartRequest<T> extends AbstractRequest<T> {

    private List<ByteArrayBody> attaches = new LinkedList<ByteArrayBody>();

    public HttpRequestBase getRequest() {
        if (attaches.isEmpty()) {
            return super.getRequest();
        }

        HttpMethod httpMethod = getHttpMethod();
        if (HttpMethod.POST.equals(httpMethod) || HttpMethod.PUT.equals(httpMethod)) {
            HttpRequestBase request = HttpRequestFactory.newRequestInstance(httpMethod);
            setMultiPartEntity((HttpEntityEnclosingRequestBase) request);
            return request;
        }
        else {
            throw new IllegalStateException("Attaches are permitted only for POST and PUT methods!");
        }
    }

    protected void addAttach(String fileName, byte[] attach) {
        attaches.add(new ByteArrayBody(attach, fileName));
    }

    private void setMultiPartEntity(HttpEntityEnclosingRequestBase request) {
        MultipartEntity multipartEntity = new MultipartEntity();
        for (NameValuePair param : getParams()) {
            multipartEntity.addPart(param.getName(), newStringBody(param));
        }
        for (ByteArrayBody attach : attaches) {
            multipartEntity.addPart(attach.getFilename(), attach);
        }
        request.setEntity(multipartEntity);
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

}
