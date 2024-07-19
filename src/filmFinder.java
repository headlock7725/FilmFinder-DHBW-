import java.text.SimpleDateFormat;
import java.util.List;

import classes.*;

public class filmFinder{
    public static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd"); //Date format for the whole Project
    
    public static void main(String[] args) {
        dataProcessor.importDatabase("/Users/bazzman/Projects/JavaClassDHBW/db/movieproject2024.db");
        filmSearch("Matrix");
        if (args.length == 0) {
            throw new IllegalArgumentException("No command-line arguments provided.");
        }
        else{
            dataProcessor.importDatabase(args[0]);
        }
        
    }

    public static void filmSearch(String serachString){
        List<Movie> movies = dataProcessor.getMovies();
        List<Movie> filteredMovies = movies.stream().filter(movie -> movie.getTitle().contains(serachString)).toList();

        for (Movie foundMovie : filteredMovies){
            System.out.println(String.format("-----------Found %d Entries-----------", filteredMovies.size()));
            System.out.println("ID: " + foundMovie.getId());
            System.out.println("Title: " + foundMovie.getTitle());
            System.out.println("Description: " + foundMovie.getPlot());
            System.out.println("Actors: " + foundMovie.getId());
            System.out.println("Genre: " + foundMovie.getGenre());
            System.out.println("Release Date: " + sdf.format(foundMovie.getReleaseDate()));
        }
    }

    
}