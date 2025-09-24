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

import java.time.Duration;
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
                .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(10 * 1024 * 1024))
                .build();
        this.objectMapper = new ObjectMapper();
    }
    public Mono<RecipeResponse> generateRecipe(RecipeRequest request) {
        String prompt = createPrompt(request);

        Map<String, Object> requestBody = createOpenAIRequest(prompt);

        return webClient.post()
                .uri(apiUrl)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + apiKey)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(String.class)
                .timeout(Duration.ofSeconds(30))
                .map(this::parseOpenAIResponse)
                .onErrorReturn(createErrorResponse());
    }
    private String createPrompt(RecipeRequest request) {
        StringBuilder prompt = new StringBuilder();
        prompt.append("Je suis un chef cuisinier. Voici les ingrédients que j'ai : ");
        prompt.append(String.join(", ", request.getIngredients()));
        prompt.append(". Restrictions alimentaires :");
        prompt.append(request.getDietaryRestrictions());
        prompt.append(". Cuisine préférée :");
        prompt.append(request.getCuisine());
        prompt.append(". Nombre de portions :");
        prompt.append(request.getServings());
        prompt.append(". Peut-tu me proposer une recette détaillée ? \n" +
                "Format ta réponse en JSON avec cette structure :\n" +
                "{\n" +
                "  \"recipeName\": \"nom de la recette\",\n" +
                "  \"description\": \"courte description\",\n" +
                "  ...\n" +
                "}");
        return prompt.toString();
    }
    private Map<String, Object> createOpenAIRequest(String prompt) {
        Map<String, Object> message = new HashMap<>();
        message.put("role", "user");
        message.put("content", prompt);

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", "gpt-3.5-turbo");
        requestBody.put("messages", Arrays.asList(message));
        requestBody.put("max_tokens", 1500);
        requestBody.put("temperature", 0.7);

        return requestBody;
    }
    private RecipeResponse parseOpenAIResponse(String response) {
        try {
            JsonNode jsonNode = objectMapper.readTree(response);
            String content = jsonNode.get("choices").get(0).get("message").get("content").asText();

            // Nettoyer la réponse pour extraire le JSON
            String cleanedContent = content.trim();
            if (cleanedContent.startsWith("```json")) {
                cleanedContent = cleanedContent.substring(7);
            }
            if (cleanedContent.endsWith("```")) {
                cleanedContent = cleanedContent.substring(0, cleanedContent.length() - 3);
            }

            // Parser le JSON de la recette
            JsonNode recipeNode = objectMapper.readTree(cleanedContent);
            RecipeResponse recipeResponse = objectMapper.treeToValue(recipeNode, RecipeResponse.class);

            return recipeResponse;
        } catch (Exception e) {
            return createErrorResponse();
        }
    }
    private RecipeResponse createErrorResponse() {
        RecipeResponse errorResponse = new RecipeResponse();
        errorResponse.setRecipeName("Erreur");
        errorResponse.setDescription("Désolé, impossible de générer une recette...");
        errorResponse.setInstructions(Arrays.asList("Vérifiez votre connexion internet..."));
        return errorResponse;
    }
}
