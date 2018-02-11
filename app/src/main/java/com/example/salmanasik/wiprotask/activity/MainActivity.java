package com.example.salmanasik.wiprotask.activity;

import android.graphics.Color;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.example.salmanasik.wiprotask.adapter.CustomAdapter;
import com.example.salmanasik.wiprotask.R;
import com.example.salmanasik.wiprotask.apiclient.RetrofitApiClient;
import com.example.salmanasik.wiprotask.interfaces.RetrofitApiInterface;
import com.example.salmanasik.wiprotask.model.FactsResponse;
import com.example.salmanasik.wiprotask.model.Row;
import com.example.salmanasik.wiprotask.utils.ConnectionUtils;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;



public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener{


    private MaterialDialog progressDialog;
    private ArrayList<Row> rowArrayList;
    private ArrayList<Row> tempRowArrayList;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;
    private SwipeRefreshLayout pullDownRefresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setTitle("");
        recyclerView = findViewById(R.id.my_recycler_view);
        recyclerView.setNestedScrollingEnabled(false);

        pullDownRefresh =  findViewById(R.id.pullDownRefresh);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        pullDownRefresh.setOnRefreshListener(this);
        rowArrayList = new ArrayList<>();
        tempRowArrayList = new ArrayList<>();
        getFactsData(true);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    private void getFactsData(boolean isProgressEnabled) {


        if (ConnectionUtils.isOnline(MainActivity.this)) {

            if(isProgressEnabled) {
                progressDialog = new MaterialDialog.Builder(MainActivity.this)
                        .content(getResources().getString(R.string.loadingText))
                        .cancelable(false)
                        .canceledOnTouchOutside(false)
                        .autoDismiss(false)
                        .contentColor(Color.DKGRAY)
                        .progress(true, 0)
                        .show();
            }

            RetrofitApiInterface apiService =
                    RetrofitApiClient.getClient().create(RetrofitApiInterface.class);

            Call<FactsResponse> call = apiService.getFactResponse();
            call.enqueue(new Callback<FactsResponse>() {
                @Override
                public void onResponse(Call<FactsResponse> call, Response<FactsResponse> response) {
                    progressDialog.dismiss();
                    if(!rowArrayList.isEmpty()){
                        rowArrayList.clear();
                        tempRowArrayList.clear();
                    }
                    pullDownRefresh.setRefreshing(false);
                    if (response.body() != null) {

                        getSupportActionBar().setTitle(response.body().getTitle());
                        tempRowArrayList.addAll(response.body().getRows());
                         for(Row row : tempRowArrayList){
                             if(row.getTitle() == null &&
                                     row.getDescription() == null &&
                                     row.getImageHref() == null) {

                             }else{
                                 rowArrayList.add(row);
                             }
                         }
                        adapter = new CustomAdapter(MainActivity.this,rowArrayList);
                        recyclerView.setAdapter(adapter);

                    }

                }

                @Override
                public void onFailure(Call<FactsResponse> call, Throwable t) {
                    progressDialog.dismiss();
                    pullDownRefresh.setRefreshing(false);


                }
            });
        } else {
           /* PNAlertUtils.error(getResources().getString(R.string.errorMsg),
                    getResources().getString(R.string.offlineMsg), PNDispatchPlumberActivity.this);*/
        }
    }

    @Override
    public void onRefresh() {
        getFactsData(false);
    }
}
