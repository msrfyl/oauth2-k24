package com.msrfyl.k24.oauth2.security;

import com.msrfyl.k24.oauth2.U;
import com.nimbusds.jose.jwk.JWKSelector;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.ProviderSettings;
import org.springframework.security.oauth2.server.authorization.config.TokenSettings;
import org.springframework.security.web.SecurityFilterChain;

import java.io.IOException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Configuration(proxyBeanMethods = false)
public class AuthorizationServerConfig {

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    private SecurityFilterChain authServerSecurityFilterChain(HttpSecurity httpSecurity) throws Exception {
        OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(httpSecurity);
        httpSecurity.formLogin(i -> {
            i.loginPage("/login");
        });
        httpSecurity.sessionManagement(i -> {
            i.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        });
        return httpSecurity.build();
    }

    @Bean
    private RegisteredClientRepository registeredClientRepository() throws IOException {
        List<RegisteredClient> clientList = new ArrayList<>();

        new U().registeredClient().forEach(i ->  {
            RegisteredClient.Builder builder =RegisteredClient.withId(UUID.randomUUID().toString())
                    .clientId(i.getClientId())
                    .clientSecret("{noop}"+i.getClientSecret())
                    .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC);
            i.getAuthorizationGrantTypes().forEach( ag -> {
                switch (ag) {
                    case "client_credentials" -> builder.authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS);
                    case "refresh_token" -> builder.authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN);
                    case "authorization_code" -> builder.authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE);
                    case "jwt_bearer" -> builder.authorizationGrantType(AuthorizationGrantType.JWT_BEARER);
                    default -> builder.authorizationGrantType(AuthorizationGrantType.PASSWORD);
                }
            });
            i.getScopes().forEach( sc -> {
                builder.scope(sc);
            });
            if (i.getRedirectUrl() != null) {
                builder.redirectUri(i.getRedirectUrl());
            }
            clientList.add(
                builder.tokenSettings(
                        TokenSettings.builder()
                                .accessTokenTimeToLive(Duration.ofSeconds(i.getAccessTokenExpired()))
                                .refreshTokenTimeToLive(Duration.ofSeconds(i.getRefreshTokenExpired()))
                                .build()
                ).build()
            );
        });

        return new InMemoryRegisteredClientRepository(clientList);
    }

    @Bean
    private JWKSource<SecurityContext> jwkSource() {
        RSAKey rsaKey = getRsaKey();
        JWKSet jwkSet = new JWKSet(rsaKey);
        return (jwkSelector, securityContext) -> jwkSelector.select(jwkSet);
    }

    private RSAKey getRsaKey() {
        KeyPair keyPair = keyPair();
        RSAPublicKey rsaPublicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey rsaPrivateKey = (RSAPrivateKey) keyPair.getPrivate();
        return new RSAKey.Builder(rsaPublicKey)
                .privateKey(rsaPrivateKey)
                .keyID(UUID.randomUUID().toString())
                .build();
    }

    private KeyPair keyPair() {
        KeyPair keyPair = null;
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048);
            keyPair = keyPairGenerator.generateKeyPair();
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
        return keyPair;
    }

    @Bean
    private ProviderSettings  providerSettings() throws IOException {
        return ProviderSettings.builder()
                .issuer(new U().getAuth().getUrl())
                .build();
    }



}
