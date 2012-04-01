package com.danikula.android.garden.transport;

import com.danikula.android.garden.utils.Validate;

public class HttpTransport {
    
    private WebClient webClient;
    
    public HttpTransport(WebClient webClient) {
        Validate.notNull(webClient, "HttpClient");
        this.webClient = webClient;
    }

    public <I, O> O send (Request message) {
        return null;
    }

}
