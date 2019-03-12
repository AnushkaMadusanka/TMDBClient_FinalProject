package com.androidtutz.anushka.tmdbclient.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.paging.LivePagedListBuilder;
import android.arch.paging.PagedList;
import android.support.annotation.NonNull;

import com.androidtutz.anushka.tmdbclient.model.Movie;
import com.androidtutz.anushka.tmdbclient.model.MovieDataSource;
import com.androidtutz.anushka.tmdbclient.model.MovieDataSourceFactory;
import com.androidtutz.anushka.tmdbclient.model.MovieRepository;
import com.androidtutz.anushka.tmdbclient.service.MovieDataService;
import com.androidtutz.anushka.tmdbclient.service.RetrofitInstance;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class MainActivityViewModel extends AndroidViewModel {
    private MovieRepository movieRepository;

    LiveData<MovieDataSource> movieDataSourceLiveData;
    private Executor executor;
    private LiveData<PagedList<Movie>> moviesPagedList;


    public MainActivityViewModel(@NonNull Application application) {
        super(application);
        movieRepository=new MovieRepository(application);

        MovieDataService movieDataService= RetrofitInstance.getService();
        MovieDataSourceFactory factory=new MovieDataSourceFactory(movieDataService,application);
        movieDataSourceLiveData=factory.getMutableLiveData();

         PagedList.Config config=(new PagedList.Config.Builder())
                                   .setEnablePlaceholders(true)
                                   .setInitialLoadSizeHint(10)
                                   .setPageSize(20)
                                   .setPrefetchDistance(4)
                                    .build();

         executor= Executors.newFixedThreadPool(5);

         moviesPagedList = (new LivePagedListBuilder<Long,Movie>(factory,config))
                 .setFetchExecutor(executor)
                 .build();


    }

    public LiveData<List<Movie>> getAllMovies(){

        return movieRepository.getMutableLiveData();
    }

    public LiveData<PagedList<Movie>> getMoviesPagedList() {
        return moviesPagedList;
    }
}
