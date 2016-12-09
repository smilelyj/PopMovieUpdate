package com.example.yongjili.popmovies;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.support.v4.app.Fragment;

import com.squareup.picasso.Picasso;

/**
 * Created by YongjiLi on 11/1/15.
 */
public class DetailActivityFragment extends Fragment{

    public DetailActivityFragment() {
    }

    //@TargetApi(Build.VERSION_CODES.M)
    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

        //Get intent
        Intent intent = getActivity().getIntent();
        String title = intent.getStringExtra("title");
        String posterPath = intent.getStringExtra("posterPath");
        String overview = intent.getStringExtra("overview");
        String voteAverage = intent.getStringExtra("voteAverage");
        String releaseDate = intent.getStringExtra("releaseDate");

        //Connect the textview with layout id.
        TextView tvTitle = (TextView) rootView.findViewById(R.id.title);
        TextView tvReleaseDate = (TextView) rootView.findViewById(R.id.release_date);
        TextView tvPosterPath = (TextView) rootView.findViewById(R.id.thumbnail);
        ImageView ivPoster = (ImageView) rootView.findViewById(R.id.poster_image);
        TextView tvVoteAverage = (TextView) rootView.findViewById(R.id.vote_average);
        TextView tvOverview = (TextView) rootView.findViewById(R.id.plot_synopsis);

        //set text
        tvTitle.setText(title);
        tvReleaseDate.setText(releaseDate);
        tvPosterPath.setText(posterPath);
        tvVoteAverage.setText(voteAverage);
        tvOverview.setText(overview);

        Picasso.with(getContext()).load(posterPath).into(ivPoster);

        return rootView;
    }
}
