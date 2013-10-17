package com.danikula.android.garden.task;

public interface OnRequestListener {

    void onRequestSuccess(Request request, int requestId, Object result);

    void onRequestError(Request request, int requestId, Exception error);
    
    void onRequestCancel(Request request, int requestId);

}