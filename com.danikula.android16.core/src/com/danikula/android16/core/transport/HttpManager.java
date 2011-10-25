package com.danikula.android16.core.transport;

import java.io.IOException;
import java.io.InputStream;

public interface HttpManager {
    
//    public String getStringContent(String requestUrl) throws IOException;

    public InputStream invoke(String requestUrl, HttpMethod method) throws IOException;
}
