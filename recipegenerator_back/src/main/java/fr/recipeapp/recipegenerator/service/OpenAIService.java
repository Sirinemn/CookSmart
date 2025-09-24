package fr.recipeapp.recipegenerator.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.recipeapp.recipegenerator.model.RecipeRequest;
import fr.recipeapp.recipegenerator.model.RecipeResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Service
public class OpenAIService {

    @Value("${openai.api.key}")
    private String apiKey;

    @Value("${openai.api.url}")
    private String apiUrl;

    private final WebClient webClient;
    private final ObjectMapper objectMapper;

    public OpenAIService() {
        this.webClient = WebClient.builder()
                .baseUrl("https://api.openai.com/v1")
                .build();
        this.objectMapper = new ObjectMapper();
    }

    public Mono<RecipeResponse> generateRecipe(RecipeRequest request) {
        String prompt = buildPrompt(request);

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", "gpt-3.5-turbo");
        requestBody.put("messages", Arrays.asList(
                Map.of("role", "system", "content", "Tu es un chef cuisinier expert qui aide les gens à créer des recettes avec les ingrédients qu'ils ont. Réponds toujours en JSON avec le format exact demandé."),
                Map.of("role", "user", "content", prompt)
        ));
        requestBody.put("max_tokens", 1500);
        requestBody.put("temperature", 0.7);

        return webClient.post()
                .uri("/chat/completions")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + apiKey)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(String.class)
                .map(this::parseOpenAIResponse)
                .onErrorResume(error -> {
                    System.err.println("Erreur lors de l'appel à OpenAI: " + error.getMessage());
                    return Mono.just(createErrorResponse());
                });
    }

    private String buildPrompt(RecipeRequest request) {
        StringBuilder prompt = new StringBuilder();
        prompt.append("J'ai les ingrédients suivants à la maison : ");
        prompt.append(String.join(", ", request.getIngredients()));
        prompt.append("\n\n");

        if (request.getDietaryRestrictions() != null && !request.getDietaryRestrictions().isEmpty()) {
            prompt.append("Restrictions alimentaires : ").append(request.getDietaryRestrictions()).append("\n");
        }

        if (request.getCuisine() != null && !request.getCuisine().isEmpty()) {
            prompt.append("Type de cuisine préféré : ").append(request.getCuisine()).append("\n");
        }

        if (request.getServings() != null) {
            prompt.append("Nombre de portions : ").append(request.getServings()).append("\n");
        }

        prompt.append("\nPeux-tu me proposer une recette que je peux faire avec ces ingrédients ? ");
        prompt.append("Si il me manque des ingrédients importants, propose-moi des alternatives ou des ingrédients supplémentaires à acheter. ");
        prompt.append("\nRéponds UNIQUEMENT avec un JSON valide dans ce format exactement :\n");
        prompt.append("{\n");
        prompt.append("  \"title\": \"Nom de la recette\",\n");
        prompt.append("  \"description\": \"Description courte de la recette\",\n");
        prompt.append("  \"ingredients\": [\"liste\", \"des\", \"ingrédients\"],\n");
        prompt.append("  \"instructions\": [\"étape 1\", \"étape 2\", \"etc\"],\n");
        prompt.append("  \"cookingTime\": \"30 minutes\",\n");
        prompt.append("  \"difficulty\": \"Facile\",\n");
        prompt.append("  \"missingIngredients\": [\"ingrédients manquants\"],\n");
        prompt.append("  \"canMakeWithAvailableIngredients\": true,\n");
        prompt.append("  \"alternativeRecipes\": [\"autres suggestions\"]\n");
        prompt.append("}\n");
        prompt.append("Ne ajoute aucun texte avant ou après le JSON.");

        return prompt.toString();
    }

    private RecipeResponse parseOpenAIResponse(String response) {
        try {
            JsonNode jsonNode = objectMapper.readTree(response);
            String content = jsonNode.path("choices").get(0).path("message").path("content").asText();

            // Nettoyer la réponse pour extraire seulement le JSON
            String jsonContent = extractJsonFromResponse(content);

            return objectMapper.readValue(jsonContent, RecipeResponse.class);
        } catch (Exception e) {
            System.err.println("Erreur lors du parsing de la réponse OpenAI: " + e.getMessage());
            return createErrorResponse();
        }
    }

    private String extractJsonFromResponse(String response) {
        // Supprimer les markdown et autres formatages
        String cleaned = response.trim();
        if (cleaned.startsWith("```json")) {
            cleaned = cleaned.substring(7);
        }
        if (cleaned.startsWith("```")) {
            cleaned = cleaned.substring(3);
        }
        if (cleaned.endsWith("```")) {
            cleaned = cleaned.substring(0, cleaned.length() - 3);
        }

        // Trouver le début et la fin du JSON
        int start = cleaned.indexOf('{');
        int end = cleaned.lastIndexOf('}');

        if (start != -1 && end != -1 && end > start) {
            return cleaned.substring(start, end + 1);
        }

        return cleaned;
    }

    private RecipeResponse createErrorResponse() {
        RecipeResponse errorResponse = new RecipeResponse();
        errorResponse.setTitle("Erreur");
        errorResponse.setDescription("Désolé, je n'ai pas pu générer une recette pour le moment.");
        errorResponse.setInstructions(Arrays.asList("Veuillez réessayer plus tard."));
        errorResponse.setCanMakeWithAvailableIngredients(false);
        return errorResponse;
    }
}
