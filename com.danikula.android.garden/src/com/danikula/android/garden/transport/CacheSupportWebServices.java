package com.danikula.android.garden.transport;

import com.danikula.android.garden.storage.Storage;
import com.danikula.android.garden.utils.Validate;

public class CacheSupportWebServices extends WebServices {

    private Storage<String, Object> cache;

    public CacheSupportWebServices(Storage<String, Object> cache) {
        Validate.notNull(cache, "cache");
        this.cache = cache;
    }

    @Override
    public <T> T invoke(AbstractRequest<T> request) throws TransportException {
        String requestId = request.getId();
        T response = (T) (cache.contains(requestId) ? cache.get(requestId) : super.invoke(request));
        cache.put(requestId, response);
        return response;
    }
}
