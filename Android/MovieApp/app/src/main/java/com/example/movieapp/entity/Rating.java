package com.example.movieapp.entity;

public class Rating {
    private Integer movieId;
    private String rateBy;
    private boolean rateType;

    public Integer getMovieId() {
        return movieId;
    }

    public void setMovieId(Integer movieId) {
        this.movieId = movieId;
    }

    public String getRateBy() {
        return rateBy;
    }

    public void setRateBy(String rateBy) {
        this.rateBy = rateBy;
    }

    public boolean isRateType() {
        return rateType;
    }

    public void setRateType(boolean rateType) {
        this.rateType = rateType;
    }
}
