package tn.anasazx.tunirate.authentication.service.implementation;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import tn.anasazx.tunirate.authentication.service.GoogleAuthService;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class GoogleAuthServiceImpl implements GoogleAuthService {

    @Value("${google.client-id}")
    private String clientId;

    @Override
    public GoogleIdToken.Payload verifyToken(String idTokenString) {

        try {

            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), GsonFactory.getDefaultInstance())
                            .setAudience(Collections.singletonList(clientId))
                            .build();

            GoogleIdToken idToken = verifier.verify(idTokenString);

            if (idToken == null) {
                throw new RuntimeException("Invalid Google token");
            }

            return idToken.getPayload();

        } catch (Exception e) {
            throw new RuntimeException("Google authentication failed", e);
        }

    }

}