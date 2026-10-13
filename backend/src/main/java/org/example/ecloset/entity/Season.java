package org.example.ecloset.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "season")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Season {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "season_id")
    private Integer seasonId;

    @Column(name = "name", nullable = false, unique = true, length = 50)
    private String name;

    @Column(name = "min_temp")
    private Integer minTemp;

    @Column(name = "max_temp")
    private Integer maxTemp;

    @OneToMany(mappedBy = "season")
    @Builder.Default
    private List<Item> items = new ArrayList<>();
}