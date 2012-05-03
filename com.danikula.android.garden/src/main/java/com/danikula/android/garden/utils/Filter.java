package com.danikula.android.garden.utils;

/**
 * Фильт, использующийся для фильтрации списка в методе {@link #filterList}
 * 
 * @author danik
 * @param <T> тип объекта, который содержится в списке
 */
public interface Filter<T> {
    /**
     * Проверяет, удовлетворяет ли объект критерию фильтра
     * 
     * @param item проверяемый объект
     * @return <code>true</code> если объект удовлетворяет критерию фильтра
     */
    public boolean mathes(T item);
}