package com.antonio.popmovapp;

import java.io.Serializable;

/**
 * Created by Antonio on 27/09/2016.
 */
public class MovieListStore implements Serializable {

    private String title, overview, posterPath, date;
    private double rate;

    MovieListStore (String titulo, String sinopsis, String posterUrl, String fecha, double rateAverage ){
        title=titulo;
        overview=sinopsis;
        posterPath=posterUrl;
        date=fecha;
        rate=rateAverage;
    }
    public String getPosterPath() {
        return posterPath;
    }
    public String getTitle() {
        return title;
    }
    public String getOverview() {
        return overview;
    }
    public String getDate() {
        return date;
    }
    public double getRate() {
        return rate;
    }
}
