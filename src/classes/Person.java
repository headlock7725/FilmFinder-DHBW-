package classes;

import java.util.ArrayList;
import java.util.List;

abstract class Person {
    protected int id;
    protected String name;
    protected List<Movie> movies = new ArrayList<>();

    public Person(int id, String name){
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