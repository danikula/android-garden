package com.danikula.android.garden.transport;

import java.io.Serializable;

import com.danikula.android.garden.storage.Storage;
import com.danikula.android.garden.utils.Validate;

public class CacheSupportWebServices extends WebServices {

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
        T responsePayload = null;
        if (cache.contains(requestId)) {
            responsePayload = (T) cache.get(requestId);
        }
        else {
            responsePayload = super.invoke(request);
            cacheResponsePayload(requestId, responsePayload);
        }
        return responsePayload;
    }

    private <T> void cacheResponsePayload(String requestId, T responsePayload) {
        if (!(responsePayload instanceof Serializable)) {
            throw new IllegalStateException(String.format("Error saving payload to cache! %s is not serializable!",
                    responsePayload));
        }
        Serializable cacheItem = (Serializable) responsePayload; 
        cache.put(requestId, cacheItem);
    }
}
