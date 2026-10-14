package org.example.ecloset.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.ecloset.enums.Category;
import org.example.ecloset.enums.Color;
import org.example.ecloset.enums.Material;
import org.example.ecloset.enums.Occasion;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "item")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id")
    private Integer itemId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(name = "category", nullable = false, length = 50)
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "season_id")
    private Season season;

    @Enumerated(EnumType.STRING)
    @Column(name = "color", length = 50)
    private Color color;

    @Enumerated(EnumType.STRING)
    @Column(name = "material", length = 50)
    private Material material;

    @Enumerated(EnumType.STRING)
    @Column(name = "occasion", length = 50)
    private Occasion occasion;

    @Column(name = "date_added", nullable = false)
    private LocalDate dateAdded;

    @Column(name = "times_worn")
    @Builder.Default
    private Integer timesWorn = 0;

    @Column(name = "image_url", nullable = false, length = 500)
    private String imageUrl;

    @Column(name = "brand", length = 100)
    private String brand;

    @Column(name = "price", precision = 10, scale = 2)
    private BigDecimal price;

    @PrePersist
    protected void onCreate() {
        if (dateAdded == null) {
            dateAdded = LocalDate.now();
        }
    }
}