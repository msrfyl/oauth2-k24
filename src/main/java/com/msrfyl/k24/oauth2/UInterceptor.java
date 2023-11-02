package com.msrfyl.k24.oauth2;

import com.msrfyl.k24.oauth2.model.Client;
import kong.unirest.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class UInterceptor implements Interceptor {

    private final Logger logger = LoggerFactory.getLogger(UInterceptor.class);

    @Override
    public void onRequest(HttpRequest<?> request, Config config) {
        try {
            String token = loadToken();
            request.header("Authorization", "Bearer " + token);
        } catch (IOException e) {
            logger.error("failed on request", e);
            throw new RuntimeException(e);
        }
        Interceptor.super.onRequest(request, config);
    }

    @Override
    public void onResponse(HttpResponse<?> response, HttpRequestSummary request, Config config) {
        Interceptor.super.onResponse(response, request, config);
    }

    private String loadToken() throws IOException {
        Client client = new U().registeredClient().stream().findFirst().get();
        HttpResponse<JsonNode> response = Unirest.post(new U().getAuth().getUrl() + "/oauth2/token")
                .basicAuth(client.getClientId(), client.getClientSecret())
                .field("grant_type", client.getAuthorizationGrantTypes())
                .asJson().ifFailure(i -> {
                    logger.error("load token failed");
                });
        return response.getBody().getObject().getString("access_token");
    }

}
