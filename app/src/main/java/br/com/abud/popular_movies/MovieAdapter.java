package br.com.abud.popular_movies;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class MovieAdapter extends ArrayAdapter<Movies> {

    private Context context;
    private List <Movies> filmes;

    public MovieAdapter (Context context, List<Movies> filmes){
        super(context, -1, filmes);
        this.context = context;
        this.filmes = filmes;
    }

    @Override
    public int getCount () {
        return filmes.size();
    }

    public class MoviesViewHolder {
        ImageView posterPathImageView;
        TextView titleTextView;
        TextView overviewTextView;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent){
        MoviesViewHolder vh = null;
        if (convertView == null){
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.list_item,parent,false);
            vh = new MoviesViewHolder();
            vh.posterPathImageView = convertView.findViewById(R.id.posterPathImageView);
            vh.titleTextView = convertView.findViewById(R.id.titleTextView);
            vh.overviewTextView = convertView.findViewById(R.id.overviewTextView);
            convertView.setTag(vh);
        }else{
            vh = (MoviesViewHolder) convertView.getTag();
        }

        Movies filmeDaVez = filmes.get(position);

        vh.titleTextView.setText(filmeDaVez.movieTitle);
        vh.overviewTextView.setText(filmeDaVez.movieOverview);

        baixarImagem (filmeDaVez, vh.posterPathImageView);

        return convertView;

    }

    private void baixarImagem (Movies filmeDavez, ImageView posterPathImageView) {
        new Thread(() -> {
            try {
                URL url = new URL(filmeDavez.posterPathURL);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                InputStream is = conn.getInputStream();
                Bitmap figura = BitmapFactory.decodeStream(is);
                ((Activity) context).runOnUiThread(() -> {
                    posterPathImageView.setImageBitmap(figura);
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }
}
