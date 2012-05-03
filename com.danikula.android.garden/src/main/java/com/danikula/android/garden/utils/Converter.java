package com.danikula.android.garden.utils;

public interface Converter<I, O> {

    O convert(I input);
}