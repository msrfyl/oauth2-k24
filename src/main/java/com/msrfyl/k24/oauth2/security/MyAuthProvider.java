package com.msrfyl.k24.oauth2.security;

import com.msrfyl.k24.oauth2.U;
import kong.unirest.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class MyAuthProvider implements AuthenticationProvider {

    private final Logger logger = LoggerFactory.getLogger(MyAuthProvider.class);

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        try {
            String username = authentication.getName();
            String password = authentication.getCredentials().toString();
            String urlApi = new U().getResource().getUrl() + "/api/authenticate";
            HttpResponse<String> response = new U().accessClient().post(urlApi)
                    .field("username", username)
                    .field("password", password)
                    .asString();
            logger.info("authenticate user " + username + " [" + response.getStatus() + "]");
            if (response.getStatus() == 200) {
                logger.info("success login user");
                return new UsernamePasswordAuthenticationToken(username, password, new ArrayList<>());
            } else if(response.getStatus() == 401) {
                throw new BadCredentialsException("authorization server cant connecting with resource");
            } else {
                throw new BadCredentialsException("username or password not match");
            }
        } catch (Exception e) {
            logger.error("failed authenticate", e);
            return null;
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication == UsernamePasswordAuthenticationToken.class;
    }
}
