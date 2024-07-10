package com.zion.bean;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "recipes")
public class Recipe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long recipeId;
    @Column(nullable = false)
    private String recipeName;
    @Column(nullable = false)
    private String recipeInstructions;
    private double recipeCost;
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "fk_recipe", referencedColumnName = "recipeId")
    private List<Ingredient> recipeIngredients;

}
