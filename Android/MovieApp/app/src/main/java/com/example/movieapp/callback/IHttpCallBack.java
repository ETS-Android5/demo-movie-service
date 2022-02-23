package com.example.movieapp.callback;

public interface IHttpCallBack<T> {
    void onSuccess(String request_code, T request, String data);
    void onFail(String request_code, T request, String data);
}
