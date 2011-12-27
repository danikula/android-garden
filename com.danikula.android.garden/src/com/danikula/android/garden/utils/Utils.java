package com.danikula.android.garden.utils;

import java.util.ArrayList;
import java.util.List;

public class Utils {

    public static void sleep(long timeMs) {
        try {
            Thread.currentThread().sleep(timeMs);
        }
        catch (InterruptedException e) {
            // do nothing
        }
    }
    
    /**
     * Фильтрует список по заданному критерию.
     * 
     * @param <T> тип объекта, который содержится в списке
     * @param items List&lt;T&gt; коллекция, которую необходимо отфильтровать
     * @param filter Filter&lt;T&gt; фильтр, по которому фильтруется список
     * @return отфильтрованный список
     * @throws IllegalArgumentException если коллекция или фильтр равны <code>null</code>
     */
    public static <T> List<T> filterList(List<T> items, Filter<T> filter) {
        Validate.notNull(items, "list");
        Validate.notNull(filter, "filter");

        List<T> filteredList = new ArrayList<T>();
        for (T item : items) {
            if (filter.mathes(item)) {
                filteredList.add(item);
            }
        }
        return filteredList;
    }
    
    public static <I, O>List<O> convert(List<I> source, Converter<I, O> converter) {
        List<O> result = null;
        if (source != null) {
            result = new ArrayList<O>();
            for (I sourceItem : source) {
                O targetItem = converter.convert(sourceItem);
                result.add(targetItem);
            }
        }
        return result;
    }
    
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
    
    public interface Converter<I, O> {
        
        O convert(I input);
    }
}
