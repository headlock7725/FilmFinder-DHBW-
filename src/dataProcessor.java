import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import classes.*;

public class dataProcessor {
    private static List<Actor> actors = new ArrayList<>();
    private static List<Movie> movies = new ArrayList<>();
    private static List<Director> directors = new ArrayList<>();

    public static void importDatabase(String filepath){
        int entityIndex = -1; //entityIndex to differentiate between different parts of the database (0 - actors, 1 - movies ... 4 - director to movie relation)
        List<String> lines = null;

        try {
            lines = Files.readAllLines(Paths.get(filepath), StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (String line : lines) {
            int entityId = -1;

            List<String> entityData = new ArrayList<>();

            if (line.contains("New_Entity")){
                entityIndex += 1;
                continue;
            }

            //remove whitespace characters in the beginning of a string and split entries by separator using regex
            Pattern pattern = Pattern.compile("(?<=\") *([^\"]*)(?=\",)");
            Matcher matcher = pattern.matcher(line + ",");
            while (matcher.find()) {
                entityData.add(matcher.group(0));
            }

            try{
                entityId = Integer.parseInt(entityData.get(0));
            }
            catch (NumberFormatException e){
                continue;
            }

            switch (entityIndex) {
                //ACTORS
                case 0:
                    processActor(entityId, entityData);
                    break;
                //MOVIES
                case 1:
                    processMovie(entityId, entityData);
                    break;
                //DIRECTORS
                case 2:
                    directors.add(new Director(entityId, entityData.get(1)));
                    break;

                //ACTOR IN MOVIE
                case 3:
                    addActorToMovie(entityData);
                    break;

                //DIRECTOR IN MOVIE
                case 4:
                    addDirectorToMovie(entityData);
                    break;

                default:
                    System.out.println("ERR: ENTITY IN DATABASE OUT OF SCOPE (>5 Entries)");
            }
        }
    }

    private static void processActor(int entityId, List<String> data){
        //check if actor already exists via stream and lambda function
        
        boolean actorAlreadyExists = actors.stream().anyMatch(actor -> actor.getName().equals(data.get(1)));
        if (!actorAlreadyExists){
            actors.add(new Actor(entityId, data.get(1)));
        }
    }

    private static void processMovie(int entityId, List<String> data){

        Date releaseDate = null;
        try{
            releaseDate = filmFinder.sdf.parse(data.get(4));
        }
        catch(ParseException e){
            //System.out.println("Invalid date format MovieID: " + entityId + "\n" + entityData.get(4));
            return;
        }
        
        boolean movieAlreadyExists = movies.stream().anyMatch(movie -> movie.getTitle().equals(data.get(1)));
        if (!movieAlreadyExists){
            Movie newMovie = new Movie(entityId, data.get(1), data.get(2), data.get(3), releaseDate);

            try{
                int imdbVotes = Integer.parseInt(data.get(5));
                double imdbRating = Double.parseDouble(data.get(6));

                newMovie.setRating(imdbRating, imdbVotes);
            }
            catch(NumberFormatException e){
                ;
            }

            movies.add(newMovie);
        }
    }

    private static void addActorToMovie(List<String> data){
        final int[] actorID = { -1 };
        final int[] movieID = { -1 };

        try{
            actorID[0] = Integer.parseInt(data.get(0));
            movieID[0] = Integer.parseInt(data.get(1));
        }
        catch (NumberFormatException e){
            return;
        }

        Actor actorEntity = actors.stream().filter(actor -> actor.getId() == actorID[0]).findFirst().orElse(null);
        Movie movieEntity = movies.stream().filter(movie -> movie.getId() == movieID[0]).findFirst().orElse(null);

        if (actorEntity != null && movieEntity != null){
            actorEntity.addMovie(movieEntity);
            movieEntity.addActor(actorEntity);
        }
    }

    private static void addDirectorToMovie(List<String> data){
        final int[] directorID = {-1};
        final int[] movieID = {-1};
        try{
            directorID[0] = Integer.parseInt(data.get(0));
            movieID[0] = Integer.parseInt(data.get(1));
        }
        catch (NumberFormatException e){
            return;
        }

        Director directorEntity = directors.stream().filter(director -> director.getId() == directorID[0]).findFirst().orElse(null);
        Movie movieEntity = movies.stream().filter(movie -> movie.getId() == movieID[0]).findFirst().orElse(null);
        
        if (directorEntity != null && movieEntity != null){
            directorEntity.addMovie(movieEntity);
            movieEntity.addDirector(directorEntity);
        }
    }

    public static List<Actor> getActors() {
        return actors;
    }

    public static List<Movie> getMovies() {
        return movies;
    }

    public static List<Director> getDirectors() {
        return directors;
    }
}
