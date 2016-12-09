package com.example.yongjili.popmovies;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by YongjiLi on 11/1/15.
 */

public class MainActivityFragment extends android.support.v4.app.Fragment {

    private ConnectivityManager mConnectivityManager;
    List<PopMovie> fetchedMovies = new ArrayList<>();
    PopMovieAdapter popMovieAdapter;


    public MainActivityFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        mConnectivityManager = (ConnectivityManager) getActivity().getApplicationContext().
                getSystemService(Context.CONNECTIVITY_SERVICE);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
        inflater.inflate(R.menu.menu_main, menu);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        List<PopMovie> newMovie = new ArrayList<>();

        popMovieAdapter = new PopMovieAdapter(getActivity(),R.layout.grid_item_info, newMovie);
        //popMovieAdapter = new PopMovieAdapter(getActivity(), Arrays.asList(popMovies));

        View rootView = inflater.inflate(R.layout.content_main, container, false);

        // Get a reference to the ListView, and attach this adapter to it.
        GridView gridView = (GridView) rootView.findViewById(R.id.gridView);
        gridView.setAdapter(popMovieAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long l) {
                PopMovie popMovie = popMovieAdapter.getItem(position);

                Intent intent = new Intent(getActivity(), DetailActivity.class);
                intent.putExtra("title", popMovie.getTitle());
                intent.putExtra("posterPath", popMovie.getPoster_path());
                intent.putExtra("overview", popMovie.getOverview());
                intent.putExtra("voteAverage", popMovie.getVote_average());
                intent.putExtra("releaseDate", popMovie.getRelease_date());
                startActivity(intent);
            }
        });

        return rootView;
    }

    private void updateMovies() {
        if (!com.example.yongjili.popmovies.helpers.NetworkHelper.hasInternetConnection(mConnectivityManager)) {
            Toast.makeText(getActivity().getApplicationContext(),
                    R.string.toast_internet_connection_required,
                    Toast.LENGTH_LONG).show();
        } else {
            FetchPopMovieTask popMovieTask = new FetchPopMovieTask();
            popMovieTask.execute();
        }

    }

    @Override
    public void onStart() {
        super.onStart();
        updateMovies();
    }

    //Async Task
    public class FetchPopMovieTask extends AsyncTask<String, Void, List<PopMovie>> {

        private final String LOG_TAG = FetchPopMovieTask.class.getSimpleName();

        private List<PopMovie> getMovieDataFromJson(String PopMovieJsonStr)
                            throws JSONException{
            //API names in JSON objects
            final String TMDB_RESULTS = "results";
            final String TMDB_TITLE = "title";
            final String TMDB_POSTER_PATH = "poster_path";
            final String TMDB_OVERVIEW = "overview";
            final String TMDB_VOTE_AVERAGE = "vote_average";
            final String TMDB_RELEASE_DATE = "release_date";

            final String TMDB_BASE_URL = "http://image.tmdb.org/t/p/";
            final String TMDB_IMAGE_SIZE = "w185/";

            JSONObject completeObject = new JSONObject(PopMovieJsonStr);
            JSONArray resultsArray = completeObject.getJSONArray(TMDB_RESULTS);

            //Go through each item
            for (int i = 0 ; i < resultsArray.length() ; i++ )
            {
                String title;
                String poster_path;
                String overview;
                String vote_average;
                String release_date;

                //Find the current movie
                JSONObject currentMovie = resultsArray.getJSONObject(i);

                //getString
                title = currentMovie.getString(TMDB_TITLE);
                poster_path = currentMovie.getString(TMDB_POSTER_PATH);
                overview = currentMovie.getString(TMDB_OVERVIEW);
                vote_average = currentMovie.getString(TMDB_VOTE_AVERAGE);
                release_date = currentMovie.getString(TMDB_RELEASE_DATE);

                fetchedMovies.add(new PopMovie(title, TMDB_BASE_URL + TMDB_IMAGE_SIZE + poster_path, overview, vote_average,release_date));

            }

            return fetchedMovies;
        }

        //Make connection and get JSON
        @Override
        protected List<PopMovie> doInBackground(String... params) {

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            String PopMovieJsonStr = null;

            //Get preference
            SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getActivity());
            String sort = pref.getString(getString(R.string.pref_sort_by_key), getString(R.string.pref_sort_by_default));

            fetchedMovies.clear();

            //Values for URI
            String key = getString(R.string.api_key);

            try {
                //Base URL and keys for URI
                final String BASE_URL = "http://api.themoviedb.org/3/discover/movie?";
                final String SORT_BY = "sort_by";
                final String API_KEY = "api_key";

                //Build URI
                Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                        .appendQueryParameter(SORT_BY, sort)
                        .appendQueryParameter(API_KEY, key)
                        .build();

                URL url = new URL(builtUri.toString());

                //Create connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                //Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    //Nothing to do
                    PopMovieJsonStr = null;
                }

                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    //Steam was empty. No need to parse.
                    PopMovieJsonStr = null;
                }

                PopMovieJsonStr = buffer.toString();

            } catch (Exception e) {
                Log.e(LOG_TAG, "Error", e);
                PopMovieJsonStr = null;

            } finally {
                //Close connection
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e)
                    {
                        Log.e(LOG_TAG, "Error closing stream.", e);
                    }
                }
            }

            //Make call to get JSON
            try {
                return getMovieDataFromJson(PopMovieJsonStr);
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }

            //Only occurs when there was is error getting or parsing the forecast
            return null;
        }

        @Override
        protected void onPostExecute(List<PopMovie> result) {
            if (result != null) {

                //Clear current movies adapter
                popMovieAdapter.clear();

                //Add each movie from JSON
                for (PopMovie item : result) {
                    popMovieAdapter.add(item);
                }

            }
        }
    }
}