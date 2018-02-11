package com.example.salmanasik.wiprotask.interfaces;

import com.example.salmanasik.wiprotask.model.FactsResponse;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by SalmanAsik on 11-02-2018.
 */

public interface RetrofitApiInterface {

    @GET("facts.json")
    public Call<FactsResponse> getFactResponse();
}
