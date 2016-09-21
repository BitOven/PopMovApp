package com.antonio.popmovapp;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;

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


public class GridMoviesFragment extends Fragment {

    private ArrayList<String> movieList = new ArrayList<>();
    private GridMoviesAdapter moviesAdapter;

    public GridMoviesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

       // movieAdapter = new ArrayAdapter<String>(getActivity(),R.layout.grid_item_movies,R.id.text_item_movies,movieList);
        View view = inflater.inflate(R.layout.fragment_grid_movies, container, false);
        GridView gridView = (GridView) view.findViewById(R.id.gridview_fragment);
        //gridView.setAdapter(movieAdapter);
        moviesAdapter= new GridMoviesAdapter(getActivity(),movieList,R.layout.grid_item_movies,R.id.text_item_movies,R.id.imageView1);
        gridView.setAdapter(moviesAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //Intent details
            }
        });
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        DownloadMovies downloadMovies = new DownloadMovies();
        downloadMovies.execute("top_rated");
    }

    public class DownloadMovies extends AsyncTask<String,Void,String[]> {
        @Override
        protected String[] doInBackground(String... params) {
            // These two need to be declared outside the try/catch
            // so that they can be closed in the finally block.
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String movieJsonStr = null;

            try {
                // Construct the URL for the OpenWeatherMap query
                // Possible parameters are avaiable at OWM's forecast API page, at
                // http://openweathermap.org/API#forecast
                final String MOVIES_BASE_URL="https://api.themoviedb.org/3/movie/"+params[0]+"?";
                final String APP_PARAM="api_key";

                Uri builtUri= Uri.parse(MOVIES_BASE_URL).buildUpon()
                        .appendQueryParameter(APP_PARAM, BuildConfig.TMDB_API_KEY)
                        .build();

                //String baseUrl = "http://api.openweathermap.org/data/2.5/forecast/daily?q=94043&mode=json&units=metric&cnt=7";
                //String apiKey = "&APPID=" + BuildConfig.OPEN_WEATHER_MAP_API_KEY;
                //URL url = new URL(baseUrl.concat(apiKey));
                URL url = new URL(builtUri.toString());
                //Log.v("uriBuilder",builtUri.toString());
                // Create the request to OpenWeatherMap, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                movieJsonStr = buffer.toString();
                //Log.v("ForecasFragment","Forecast JSON String:"+forecastJsonStr);

            } catch (IOException e) {
                Log.e("AsyncTaskDownloadMovies", "Error ", e);
                // If the code didn't successfully get the weather data, there's no point in attemping
                // to parse it.
                return null;
            } finally{
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e("AsyncTaskDownloadMovies", "Error closing stream", e);
                    }
                }
            }
            try {
                Log.d("AsyncTaskDOwnloadMovies","El json recibido es: "+movieJsonStr);
                return getMovieDataFromJson(movieJsonStr);
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String[] strings) {
            super.onPostExecute(strings);
            if(strings != null){
                movieList.clear();
                for(String moviePosterString : strings) {
                    movieList.add(moviePosterString);
                }
                moviesAdapter.notifyDataSetChanged();
            }
        }
    }
    private String[] getMovieDataFromJson(String jsonStr) throws JSONException{

        // These are the names of the JSON objects that need to be extracted.
        //final String TMDB_TITLE = "original_title";
        final String TMDB_POSTER = "poster_path";
        //final String TMDB_SYNOPSIS = "overview";
        //final String TMDB_RATE = "vote_average";
        //final String TMDB_REL_DATE = "release_date";
        final String TMDB_RESULTS = "results";

        JSONObject moviesJson = new JSONObject(jsonStr);
        JSONArray moviesArray = moviesJson.getJSONArray(TMDB_RESULTS);

        String[] posterPath= new String[moviesArray.length()];
        for (int i=0; i<moviesArray.length();i++){
            posterPath[i]= moviesArray.getJSONObject(i).getString(TMDB_POSTER);
        }

        return posterPath;
    }

}
