package com.example.movieapp.entity;


public class MovieInfo {
    private Integer movieId;
    private String title;
    private String year;
    private String releaseYear;
    private String duration;
    private String rating;
    private String poster;
    private String desc;
    private int totalLike;
    private int totalDislike;
    private boolean likedByYou;
    private boolean dislikedByYou;
    private String response;


    public Integer getMovieId() {
        return movieId;
    }

    public void setMovieId(Integer movieId) {
        this.movieId = movieId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(String releaseYear) {
        this.releaseYear = releaseYear;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getTotalLike() {
        return totalLike;
    }

    public void setTotalLike(int totalLike) {
        this.totalLike = totalLike;
    }

    public int getTotalDislike() {
        return totalDislike;
    }

    public void setTotalDislike(int totalDislike) {
        this.totalDislike = totalDislike;
    }

    public boolean isLikedByYou() {
        return likedByYou;
    }

    public void setLikedByYou(boolean likedByYou) {
        this.likedByYou = likedByYou;
    }

    public boolean isDislikedByYou() {
        return dislikedByYou;
    }

    public void setDislikedByYou(boolean dislikedByYou) {
        this.dislikedByYou = dislikedByYou;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }
}
