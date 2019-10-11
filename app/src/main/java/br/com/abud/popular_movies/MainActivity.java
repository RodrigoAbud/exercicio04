package br.com.abud.popular_movies;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ListView popularMoviesListView;
    private List<Movies> moviesList;
    private MovieAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        popularMoviesListView = findViewById(R.id.popularMoviesListView);

        moviesList = new LinkedList<>();

        adapter = new MovieAdapter(
                this, moviesList);

        popularMoviesListView.setAdapter(adapter);



        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(v -> {

            String end = getString(R.string.web_service_url, getString(R.string.api_key), getString(R.string.api_language));

            new Thread(
                    () -> {
                        try {
                            URL url = new URL(end);
                            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                            InputStream is = conn.getInputStream();
                            InputStreamReader isr = new InputStreamReader(is);
                            BufferedReader reader = new BufferedReader(isr);
                            String linha = null;
                            StringBuilder resultado = new StringBuilder();
                            while ((linha = reader.readLine()) != null){
                                resultado.append(linha);
                            }
                            reader.close();
                            conn.disconnect();
                            lidaComJSON(resultado.toString());

                        }
                        catch (MalformedURLException e){
                            e.printStackTrace();
                            runOnUiThread(() -> {
                                Toast.makeText(this,getString(R.string.invalid_url),Toast.LENGTH_SHORT).show();
                            });
                        }
                        catch (IOException e){
                            e.printStackTrace();
                            runOnUiThread(() -> {
                                Toast.makeText(this,getString(R.string.connect_error),Toast.LENGTH_SHORT).show();
                            });
                        }
                    }
            ).start();



            Log.i("meulog", end);




        });
    }

    private void lidaComJSON (String jsonTextual){
        try{
            JSONObject json = new JSONObject(jsonTextual);
            JSONArray results = json.getJSONArray("results");
            for (int i = 0; i < results.length(); i++){
                JSONObject iesimo = results.getJSONObject(i);
                String title = iesimo.getString("title");
                String overview = iesimo.getString("overview");
                String poster_path = iesimo.getString("poster_path");
                Movies m = new Movies(title, overview, poster_path);
                moviesList.add(m);
            }
            runOnUiThread(
                    () ->{
                        adapter.notifyDataSetChanged();
                    }
            );

        }
        catch (JSONException e){
            e.printStackTrace();
        }

    }
}
