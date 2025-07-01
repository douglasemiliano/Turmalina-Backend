package com.ifpb.turmalina.service;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.classroom.ClassroomScopes;
import com.google.api.services.oauth2.Oauth2Scopes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.List;

@Service
public class AuthService {
    @Value("${google.client-id}")
    private String clientId;

    @Value("${google.client-secret}")
    private String clientSecret;
    private static final String REDIRECT_URI = "http://localhost:8080/Callback";

    public static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    private static final List<String> scopes = Arrays.asList(
            ClassroomScopes.CLASSROOM_COURSES,
            ClassroomScopes.CLASSROOM_COURSEWORK_STUDENTS,
            ClassroomScopes.CLASSROOM_ROSTERS,
            ClassroomScopes.CLASSROOM_COURSEWORK_STUDENTS_READONLY,
            ClassroomScopes.CLASSROOM_COURSEWORK_ME,
            ClassroomScopes.CLASSROOM_COURSEWORK_ME_READONLY,
            ClassroomScopes.CLASSROOM_PROFILE_EMAILS,
            ClassroomScopes.CLASSROOM_PROFILE_PHOTOS,
            Oauth2Scopes.USERINFO_PROFILE
    );
    private static Credential storedCredential;

    public String getAuthUrl() throws GeneralSecurityException, IOException {
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                GoogleNetHttpTransport.newTrustedTransport(),
                JSON_FACTORY,
                clientId,
                clientSecret,
                scopes
        ).setAccessType("offline") // Permite obter o refresh token
                .build();

        return flow.newAuthorizationUrl()
                .setRedirectUri(REDIRECT_URI)
                .build();
    }

    public String saveCredentials(String authorizationCode) throws IOException, GeneralSecurityException {
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                GoogleNetHttpTransport.newTrustedTransport(),
                JSON_FACTORY,
                clientId,
                clientSecret,
                scopes
        ).setAccessType("offline")
                .build();


        // Trocando o código de autorização pelo token
        TokenResponse tokenResponse = flow.newTokenRequest(authorizationCode)
                .setRedirectUri(REDIRECT_URI)
                .execute();

        // Criando as credenciais com base no token recebido
        storedCredential = flow.createAndStoreCredential(tokenResponse, "user");

        return tokenResponse.getAccessToken();
    }

    public Credential getCredentials() {
        return storedCredential; // Retorna as credenciais armazenadas
    }
}

