package org.example.ecloset.repository;

import org.example.ecloset.entity.Outfit;
import org.example.ecloset.entity.Season;
import org.example.ecloset.enums.Color;
import org.example.ecloset.enums.Occasion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OutfitRepository extends JpaRepository<Outfit, Integer> {

    List<Outfit> findByUser_UserId(Integer userId);

    List<Outfit> findByItems_ItemId(Integer itemId);

    List<Outfit> findByUser_UserIdAndSeasonIn(Integer userId, List<Season> seasons);

    List<Outfit> findByUser_UserIdAndTimesWorn(Integer userId, Integer timesWorn);

    List<Outfit> findByUser_UserIdAndColor(Integer userId, Color color);

    List<Outfit> findByUser_UserIdAndOccasion(Integer userId, Occasion occasion);

    @Modifying
    @Query("UPDATE Outfit o SET o.timesWorn = o.timesWorn + 1 " +
            "WHERE o.outfitId = :outfitId AND o.user.userId = :userId")
    int incrementTimesWorn(@Param("outfitId") Integer outfitId, @Param("userId") Integer userId);

    @Modifying
    @Query("UPDATE Outfit o SET o.unavailable = true " +
            "WHERE o.outfitId = :outfitId")
    void markAsUnavailable(@Param("outfitId") Integer outfitId);

    List<Outfit> findByUser_UserIdAndUnavailableTrue(Integer userId);
}