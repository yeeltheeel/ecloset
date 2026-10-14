package org.example.ecloset.repository;

import org.example.ecloset.entity.Item;
import org.example.ecloset.entity.Season;
import org.example.ecloset.enums.Category;
import org.example.ecloset.enums.Color;
import org.example.ecloset.enums.Material;
import org.example.ecloset.enums.Occasion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Integer> {

    // Все вещи пользователя
    List<Item> findByUser_UserId(Integer userId);

    // Все вещи пользователя по конкретному сезону
    List<Item> findByUser_UserIdAndSeason_SeasonId(Integer userId, Integer seasonId);

    // Все вещи пользователя по списку сезонов (для поиска по температуре)
    List<Item> findByUser_UserIdAndSeasonIn(Integer userId, List<Season> seasons);

    // Ненадетые ни разу вещи
    List<Item> findByUser_UserIdAndTimesWorn(Integer userId, Integer timesWorn);

    // Поиск по категории
    List<Item> findByUser_UserIdAndCategory(Integer userId, Category category);

    // Поиск по цвету
    List<Item> findByUser_UserIdAndColor(Integer userId, Color color);

    // Поиск по материалу
    List<Item> findByUser_UserIdAndMaterial(Integer userId, Material material);

    // Поиск по occasion
    List<Item> findByUser_UserIdAndOccasion(Integer userId, Occasion occasion);

    @Modifying
    @Query("UPDATE Item i SET i.timesWorn = i.timesWorn + 1 " +
            "WHERE i.itemId = :itemId AND i.user.userId = :userId")
    int incrementTimesWorn(@Param("itemId") Integer itemId,
                           @Param("userId") Integer userId);
}