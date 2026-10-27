package org.example.ecloset.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.ecloset.enums.Color;
import org.example.ecloset.enums.Occasion;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "outfit")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Outfit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "outfit_id")
    private Integer outfitId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "season_id")
    private Season season;

    @Enumerated(EnumType.STRING)
    @Column(name = "color", length = 50)
    private Color color;

    @Enumerated(EnumType.STRING)
    @Column(name = "occasion", length = 50)
    private Occasion occasion;

    @Column(name = "date_added", nullable = false)
    private LocalDate dateAdded;

    @Column(name = "times_worn")
    @Builder.Default
    private Integer timesWorn = 0;

    @Column(name = "unavailable", nullable = false)
    @Builder.Default
    private Boolean unavailable = false;

    @Column(name = "image_url", nullable = false, length = 500)
    private String imageUrl;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "closet",
            joinColumns = @JoinColumn(name = "outfit_id"),
            inverseJoinColumns = @JoinColumn(name = "item_id")
    )
    @Builder.Default
    private Set<Item> items = new HashSet<>();

    @PrePersist
    protected void onCreate() {
        if (dateAdded == null) {
            dateAdded = LocalDate.now();
        }
        if (timesWorn == null) {
            timesWorn = 0;
        }
        if (unavailable == null) {
            unavailable = false;
        }
    }
}