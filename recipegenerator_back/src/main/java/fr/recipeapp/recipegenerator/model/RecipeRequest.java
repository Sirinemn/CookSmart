package fr.recipeapp.recipegenerator.model;

import java.util.List;

public class RecipeRequest {
    private List<String> ingredients;
    public String dietaryRestrictions;
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
        return dietaryRestrictions;
    }
    public void setDietaryResstrictions(String dietaryResstrictions) {
        this.dietaryRestrictions = dietaryResstrictions;
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
    @Override
    public String toString() {
        return "RecipeRequest{" +
                "ingredients=" + ingredients +
                ", dietaryRestrictions='" + dietaryRestrictions + '\'' +
                ", cuisine='" + cuisine + '\'' +
                ", servings=" + servings +
                '}';
    }
}
