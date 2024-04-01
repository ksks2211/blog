package org.iptime.yoon.blog.common.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.env.ConfigurableEnvironment;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueRequest;

import java.util.Arrays;

/**
 * @author rival
 * @since 2024-03-08
 */
public class SecretsManagerEnvironmentPostProcessor implements EnvironmentPostProcessor {

    private static final String DEPLOYMENT_PROFILE = "deploy";




    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {

//         Check if the deployment profile is active
        if (Arrays.stream(environment.getActiveProfiles()).noneMatch(DEPLOYMENT_PROFILE::equals)) {
            // If not, skip loading secrets and return early
            return;
        }
        try(SecretsManagerClient client = SecretsManagerClient.builder()
            .region(Region.of("ap-northeast-2"))
            .build()){
            // Replace 'myapp/secret' with the actual name of your secret
            GetSecretValueRequest request = GetSecretValueRequest.builder()
                .secretId("myblog/secret")
                .build();

            // Fetch the secret value
            String secretValue = client.getSecretValue(request).secretString();

            // Assuming the secret value is a JSON string with a key named 'message'
            ObjectMapper mapper = new ObjectMapper();

            JsonNode jsonNode = mapper.readTree(secretValue);

            jsonNode.fieldNames().forEachRemaining(key->{
                String value = jsonNode.get(key).asText();
                System.setProperty(key,value);
            });

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }



}