package com.example.github_dmos5.api;


import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

import com.example.github_dmos5.model.GitHub;

public interface RetrofitService {

    @GET("{github}")
    Call<GitHub> getDados(@Path("github") String usuario);
}
