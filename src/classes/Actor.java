package classes;

import java.util.List;

public class Actor extends Person{

    public Actor(int id, String name) {
        super(id, name);
    }

    public List<Movie> getMovies() {
        return movies;
    }
}
