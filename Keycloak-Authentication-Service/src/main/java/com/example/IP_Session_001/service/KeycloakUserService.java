package com.example.IP_Session_001.service;

import com.example.IP_Session_001.dto.response.KeycloakUserCreateResponse;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.representations.AccessTokenResponse;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class KeycloakUserService {

    private final Keycloak keycloak;

    @Value("${keycloak.realm}")
    private String realm;

    @Value(("${keycloak.server-url}"))
    private String serverUrl;

    @Value("${spring.security.oauth2.client.registration.keycloak.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.keycloak.client-secret}")
    private String clientSecret;

    public KeycloakUserCreateResponse createUser(String email, String password, String firstName, String lastName) {

        UserRepresentation user = new UserRepresentation();
        user.setEnabled(true);
        user.setUsername(email);
        user.setEmail(email);
        user.setFirstName(firstName);
        user.setLastName(lastName);

        Response response = keycloak.realm(realm).users().create(user);
        if (response.getStatus() == 409) {
            throw new RuntimeException("User already exists");
        }
        if (response.getStatus() != 201) {
            throw new RuntimeException("Failed: " + response.getStatus());
        }

        String userId = response.getLocation().getPath().replaceAll(".*/([^/]+)$", "$1");

        CredentialRepresentation credential = new CredentialRepresentation();
        credential.setTemporary(false);
        credential.setType(CredentialRepresentation.PASSWORD);
        credential.setValue(password);

        keycloak.realm(realm)
                .users()
                .get(userId)
                .resetPassword(credential);

        // NEW: Generate token for the created user
        Map<String, Object> token = generateToken(email, password);

        return new KeycloakUserCreateResponse(userId, token);
    }



    public Map<String, Object> generateToken(String username, String password) {
        try {
            log.info("Generating token for user: {}", username);

            Keycloak keycloak = KeycloakBuilder.builder()
                    .serverUrl(serverUrl)
                    .realm(realm)
                    .clientId(clientId)
                    .clientSecret(clientSecret)
                    .username(username)
                    .password(password)
                    .grantType(OAuth2Constants.PASSWORD)
                    .build();

            AccessTokenResponse response = keycloak.tokenManager().getAccessToken();

            log.info("Token generated successfully for user: {}", username);
            return Map.of(
                    "access_token", response.getToken(),
                    "refresh_token", response.getRefreshToken(),
                    "expires_in", response.getExpiresIn(),
                    "token_type", response.getTokenType()
            );
        } catch (jakarta.ws.rs.NotAuthorizedException e) {
            log.error("Unauthorized while generating token for user: {}. Check client credentials, direct access grants, and user status.", username, e);
            throw new RuntimeException("Cannot generate token: Unauthorized (401). " +
                    "Make sure your Keycloak client allows direct access grants and credentials are correct.");
        } catch (Exception e) {
            log.error("Failed to generate token for user: {}", username, e);
            throw new RuntimeException("Cannot generate token: " + e.getMessage());
        }
    }


}
