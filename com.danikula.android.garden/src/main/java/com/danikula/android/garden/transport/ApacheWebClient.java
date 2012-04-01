package com.danikula.android.garden.transport;

import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;

import com.danikula.android.garden.transport.request.Method;

public class ApacheWebClient implements WebClient {

    private static final String USER_AGENT = "Apache-HttpClient/Android";

    private static final int DEFAULT_TIMEOUT_MS = 10000;

    private HttpClient httpClient;

    private ApacheWebClient() {
        this.httpClient = createHttpClient(DEFAULT_TIMEOUT_MS);
    }

    private ApacheWebClient(int timeoutMs) {
        this.httpClient = createHttpClient(timeoutMs);
    }

    @Override
    public Response invoke(Request request) throws TransportException {
        try {
            HttpRequestBase httpRequest = convertToHttpRequest(request);

            HttpResponse httpResponse = httpClient.execute(httpRequest);

            HttpEntity httpEntity = httpResponse.getEntity();
            long contentLenght = httpEntity.getContentLength();
            InputStream content = httpEntity.getContent();
            return new Response(content, contentLenght);
        }
        catch (IOException e) {
            throw new TransportException(String.format("Error executing request '%s'", request), e);
        }
    }

    private org.apache.http.client.HttpClient createHttpClient(int timeoutMs) {
        HttpParams params = new BasicHttpParams();
        params.setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
        params.setParameter(CoreProtocolPNames.HTTP_CONTENT_CHARSET, HTTP.UTF_8);
        params.setParameter(CoreProtocolPNames.USER_AGENT, USER_AGENT);
        params.setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, timeoutMs);
        // params.setParameter(CoreConnectionPNames.STALE_CONNECTION_CHECK, false);

        SchemeRegistry schemeRegistry = new SchemeRegistry();
        schemeRegistry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
        schemeRegistry.register(new Scheme("https", new EasySSLSocketFactory(), 443));

        ThreadSafeClientConnManager cm = new ThreadSafeClientConnManager(params, schemeRegistry);

        return new DefaultHttpClient(cm, params);
    }

    private HttpRequestBase convertToHttpRequest(Request request) {
        Method method = request.getMethod();
        HttpRequestBase apacheRequest = HttpRequestFactory.newRequestInstance(method);
        // if (Method.POST.equals(httpMethod) || Method.PUT.equals(httpMethod)) {
        // apacheRequest.setURI(getURI(getUrl()));
        // HttpEntityEnclosingRequestBase entityRequest = ((HttpEntityEnclosingRequestBase) apacheRequest);
        // StringEntity entity = stringBody != null ? newStringBodyEntity(stringBody) : newUrlEncodedFormEntity(params);
        // if (contentType != null) {
        // entity.setContentType(contentType);
        // }
        // entityRequest.setEntity(entity);
        // }
        // else {
        // if (stringBody != null) {
        // throw new IllegalArgumentException("String body is permitted only for POST requests");
        // }
        // apacheRequest.setURI(getURI(getUrlWithParams()));
        // }
        // return apacheRequest;
        return new HttpGet();
    }
}
