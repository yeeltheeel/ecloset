package org.example.ecloset.repository;

import org.example.ecloset.entity.Season;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SeasonRepository extends JpaRepository<Season, Integer> {

    Optional<Season> findByName(String name);

    @Query("SELECT s FROM Season s WHERE s.minTemp <= :temp AND s.maxTemp >= :temp")
    List<Season> findSeasonsByTemperature(@Param("temp") Integer temp);
}