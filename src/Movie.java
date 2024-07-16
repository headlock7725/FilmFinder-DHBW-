import java.util.ArrayList;
import java.util.List;
import java.util.Date;

public class Movie {
    private int id;
    private String title;
    private String plot;
    private String genre;
    private Date releaseDate;

    private List<String> actors = new ArrayList<>();
    private List<String> directors = new ArrayList<>();

    private double rating; //optional
    private int votes;
    private boolean isRatingSet = false;

    public Movie(int id, String title, String plot, String genre, Date releaseDate){
        this.id = id;
        this.plot = plot;
        this.title = title;
        this.genre = genre;
        this.releaseDate = releaseDate;
    }

    public void addActor(String actor){
        this.actors.add(actor);
        return;
    }

    public void addDirector(String director){
        this.directors.add(director);
        return;
    }

    public void setRating(double rating, int votes){
        this.rating = rating;
        this.votes = votes;
        this.isRatingSet = true;
    }

    public double getRating() {
        if (isRatingSet){
            return rating;
        }
        else{
            return -1.0;
        }
        
    }

    public int getVotes() {
        if (isRatingSet){
            return votes;
        }
        else{
            return -1;
        }
    }

    public String getTitle(){
        return this.title;
    }
}