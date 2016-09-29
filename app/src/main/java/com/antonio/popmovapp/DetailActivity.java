package com.antonio.popmovapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.container, new PlaceholderFragment())
                .commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity (new Intent(this,SettingsActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        private static final String LOG_TAG = PlaceholderFragment.class.getSimpleName();
        private ArrayList<MovieListStore> movieInfo = new ArrayList<>();

        public PlaceholderFragment() {
            setHasOptionsMenu(true);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

            movieInfo.addAll((ArrayList<MovieListStore>)getActivity().getIntent().getSerializableExtra("array"));
            //Cargamos titulo original
            TextView title = (TextView) rootView.findViewById(R.id.textTitle);
            title.setText(movieInfo.get(0).getTitle());
            //Cargamos imagen del poster
            ImageView poster = (ImageView) rootView.findViewById(R.id.imagePoster);
            Uri builtUri= Uri.parse("http://image.tmdb.org/t/p/"+"w500/"+movieInfo.get(0).getPosterPath()).buildUpon()
                    .build();
            Picasso.with(getActivity()).load(builtUri).into(poster);
            //Cargamos fecha
            TextView date = (TextView) rootView.findViewById(R.id.textDate);
            date.setText(movieInfo.get(0).getDate());
            //Caargamos puntuacion
            TextView rate = (TextView) rootView.findViewById(R.id.textRate);
            rate.setText(String.valueOf(movieInfo.get(0).getRate()));
            //Cargamos sinopsis
            TextView textoDescriptivo = (TextView) rootView.findViewById(R.id.textReview);
            textoDescriptivo.setText(movieInfo.get(0).getOverview());

            return rootView;
        }
    }
}