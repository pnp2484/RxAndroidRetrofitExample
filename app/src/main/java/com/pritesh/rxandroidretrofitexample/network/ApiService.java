package com.pritesh.rxandroidretrofitexample.network;

import com.pritesh.rxandroidretrofitexample.models.User;

import java.util.List;

import io.reactivex.Single;
import retrofit2.http.GET;


public interface ApiService {

    // Fetch all user
    @GET("/users")
    Single<List<User>> fetchAllUsers();

}
