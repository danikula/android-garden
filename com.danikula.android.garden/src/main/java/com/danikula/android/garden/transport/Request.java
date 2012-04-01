package com.danikula.android.garden.transport;

import com.danikula.android.garden.transport.request.Method;
import com.danikula.android.garden.utils.Validate;

public class Request {

    private Method method;

    private String baseUrl;

    private ResponseHandler<?> responseHandler;

    public Request(String baseUrl) {
        Validate.notNull(baseUrl, "Url");
        this.baseUrl = baseUrl;

        this.responseHandler = new EmptyResponseHandler();
        this.method = Method.GET;
    }

    public <T> ResponseHandler<T> getResponseHandler() {
        return (ResponseHandler<T>) responseHandler;
    }

    public Method getMethod() {
        return method;
    }
    
    

}
