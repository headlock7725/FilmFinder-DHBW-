import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class filmFinder{

    private static List<Actor> actors = new ArrayList<>();
    private static List<Movie> movies = new ArrayList<>();
    
    public static void main(String[] args) {
        importDatabase("/Users/bazzman/Projects/JavaClassDHBW/db/movieproject2024.db.txt");
        if (args.length == 0) {
            throw new IllegalArgumentException("No command-line arguments provided.");
        }
        else{
            importDatabase(args[0]);
        }
        
    }


    public static void importDatabase(String filepath){
        int entityIndex = -1;//entityIndex to differentiate between different parts of the database (0 - actors, 1 - movies ...)

        try {
            List<String> lines = Files.readAllLines(Paths.get(filepath), StandardCharsets.UTF_8);
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
                catch(NumberFormatException e) {
                    System.out.println(String.format("Invalid integer format LINE: \n%s", line));
                    continue;
                }

                switch (entityIndex) {
                    case 0:
                        //check if actor already exists via stream and lambda function
                        boolean actorAlreadyExists = actors.stream().anyMatch(actor -> actor.getName() == entityData.get(1));
                        if (!actorAlreadyExists){
                            actors.add(new Actor(entityId, entityData.get(1)));
                        }
                        break;
                    case 1:
                        Date releaseDate = null;
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd");
                        try{
                            releaseDate = sdf.parse(entityData.get(4));
                        }
                        catch(ParseException e){
                            System.out.println("Invalid date format MovieID: " + entityId + "\n" + entityData.get(4));
                            continue;
                        }
                        
                        boolean movieAlreadyExists = movies.stream().anyMatch(movie -> movie.getTitle() == entityData.get(1));
                        if (!movieAlreadyExists){
                            Movie newMovie = new Movie(entityId, entityData.get(1), entityData.get(2), entityData.get(3), releaseDate);
                            movies.add(newMovie);
                        }
                        break;
                    default:
                        break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}