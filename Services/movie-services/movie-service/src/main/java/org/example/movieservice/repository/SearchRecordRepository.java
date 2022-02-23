package org.example.movieservice.repository;

import org.example.movieservice.entity.SearchRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;
import java.util.Optional;

public interface SearchRecordRepository extends JpaRepository<SearchRecord, Integer> {
    @Transactional
    Optional<SearchRecord> findSearchRecordByTitleAndYear(String title, String year);
    @Transactional
    void deleteSearchRecordByMovieId(Integer movieId);
}
