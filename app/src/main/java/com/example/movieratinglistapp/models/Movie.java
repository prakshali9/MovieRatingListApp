package com.example.movieratinglistapp.models;

public class Movie {
    private String title;
    private String studio;
    private double rating;

    public Movie() {}

    // Constructor without thumbnail
    public Movie(String title, String studio, double rating) {
        this.title = title;
        this.studio = studio;
        this.rating = rating;
    }

    // Getters and Setters
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStudio() {
        return studio;
    }

    public void setStudio(String studio) {
        this.studio = studio;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }
}
