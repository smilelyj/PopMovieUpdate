package com.example.yongjili.popmovies;

/**
 * Created by YongjiLi on 11/1/15.
 */
public class PopMovie {
    String title;
    String posterPath;
    String overview;
    String voteAverage;
    String releaseDate;

    public PopMovie(String title, String poster_path,String overview,String vote_average,String release_date)
    {

        this.title = title;
        this.posterPath = poster_path;
        this.overview = overview;
        this.voteAverage = vote_average;
        this.releaseDate = release_date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    public String getPoster_path() {
        return posterPath;
    }

    public String getOverview() {return overview;}
    public String getVote_average() {
        return voteAverage;
    }
    public String getRelease_date() {
        return releaseDate;
    }


}
