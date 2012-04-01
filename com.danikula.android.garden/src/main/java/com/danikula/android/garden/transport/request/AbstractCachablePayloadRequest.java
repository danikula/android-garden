package com.danikula.android.garden.transport.request;

import java.io.Serializable;

/**
 * {@link AbstractRequest}, поддерживающий сохранение результата запроса в кеше.
 * 
 * <p>
 * Для сохранения можно использовать и обычный {@link AbstractRequest<Serializable>}. Этот класс необходим только для того, чтобы компилятор
 * смог проконтроливать, что payload реализует {@link Serializable}
 * </p>
 * 
 * @author danik
 * 
 * @param <T> Тип payload
 */
public abstract class AbstractCachablePayloadRequest<T extends Serializable> extends AbstractRequest<T> {

}
