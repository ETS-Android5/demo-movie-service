package com.example.movieapp.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.movieapp.callback.IHttpCallBack;
import com.example.movieapp.entity.MovieInfo;
import com.example.movieapp.entity.User;
import com.example.movieapp.repository.UserRepository;
import com.example.movieapp.service.HttpService;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class MainViewModel extends AndroidViewModel implements IHttpCallBack {

    private UserRepository userRepository;
    private LiveData<List<User>> users;
    private HttpService httpService;
    private MutableLiveData<MovieInfo> movieInfoLiveData;
    private MutableLiveData<Boolean> isRatedLiveData;

    public MainViewModel(@NonNull Application application) {
        super(application);

        userRepository = new UserRepository(application);
        users = userRepository.getAll();
        httpService = new HttpService(this);
        movieInfoLiveData = new MutableLiveData<MovieInfo>() {};
        isRatedLiveData = new MutableLiveData<Boolean>() {};
    }

    public LiveData<List<User>> getUsers() {
        return users;
    }

    void setMovieInfoLiveData(MovieInfo movieInfo) {
        movieInfoLiveData.postValue(movieInfo);
    }

    public MutableLiveData<Boolean> getIsRatedLiveData() {
        return isRatedLiveData;
    }

    public void setIsRatedLiveData(Boolean aBoolean) {
        isRatedLiveData.postValue(aBoolean);
    }

    void saveNewUser(User user) {
        userRepository.insertUser(user);
    }

    public LiveData<MovieInfo> getMovieInfoLiveData() {
        return movieInfoLiveData;
    }

    public HttpService getHttpService() {
        return httpService;
    }

    @Override
    public void onSuccess(String request_code, Object request, String data) {
        System.out.println(data);
        if(request_code.equals("getSearchMovieByTitleAndYear") || request_code.equals("getSearchMovieByTitle")){
            Type type =  new TypeToken<MovieInfo>(){}.getType();
            MovieInfo movieInfo = new Gson().fromJson(data, type);
            if(movieInfo.getResponse().equals("True")){
                movieInfoLiveData.postValue(movieInfo);
            }
        }

        if(request_code.equals("rating")){
            isRatedLiveData.postValue(true);
        }
    }

    @Override
    public void onFail(String request_code, Object request, String data) {
        System.out.println(data);
    }
}
