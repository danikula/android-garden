package com.danikula.android.garden.transport;

import java.io.Serializable;

import android.util.Log;

import com.danikula.android.garden.storage.Storage;
import com.danikula.android.garden.storage.StorageException;
import com.danikula.android.garden.transport.request.AbstractRequest;
import com.danikula.android.garden.utils.Validate;
// TODO: объединить с WebService
public class CacheSupportWebServices extends WebServices {

    private static final String LOG_TAG = CacheSupportWebServices.class.getName();

    private Storage<String, Serializable> cache;

    public CacheSupportWebServices(boolean trace, Storage<String, Serializable> cache) {
        super(trace);
        Validate.notNull(cache, "cache");
        this.cache = cache;
    }

    public CacheSupportWebServices(Storage<String, Serializable> cache) {
        this(false, cache);
    }

    @Override
    public <T> T invoke(AbstractRequest<T> request) throws TransportException {
        String requestId = request.getId();
        try {
            if (cache.contains(requestId) && request.isCachable()) {
                return (T) cache.get(requestId);
            }
            return invokeRequestAndCache(request, requestId);
        }
        catch (StorageException e) {
            Log.e(LOG_TAG, "Error reading payload from cache", e);
            cache.remove(requestId);
            return invokeRequestAndCache(request, requestId);
        }
    }

    private <T> T invokeRequestAndCache(AbstractRequest<T> request, String requestId) throws TransportException {
        T responsePayload;
        responsePayload = super.invoke(request);
        cacheResponsePayload(requestId, responsePayload);
        return responsePayload;
    }

    private <T> void cacheResponsePayload(String requestId, T responsePayload) {
        if (!(responsePayload instanceof Serializable)) {
            throw new IllegalStateException(String.format("Error saving payload to cache! %s is not serializable!",
                    responsePayload));
        }
        try {
            Serializable cacheItem = (Serializable) responsePayload;
            cache.put(requestId, cacheItem);
        }
        catch (StorageException e) {
            // just log
            Log.e(LOG_TAG, "Error saving payload to cache", e);
        }
    }
}
