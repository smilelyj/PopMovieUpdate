package com.example.yongjili.popmovies;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by YongjiLi on 11/1/15.
 */

public class PopMovieAdapter extends ArrayAdapter<PopMovie> {
   // private static final String LOG_TAG = PopMovieAdapter.class.getSimpleName();

    private Context context;
    int layoutResourceId;
    List<PopMovie> popMovies = new ArrayList<>();

    public PopMovieAdapter(Context context, int layoutResourceId, List<PopMovie> popMovies) {
        // Here, we initialize the ArrayAdapter's internal storage for the context and the list.
        // the second argument is used when the ArrayAdapter is populating a single TextView.
        // Because this is a custom adapter for two TextViews and an ImageView, the adapter is not
        // going to use this second argument, so it can be any value. Here, we used 0.
        super(context, layoutResourceId, popMovies);
        this.context = context;
        this.layoutResourceId = layoutResourceId;
        this.popMovies = popMovies;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Gets the AndroidFlavor object from the ArrayAdapter at the appropriate position
        PopMovie popMovie = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(layoutResourceId, parent, false);
        }

        ImageView imageView = (ImageView) convertView.findViewById(R.id.movie_image);
        //imageView.setImageResource(popMovie.image);

        Picasso.with(context).load(popMovie.getPoster_path()).into(imageView);

        //TextView movieTitle = (TextView) convertView.findViewById(R.id.movie_text);
       // movieTitle.setText(popMovie.title);
        return convertView;
    }
}
