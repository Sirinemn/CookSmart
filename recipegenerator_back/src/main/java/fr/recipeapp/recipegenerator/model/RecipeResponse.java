package fr.recipeapp.recipegenerator.model;

import java.util.List;

public class RecipeResponse {
    private String recipeName;
    private String description;
    private List<String> ingredients;
    private List<String> instructions;
    private String cookingTime;
    private String difficulty;
    private Integer servings;
    private List<String> missingIngredients;
    private List<String> alternativeIngredients;
    private String tips;

    public RecipeResponse(String recipeName, String description) {
        this.recipeName = recipeName;
        this.description = description;
    }
    public RecipeResponse() {
    }
    // Getters et Setters
    public String getRecipeName() {
        return recipeName;
    }

    public void setRecipeName(String recipeName) {
        this.recipeName = recipeName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<String> ingredients) {
        this.ingredients = ingredients;
    }

    public List<String> getInstructions() {
        return instructions;
    }

    public void setInstructions(List<String> instructions) {
        this.instructions = instructions;
    }

    public String getCookingTime() {
        return cookingTime;
    }

    public void setCookingTime(String cookingTime) {
        this.cookingTime = cookingTime;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public Integer getServings() {
        return servings;
    }

    public void setServings(Integer servings) {
        this.servings = servings;
    }

    public List<String> getMissingIngredients() {
        return missingIngredients;
    }

    public void setMissingIngredients(List<String> missingIngredients) {
        this.missingIngredients = missingIngredients;
    }

    public List<String> getAlternativeIngredients() {
        return alternativeIngredients;
    }

    public void setAlternativeIngredients(List<String> alternativeIngredients) {
        this.alternativeIngredients = alternativeIngredients;
    }

    public String getTips() {
        return tips;
    }

    public void setTips(String tips) {
        this.tips = tips;
    }

    @Override
    public String toString() {
        return "RecipeResponse{" +
                "recipeName='" + recipeName + '\'' +
                ", description='" + description + '\'' +
                ", ingredients=" + ingredients +
                ", instructions=" + instructions +
                ", cookingTime='" + cookingTime + '\'' +
                ", difficulty='" + difficulty + '\'' +
                ", servings=" + servings +
                ", missingIngredients=" + missingIngredients +
                ", alternativeIngredients=" + alternativeIngredients +
                ", tips='" + tips + '\'' +
                '}';
    }
}
