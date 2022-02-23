package org.example.movieservice.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Builder
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "rating")
public class Rating {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private Integer movieId;
    private String rateBy;
    //true = like, false = dislike
    private boolean rateType;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

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
