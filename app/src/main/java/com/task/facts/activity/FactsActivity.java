package com.task.facts.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.animation.DecelerateInterpolator;

import com.task.facts.R;
import com.task.facts.adapter.FactsRecyclerViewAdapter;
import com.task.facts.apiclient.RetrofitApiClient;
import com.task.facts.interfaces.RetrofitApiInterface;
import com.task.facts.model.FactsResponse;
import com.task.facts.model.Row;
import com.task.facts.utils.AlertDialog;
import com.task.facts.utils.ConnectionUtils;
import com.task.facts.utils.FactsShimmerRecycler;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class FactsActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    private ArrayList<Row> rowArrayList;
    private ArrayList<Row> tempRowArrayList;
    private FactsShimmerRecycler recyclerView;
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

        pullDownRefresh = findViewById(R.id.pullDownRefresh);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        initialRVAnimation(recyclerView, this);

        pullDownRefresh.setOnRefreshListener(this);
        rowArrayList = new ArrayList<>();
        tempRowArrayList = new ArrayList<>();
        getFactsData();
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    /**
     * Getting facts response data from the facts.json api
     */
    private void getFactsData() {
        if (ConnectionUtils.isOnline(FactsActivity.this)) {
            RetrofitApiInterface apiService =
                    RetrofitApiClient.getClient().create(RetrofitApiInterface.class);

            Call<FactsResponse> call = apiService.getFactResponse();
            call.enqueue(new Callback<FactsResponse>() {
                @Override
                public void onResponse(Call<FactsResponse> call, Response<FactsResponse> response) {
                    if (!rowArrayList.isEmpty()) {
                        rowArrayList.clear();
                        tempRowArrayList.clear();
                    }
                    pullDownRefresh.setRefreshing(false);
                    if (response.body() != null) {

                        getSupportActionBar().setTitle(response.body().getTitle());
                        tempRowArrayList.addAll(response.body().getRows());
                        for (Row row : tempRowArrayList) {
                            if (row.getTitle() == null &&
                                    row.getDescription() == null &&
                                    row.getImageHref() == null) {

                            } else {
                                rowArrayList.add(row);
                            }
                        }
                        adapter = new FactsRecyclerViewAdapter(FactsActivity.this, rowArrayList);
                        recyclerView.setAdapter(adapter);

                    }

                }

                @Override
                public void onFailure(Call<FactsResponse> call, Throwable t) {
                    pullDownRefresh.setRefreshing(false);

                }
            });
        } else {
            AlertDialog.openDialog(FactsActivity.this, getResources().getString(R.string.no_connection), getResources().getString(R.string.retry), getResources().getString(R.string.alert_ok), new AlertDialog.PositiveButton() {
                @Override
                public void onButtonClick() {
                    getFactsData();
                }
            }, new AlertDialog.NegativeButton() {
                @Override
                public void onButtonClick() {

                }
            });
        }
    }

    @Override
    public void onRefresh() {
        getFactsData();
    }

    /**
     * Animation included for RecyclerView before loading data
     *
     * @param recyclerView
     * @param activity
     */
    public static void initialRVAnimation(final RecyclerView recyclerView, final Activity activity) {

        ViewTreeObserver.OnPreDrawListener rvAnims = new ViewTreeObserver.OnPreDrawListener() {

            @Override
            public boolean onPreDraw() {
                recyclerView.getViewTreeObserver().removeOnPreDrawListener(this);
                for (int i = 0; i < recyclerView.getChildCount(); i++) {
                    View v = recyclerView.getChildAt(i);
                    v.setTranslationY(getScreenHeight(activity));
                    v.animate()
                            .setStartDelay(50 * i)
                            .translationY(0)
                            .setInterpolator(new DecelerateInterpolator(3.f))
                            .setDuration(700)
                            .start();
                }

                return true;
            }

        };

        recyclerView.getViewTreeObserver().addOnPreDrawListener(rvAnims);
    }

    /**
     * Getting height of the screen
     *
     * @param context
     * @return
     */
    public static int getScreenHeight(Context context) {
        DisplayMetrics metrics = new DisplayMetrics();
        WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        manager.getDefaultDisplay().getMetrics(metrics);
        return metrics.heightPixels;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
