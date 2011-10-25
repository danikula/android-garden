package com.danikula.android16.core.transport;

import com.danikula.android16.core.storage.Storage;
import com.danikula.android16.core.utils.Validate;

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
