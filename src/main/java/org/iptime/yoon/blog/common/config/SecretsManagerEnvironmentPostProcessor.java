package org.iptime.yoon.blog.common.config;

import com.fasterxml.jackson.core.JsonProcessingException;
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



            String value = mapper.readTree(secretValue).get("spring.datasource.url").asText();
            System.setProperty("spring.datasource.url", value);


            value = mapper.readTree(secretValue).get("spring.datasource.username").asText();
            System.setProperty("spring.datasource.username", value);

            value = mapper.readTree(secretValue).get("spring.datasource.password").asText();
            System.setProperty("spring.datasource.password", value);

            value = mapper.readTree(secretValue).get("spring.security.oauth2.client.registration.google.client-id").asText();
            System.setProperty("spring.security.oauth2.client.registration.google.client-id", value);


            value = mapper.readTree(secretValue).get("spring.security.oauth2.client.registration.google.redirect-uri").asText();
            System.setProperty("spring.security.oauth2.client.registration.google.redirect-uri", value);


            value = mapper.readTree(secretValue).get("spring.data.redis.host").asText();
            System.setProperty("spring.data.redis.host", value);



            value = mapper.readTree(secretValue).get("cloud.aws.region.static").asText();
            System.setProperty("cloud.aws.region.static", value);


            value = mapper.readTree(secretValue).get("cloud.aws.s3.bucket").asText();
            System.setProperty("cloud.aws.s3.bucket", value);


            value = mapper.readTree(secretValue).get("cdn.baseUrl").asText();
            System.setProperty("cdn.baseUrl", value);

            value = mapper.readTree(secretValue).get("cors.allowed").asText();
            System.setProperty("cors.allowed", value);



            value = mapper.readTree(secretValue).get("auth.jwt.secret-key").asText();
            System.setProperty("auth.jwt.secret-key", value);

            value = mapper.readTree(secretValue).get("auth.jwt.issuer").asText();
            System.setProperty("auth.jwt.issuer", value);

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }



}