package br.com.abud.popular_movies;

import java.util.Locale;

public class Movies {

    public final String movieTitle;
    public final String movieOverview;
    public final String posterPathURL;

    public Movies (
            String movieTitle,
            String movieOverview,
            String moviePoster
    ){
        this.movieTitle = movieTitle;
        this.movieOverview = movieOverview;
        this.posterPathURL = String.format(Locale.getDefault(),
                "https://image.tmdb.org/t/p/w500%s", moviePoster);
    }
}
