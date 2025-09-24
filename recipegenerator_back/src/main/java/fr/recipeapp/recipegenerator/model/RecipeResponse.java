package fr.recipeapp.recipegenerator.model;

import java.util.List;

public class RecipeResponse {
    private String title;
    private String description;
    private List<String> ingredients;
    private List<String> instructions;
    private String cookingTime;
    private String difficulty;
    private List<String> missingIngredients; // ingrédients manquants
    private List<String> alternativeRecipes; // suggestions alternatives
    private boolean canMakeWithAvailableIngredients;

    public RecipeResponse() {
    }

    public RecipeResponse(String title, String description, List<String> ingredients,
                          List<String> instructions, String cookingTime, String difficulty) {
        this.title = title;
        this.description = description;
        this.ingredients = ingredients;
        this.instructions = instructions;
        this.cookingTime = cookingTime;
        this.difficulty = difficulty;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public List<String> getMissingIngredients() {
        return missingIngredients;
    }

    public void setMissingIngredients(List<String> missingIngredients) {
        this.missingIngredients = missingIngredients;
    }

    public List<String> getAlternativeRecipes() {
        return alternativeRecipes;
    }

    public void setAlternativeRecipes(List<String> alternativeRecipes) {
        this.alternativeRecipes = alternativeRecipes;
    }

    public boolean isCanMakeWithAvailableIngredients() {
        return canMakeWithAvailableIngredients;
    }

    public void setCanMakeWithAvailableIngredients(boolean canMakeWithAvailableIngredients) {
        this.canMakeWithAvailableIngredients = canMakeWithAvailableIngredients;
    }

    @Override
    public String toString() {
        return "RecipeResponse{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", ingredients=" + ingredients +
                ", instructions=" + instructions +
                ", cookingTime='" + cookingTime + '\'' +
                ", difficulty='" + difficulty + '\'' +
                ", missingIngredients=" + missingIngredients +
                ", alternativeRecipes=" + alternativeRecipes +
                ", canMakeWithAvailableIngredients=" + canMakeWithAvailableIngredients +
                '}';
    }
}
