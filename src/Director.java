import java.util.ArrayList;
import java.util.List;

public class Director {
    private int id;
    private String name;
    private List<Movie> movies = new ArrayList<>();

    public Director(int id, String name){
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void addMovie(Movie newMovie){
        movies.add(newMovie);
    }
    
}
