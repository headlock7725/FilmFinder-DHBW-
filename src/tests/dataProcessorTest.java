package tests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.stream.Collectors;

import main.dataProcessor;
import classes.*;

public class dataProcessorTest {

    @BeforeEach
    public void setUp() {
        dataProcessor.importDatabase("/Users/bazzman/Projects/JavaClassDHBW/db/movieproject2024.db"); //TODO: CHANGE PATH
    }

    @Test
    public void testImportActors() {
        List<Actor> actors = dataProcessor.getActors();
        assertNotNull(actors, "Actors list should not be null");
        assertFalse(actors.isEmpty(), "Actors list should not be empty");
        
        Actor firstActor = actors.get(0);
        assertEquals(9816, firstActor.getId(), "First actor ID should be 9816");
        assertEquals("François Lallement", firstActor.getName(), "First actor name should be François Lallement");

        Movie firstMovie = firstActor.getMovies().get(0);
        assertEquals(6138, firstMovie.getId(), "The only movie of 9816 should be 6138");
    }

    @Test
    public void testImportMovies(){
        List<Movie> testMovie = dataProcessor.getMovies().stream()
        .filter(movie -> movie.getId() == 1359)
        .collect(Collectors.toList());

        assertFalse(testMovie.isEmpty(), "Found Movies should not be empty");
        assertEquals(1, testMovie.size(), "There should be only one element found");
        assertEquals("Action", testMovie.getFirst().getGenre(), "Only the first occurance of a entity should be imported");
    }

    @Test
    public void testImportDirectors(){
        List<Director> directors = dataProcessor.getDirectors();
        assertNotNull(directors, "Directors list should not be null");
        assertFalse(directors.isEmpty(), "Directors list should not be empty");

        Director firstDirector = directors.get(0);
        assertEquals(27975, firstDirector.getId(), "First director ID should be 27975");
        assertEquals("Georges Méliès", firstDirector.getName(), "First director name should be Georges Méliès");
    }
} 
