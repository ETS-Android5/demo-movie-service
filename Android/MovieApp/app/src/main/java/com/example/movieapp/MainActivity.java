package com.example.movieapp;

import static android.graphics.Color.BLUE;
import static android.graphics.Color.WHITE;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;

import com.example.movieapp.databinding.ActivityMainBinding;
import com.example.movieapp.entity.MovieInfo;
import com.example.movieapp.entity.Rating;
import com.example.movieapp.viewmodel.MainViewModel;

import java.io.IOException;
import java.net.URL;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private MainViewModel mainViewModel;
    private ActivityMainBinding activityMainBinding;

    private int liked;
    private int disliked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        activityMainBinding.setMainViewModel(mainViewModel);

        mainViewModel.getUsers().observe(this, users -> {
            if(users.size()>0) {
                activityMainBinding.btnSearch.setEnabled(true);
                System.out.println("USER : " + users.get(0));
            }
        });

        activityMainBinding.btnSearch.setOnClickListener(this);
        activityMainBinding.tvIsLiked.setOnClickListener(this);
        activityMainBinding.tvIsDisliked.setOnClickListener(this);
        mainViewModel.getMovieInfoLiveData().observe(this, movieInfoObserver);
        mainViewModel.getIsRatedLiveData().observe(this, isRatedObserver);


    }

    final Observer<Boolean> isRatedObserver = new Observer<Boolean>() {
        @Override
        public void onChanged(Boolean aBoolean) {
            if(aBoolean) {
                onSearchBtnClick();
                mainViewModel.setIsRatedLiveData(false);
            }
        }
    };

    final Observer<MovieInfo> movieInfoObserver = new Observer<MovieInfo>() {
        @Override
        public void onChanged(MovieInfo movieInfo) {
            if(movieInfo!=null && movieInfo.getResponse().equals("True")) {
                activityMainBinding.tvTitle.setText(movieInfo.getTitle());
                activityMainBinding.tvYear.setText("Year : " + movieInfo.getYear());
                activityMainBinding.tvDesc.setText(movieInfo.getDesc());
                activityMainBinding.tvDuration.setText("Duration : " + movieInfo.getDuration());
                activityMainBinding.tvRating.setText("Rating : " + movieInfo.getRating());
                activityMainBinding.tvReleased.setText("Released : " + movieInfo.getReleaseYear());
                liked = movieInfo.getTotalLike();
                disliked = movieInfo.getTotalDislike();
                activityMainBinding.tvIsLiked.setText("Like : " + liked);
                activityMainBinding.tvIsDisliked.setText("Dislike : " + disliked);
                if(movieInfo.getPoster()!=null && !movieInfo.getPoster().equals("N/A")){
                    Handler postHandler = new Handler(Looper.getMainLooper());
                    Executors.newSingleThreadExecutor().execute(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                URL url = new URL(movieInfo.getPoster());
                                Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());

                                postHandler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        activityMainBinding.imgPoster.setImageBitmap(bmp);

                                    }
                                });
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                } else {
                    activityMainBinding.imgPoster.setImageBitmap(null);
                }



                if(movieInfo.isLikedByYou()){
                    activityMainBinding.tvIsLiked.setTextColor(BLUE);
                } else {
                    activityMainBinding.tvIsLiked.setTextColor(WHITE);
                }

                if(movieInfo.isDislikedByYou()){
                    activityMainBinding.tvIsDisliked.setTextColor(BLUE);
                } else {
                    activityMainBinding.tvIsDisliked.setTextColor(WHITE);
                }
            }
        }
    };

    @Override
    public void onClick(View view) {
        System.out.println("clicked : " + view.getId());
        switch (view.getId()) {
            case R.id.btn_search:
                onSearchBtnClick();
                break;
            case R.id.tv_isLiked:
                if(mainViewModel.getMovieInfoLiveData().getValue()!=null){
                    Rating rating = new Rating();
                    rating.setRateBy(mainViewModel.getUsers().getValue().get(0).getUsername());
                    rating.setRateType(true);
                    rating.setMovieId(mainViewModel.getMovieInfoLiveData().getValue().getMovieId());
                    mainViewModel.getHttpService().rating("rating", rating);

                }
                break;
            case R.id.tv_isDisliked:
                if(mainViewModel.getMovieInfoLiveData().getValue()!=null){
                    Rating rating = new Rating();
                    rating.setRateBy(mainViewModel.getUsers().getValue().get(0).getUsername());
                    rating.setRateType(false);
                    rating.setMovieId(mainViewModel.getMovieInfoLiveData().getValue().getMovieId());
                    mainViewModel.getHttpService().rating("rating", rating);

                }
                break;
        }
    }


    public void onSearchBtnClick(){
        if(activityMainBinding.edtTitle.getText().length()>0){
            //do search
            if(activityMainBinding.edtYear.getText().length()>0){
                mainViewModel.getHttpService().getSearchMovieByTitleAndYear("getSearchMovieByTitleAndYear", mainViewModel.getUsers().getValue().get(0).getUsername(), activityMainBinding.edtTitle.getText().toString(), activityMainBinding.edtYear.getText().toString());
            } else {
                mainViewModel.getHttpService().getSearchMovieByTitle("getSearchMovieByTitle", mainViewModel.getUsers().getValue().get(0).getUsername(), activityMainBinding.edtTitle.getText().toString());
            }
        }
    }

}