package com.danikula.android.garden.transport;

public interface WebClient {

    Response invoke(Request request) throws TransportException;

}
