package fr.recipeapp.recipegenerator.model;

import java.util.List;

public class RecipeRequest {
    private List<String> ingredients;
    public String dietaryResstrictions;
    private String cuisine;
    private Integer servings;

    public RecipeRequest(){}
    public RecipeRequest(List<String> ingredients) {
        this.ingredients = ingredients;
    }

    public List<String> getIngredients() {
        return ingredients;
    }
    public void setIngredients(List<String> ingredients) {
        this.ingredients = ingredients;
    }
    public String getDietaryResstrictions() {
        return dietaryResstrictions;
    }
    public void setDietaryResstrictions(String dietaryResstrictions) {
        this.dietaryResstrictions = dietaryResstrictions;
    }
    public String getCuisine() {
        return cuisine;
    }
    public void setCuisine(String cuisine) {
        this.cuisine = cuisine;
    }
    public Integer getServings() {
        return servings;
    }
    public void setServings(Integer servings) {
        this.servings = servings;
    }
    
}
