package fr.recipeapp.recipegenerator.controller;

import fr.recipeapp.recipegenerator.model.RecipeRequest;
import fr.recipeapp.recipegenerator.model.RecipeResponse;
import fr.recipeapp.recipegenerator.service.OpenAIService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/api/recipes")
@CrossOrigin(origins = "*")
public class RecipeController {

    private final OpenAIService openAIService;

    public RecipeController(OpenAIService openAIService) {
        this.openAIService = openAIService;
    }

    @PostMapping("/generate")
    public Mono<ResponseEntity<RecipeResponse>> generateRecipe(@RequestBody RecipeRequest request) {
        // Validation basique
        if (request.getIngredients() == null || request.getIngredients().isEmpty()) {
            RecipeResponse errorResponse = new RecipeResponse();
            errorResponse.setTitle("Erreur de validation");
            errorResponse.setDescription("Veuillez fournir au moins un ingrédient.");
            return Mono.just(ResponseEntity.badRequest().body(errorResponse));
        }

        return openAIService.generateRecipe(request)
                .map(response -> ResponseEntity.ok(response))
                .onErrorResume(error -> {
                    RecipeResponse errorResponse = new RecipeResponse();
                    errorResponse.setTitle("Erreur du serveur");
                    errorResponse.setDescription("Une erreur s'est produite lors de la génération de la recette.");
                    return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse));
                });
    }

    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("Recipe API is running!");
    }

    // Endpoint optionnel pour tester rapidement avec des paramètres GET
    @GetMapping("/generate-simple")
    public Mono<ResponseEntity<RecipeResponse>> generateSimpleRecipe(
            @RequestParam List<String> ingredients,
            @RequestParam(required = false) String cuisine,
            @RequestParam(required = false) String restrictions) {

        RecipeRequest request = new RecipeRequest();
        request.setIngredients(ingredients);
        request.setCuisine(cuisine);
        request.setDietaryRestrictions(restrictions);

        return generateRecipe(request);
    }
}
