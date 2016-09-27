package com.antonio.popmovapp;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class GridMoviesAdapter extends BaseAdapter {

    Context contexto;
    int layoutImage;
    int layoutText;
    int layout;
    ArrayList<MovieListStore> movieTitles = new ArrayList<>();

    public GridMoviesAdapter(Context ctx, ArrayList<MovieListStore> movieTitlesList) {
        contexto=ctx;
        movieTitles=movieTitlesList;
    }

    public GridMoviesAdapter(Context ctx, ArrayList<MovieListStore> movieTitlesList, int layoutR, int layoutIdText, int layoutIdImage) {
        contexto=ctx;
        movieTitles=movieTitlesList;
        layoutImage= layoutIdImage;
        layoutText = layoutIdText;
        layout = layoutR;
    }

    @Override
    public int getCount() {
        return movieTitles.size();
    }

    @Override
    public Object getItem(int i) {
        return movieTitles.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View movieView;

        if(view==null){
            movieView= View.inflate(contexto,layout,null);
        }else{
            movieView = (View)view;
        }
        ImageView movieImageView = (ImageView) movieView.findViewById(layoutImage);

        Uri builtUri= Uri.parse("http://image.tmdb.org/t/p/"+"w500/"+movieTitles.get(i).getPosterPath()).buildUpon()
                .build();

        Picasso.with(contexto).load(builtUri).into(movieImageView);
        return movieView;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }
}
