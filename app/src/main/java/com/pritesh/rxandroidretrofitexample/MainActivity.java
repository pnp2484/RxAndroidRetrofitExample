package com.pritesh.rxandroidretrofitexample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.pritesh.rxandroidretrofitexample.adapters.UserAdapter;
import com.pritesh.rxandroidretrofitexample.models.User;
import com.pritesh.rxandroidretrofitexample.network.ApiClient;
import com.pritesh.rxandroidretrofitexample.network.ApiService;
import com.pritesh.rxandroidretrofitexample.utils.RecyclerTouchListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

//https://www.androidhive.info/RxJava/android-rxjava-networking-with-retrofit-gson-notes-app/
public class MainActivity extends AppCompatActivity
{
    private static final String TAG = MainActivity.class.getSimpleName();
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    private ApiService apiService;
    private CompositeDisposable disposable = new CompositeDisposable();
    private List<User> mUserList = new ArrayList<>();
    private UserAdapter mUserAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        apiService = ApiClient.getClient(getApplicationContext())
                .create(ApiService.class);

        mUserAdapter = new UserAdapter(this, mUserList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        //recyclerView.addItemDecoration(new MyDividerItemDecoration(this, LinearLayoutManager.VERTICAL, 16));
        recyclerView.setAdapter(mUserAdapter);

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(this,
                recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, final int position) {
            }

            @Override
            public void onLongClick(View view, int position) {
                showToastDialog(position);
            }
        }));

        fetchAllUsers();

    }

    private void showToastDialog(int position)
    {
        User mUser = mUserList.get(position);
        Toast.makeText(this, mUser.getName(), Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        if(disposable != null && !disposable.isDisposed())
        {
            disposable.dispose();
        }
    }

    private void fetchAllUsers()
    {
        disposable.add(
                apiService.fetchAllUsers()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableSingleObserver<List<User>>()
                        {
                            @Override
                            public void onSuccess(List<User> notes)
                            {
                                mUserList.clear();
                                mUserList.addAll(notes);
                                mUserAdapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onError(Throwable e)
                            {
                                Log.e(TAG, "onError: " + e.getMessage());
                            }
                        })
        );
    }
}
