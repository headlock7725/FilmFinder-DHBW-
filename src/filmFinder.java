import java.text.SimpleDateFormat;
import java.util.List;
import java.util.stream.Collectors;

import classes.*;

public class filmFinder{
    public static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd"); //Date format for the whole Project
    
    public static void main(String[] args) {
        dataProcessor.importDatabase("/Users/bazzman/Projects/JavaClassDHBW/db/movieproject2024.db");
        actorSearch("Smith");
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
        System.out.println(String.format("-----------Found %d Entries-----------", filteredMovies.size()));

        for (Movie foundMovie : filteredMovies){
            System.out.println("ID: " + foundMovie.getId());
            System.out.println("Title: " + foundMovie.getTitle());
            System.out.println("Description: " + foundMovie.getPlot());

            System.out.println("Actors: " + foundMovie.getActors().stream()
                .map(actor -> actor.getName())
                .collect(Collectors.joining(",")));

            System.out.println("Director(s): " + foundMovie.getDirectors().stream()
                .map(actor -> actor.getName())
                .collect(Collectors.joining(",")));

            System.out.println("Genre: " + foundMovie.getGenre());
            System.out.println("Release Date: " + sdf.format(foundMovie.getReleaseDate()));

            if (foundMovie.isRatingSet()){
                System.out.println("IMDB Rating: " + foundMovie.getRating());
                System.out.println("IMDB Votes: " + foundMovie.getVotes());
            }

            System.out.println(String.format("-------------------------------------", filteredMovies.size()));
        }
    }

    public static void actorSearch(String serachString){
        List<Actor> actors = dataProcessor.getActors();
        List<Actor> filteredActors = actors.stream().filter(actor -> actor.getName().contains(serachString)).toList();
        System.out.println(String.format("-----------Found %d Entries-----------", filteredActors.size()));

        for (Actor foundActor : filteredActors){
            System.out.println("ID: " + foundActor.getId());
            System.out.println("Name: " + foundActor.getName());
            System.out.println("Movies: " + foundActor.getMovies().stream()
                .map(movie -> movie.toString())
                .collect(Collectors.joining("|")));
            System.out.println(String.format("-------------------------------------", filteredActors.size()));
        }
    }

    
}