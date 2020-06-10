package com.savr.moviedb.datasource;

import com.savr.moviedb.model.room.User;

import java.util.List;

import io.reactivex.Flowable;

public class UserRepository implements IUserDataSource {

    private IUserDataSource localDataSource;
    public static UserRepository instance;

    public UserRepository(IUserDataSource localDataSource) {
        this.localDataSource = localDataSource;
    }

    public static UserRepository getInstance(IUserDataSource localDataSource) {
        if (instance == null) {
            instance = new UserRepository(localDataSource);
        }
        return instance;
    }

    @Override
    public Flowable<User> getUserById(int userId) {
        return localDataSource.getUserById(userId);
    }

    @Override
    public Flowable<List<User>> getAllUsers() {
        return localDataSource.getAllUsers();
    }

    @Override
    public void insertUser(User... users) {
        localDataSource.insertUser(users);
    }

    @Override
    public void updateUser(User... users) {
        localDataSource.updateUser(users);
    }

    @Override
    public void deleteUser(User user) {
        localDataSource.deleteUser(user);
    }

    @Override
    public void deleteAllUsers() {
        localDataSource.deleteAllUsers();
    }
}
