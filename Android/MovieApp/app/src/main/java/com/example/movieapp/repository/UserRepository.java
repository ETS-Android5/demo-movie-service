package com.example.movieapp.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.movieapp.dao.UserDao;
import com.example.movieapp.entity.User;
import com.example.movieapp.service.DatabaseService;

import java.util.List;

public class UserRepository {
    private UserDao userDao;
    private LiveData<List<User>> users;

    public UserRepository(Application application) {
        DatabaseService db = DatabaseService.getDatabase(application);
        userDao = db.userDao();
        users = userDao.getAll();
    }

    public LiveData<List<User>> getAll(){
        return users;
    }

    public void insertUser(User... users){
        DatabaseService.databaseWriteExecutor.execute(() -> {
            userDao.insertUser(users);
        });
    }

}