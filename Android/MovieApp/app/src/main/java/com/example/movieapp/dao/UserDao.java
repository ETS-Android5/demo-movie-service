package com.example.movieapp.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.movieapp.entity.User;

import java.util.List;

@Dao
public interface UserDao {

    @Query("SELECT * FROM user")
    LiveData<List<User>> getAll();

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertUser(User... users);

    @Query("DELETE FROM user")
    void deleteAll();
}

