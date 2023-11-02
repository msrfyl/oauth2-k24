package com.msrfyl.k24.oauth2.model;

import java.util.List;

public class Client {
    String clientId;
    String clientSecret;
    List<String> authorizationGrantTypes;
    List<String> scopes;
    Long accessTokenExpired;
    Long refreshTokenExpired;
    String redirectUrl;

    public Client(
            String clientId, String clientSecret, List<String> authorizationGrantTypes,
            List<String> scopes, Long accessTokenExpired, Long refreshTokenExpired
    ) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.authorizationGrantTypes = authorizationGrantTypes;
        this.scopes = scopes;
        this.accessTokenExpired = accessTokenExpired;
        this.refreshTokenExpired = refreshTokenExpired;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public List<String> getAuthorizationGrantTypes() {
        return authorizationGrantTypes;
    }

    public void setAuthorizationGrantTypes(List<String> authorizationGrantTypes) {
        this.authorizationGrantTypes = authorizationGrantTypes;
    }

    public List<String> getScopes() {
        return scopes;
    }

    public void setScopes(List<String> scopes) {
        this.scopes = scopes;
    }

    public Long getAccessTokenExpired() {
        return accessTokenExpired;
    }

    public void setAccessTokenExpired(Long accessTokenExpired) {
        this.accessTokenExpired = accessTokenExpired;
    }

    public Long getRefreshTokenExpired() {
        return refreshTokenExpired;
    }

    public void setRefreshTokenExpired(Long refreshTokenExpired) {
        this.refreshTokenExpired = refreshTokenExpired;
    }

    public String getRedirectUrl() {
        return redirectUrl;
    }

    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }
}
