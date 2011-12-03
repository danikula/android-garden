package com.danikula.android.garden.transport;

import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
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

import com.danikula.android.garden.io.IoUtils;

public class WebServices {

    private static final int TIMEOUT_MS = 10000;

    private HttpClient httpClient;

    public WebServices() {
        httpClient = createHttpClient();
    }

    public <T> T invoke(AbstractRequest<T> request) throws TransportException {
        onBeforeInvoke(request);
        InputStream serverResponse = null;
        try {
            serverResponse = getInputStream(request);
            String content = IoUtils.streamToString(serverResponse);
            onServerResponseReceived(request, content);
            T response = request.parseServerResponse(content);
            onAfterInvoke(request, response);
            return response;
        }
        catch (IOException e) {
            throw new TransportException("Network level problem is occur", e);
        }
        catch (ResponseParsingException e) {
            throw new TransportException("Error parse server's response", e);
        }
        finally {
            IoUtils.closeSilently(serverResponse);
        }
    }

    protected <T> void onBeforeInvoke(AbstractRequest<T> request) {
        // do noting by default
    }

    protected <T> void onServerResponseReceived(AbstractRequest<T> request, String response) {
        // do noting by default
    }

    protected <T> void onAfterInvoke(AbstractRequest<T> request, T response) {
        // do noting by default
    }

    private HttpClient createHttpClient() {
        HttpParams params = new BasicHttpParams();
        params.setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
        params.setParameter(CoreProtocolPNames.HTTP_CONTENT_CHARSET, HTTP.UTF_8);
        params.setParameter(CoreProtocolPNames.USER_AGENT, "Apache-HttpClient/Android");
        params.setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, TIMEOUT_MS);
        // params.setParameter(CoreConnectionPNames.STALE_CONNECTION_CHECK, false);

        SchemeRegistry schemeRegistry = new SchemeRegistry();
        schemeRegistry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));

        ThreadSafeClientConnManager cm = new ThreadSafeClientConnManager(params, schemeRegistry);

        return new DefaultHttpClient(cm, params);
    }

    public <T> InputStream getInputStream(AbstractRequest<T> request) throws IOException {
        HttpResponse response = httpClient.execute(request.getRequest());
        StatusLine status = response.getStatusLine();
        if (status.getStatusCode() != HttpStatus.SC_OK) {
            // dumpResponse(response.getEntity().getContent());
            throw new IOException(String.format("Error getting data from '%s'. %d %s", request, status.getStatusCode(),
                    status.getReasonPhrase()));
        }
        return response.getEntity().getContent();
    }
}
