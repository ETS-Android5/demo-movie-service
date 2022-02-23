package com.example.movieapp.service;

import androidx.annotation.NonNull;

import com.example.movieapp.callback.IHttpCallBack;
import com.example.movieapp.entity.Rating;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HttpService<T>  {
    private IHttpCallBack iHttpCallBack;
    private final static String HOST = "http://192.168.1.76:8759/";

    public HttpService(IHttpCallBack iHttpCallBack) {
//        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
//        StrictMode.setThreadPolicy(policy);
        this.iHttpCallBack = iHttpCallBack;
    }

    public void getSearchMovieByTitle(String request_code, String username, String title){
        String url = HOST + "api/v1/movie" + "?username=" + username + "&title=" + title;
        OkHttpClient client = new OkHttpClient().newBuilder().build();
        Request request = new Request.Builder()
                .url(url)
                .build();

        Call call = client.newCall(request);
        call.enqueue(new Callback() {

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                iHttpCallBack.onSuccess(request_code, url, response.body().string());
            }

            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                iHttpCallBack.onFail(request_code, url, e.toString());
            }
        });
    }

    public void getSearchMovieByTitleAndYear(String request_code, String username, String title, String year){
        String url = HOST + "api/v1/movie" + "?username=" + username + "&title=" + title + "&year=" + year;
        OkHttpClient client = new OkHttpClient().newBuilder().build();
        Request request = new Request.Builder()
                .url(url)
                .build();

        Call call = client.newCall(request);
        call.enqueue(new Callback() {

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                iHttpCallBack.onSuccess(request_code, url, response.body().string());
            }

            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                iHttpCallBack.onFail(request_code, url, e.toString());
            }
        });
    }

    public void rating(String request_code, Rating rating){
        String url = HOST + "api/v1/movie";
        OkHttpClient client = new OkHttpClient().newBuilder().build();
        MediaType JSON = MediaType.get("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(JSON, new Gson().toJson(rating));
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                iHttpCallBack.onSuccess(request_code, url, response.body().string());
            }

            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                iHttpCallBack.onFail(request_code, url, e.toString());
            }
        });
    }

}
