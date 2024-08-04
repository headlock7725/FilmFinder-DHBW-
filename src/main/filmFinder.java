package main;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import classes.*;

public class filmFinder{
    public static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd"); //Date format for the whole Project
    
    public static void main(String[] args) {
        if (args.length == 0) {
            throw new IllegalArgumentException("No command-line arguments provided.");
        }
        else{
            dataProcessor.importDatabase("/Users/bazzman/Projects/JavaClassDHBW/db/movieproject2024.db"); //TODO: CHANGE PATH

            for (String arg : args){
                System.err.println(arg);
                String[] request = arg.replace("\"", "").split("=");
                if (arg.contains("filmsuche")){
                    filmSearch(request[1]);
                }
                else if (arg.contains("schauspielersuche")){
                    actorSearch(request[1]);
                }
                else if (arg.contains("schauspielernetzwerk")){
                    actorNetwork(Integer.parseInt(request[1]));
                }
                else if (arg.contains("filmnetzwerk")){
                    movieNetwork(Integer.parseInt(request[1]));
                }
            }
        }
        System.out.println("DONE");
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

    public static void movieNetwork(int searchID){
        List<Movie> movies = dataProcessor.getMovies();
        Movie foundMovie = movies.stream().filter(movie -> movie.getId() == searchID).findFirst().orElse(null);

        if (foundMovie == null){
            System.out.println("No movie with this ID found - " + searchID);
            return;
        }

        List<Actor> allActors = foundMovie.getActors();
        List<Movie> allMovies = new ArrayList<>();

        for (Actor actor : allActors){
            allMovies.addAll(
                actor.getMovies().stream()
                .filter(movie -> !allMovies.contains(movie))
                .toList()
            );
        }

        System.out.println("Filme: " + allMovies.stream()
            .map(movie -> movie.toString())
            .collect(Collectors.joining(", "))
        );

        System.out.println("Schauspieler: " + allActors.stream()
            .map(actor -> actor.getName())
            .collect(Collectors.joining(", "))
        );
    }

    public static void actorNetwork(int searchID){
        List<Actor> actors = dataProcessor.getActors();
        Actor foundActor = actors.stream().filter(actor -> actor.getId() == searchID).findFirst().orElse(null);

        if (foundActor == null){
            System.out.println("No actor with this ID found - " + searchID);
            return;
        }
        
        List<Actor> allActors = new ArrayList<>();
        List<Movie> allMovies = foundActor.getMovies();

        for (Movie actorMovie : allMovies){
            allActors.addAll(
                actorMovie.getActors().stream()
                .filter(actor -> !allActors.contains(actor)).toList()
            );
        }

        System.out.println("Filme: " + allMovies.stream()
            .map(movie -> movie.toString())
            .collect(Collectors.joining(", "))
        );

        System.out.println("Schauspieler: " + allActors.stream()
            .map(actor -> actor.getName())
            .collect(Collectors.joining(", "))
        );
    }

    
}