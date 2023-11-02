package com.msrfyl.k24.oauth2;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import com.msrfyl.k24.oauth2.model.Auth;
import com.msrfyl.k24.oauth2.model.Client;
import com.msrfyl.k24.oauth2.model.Resource;
import kong.unirest.Unirest;
import kong.unirest.UnirestInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.StreamSupport;

public class U {

    private Logger logger = LoggerFactory.getLogger(U.class);

    public String configRunningPath = "configuration/tmp/running-configuration.yml";

    private JsonNode configYml() throws IOException {
        ObjectMapper map = new YAMLMapper();
        String configPath = "configuration/configuration.yml";
        File fi = new File(configPath);
        return map.readTree(fi);
    }

    private JsonNode clientYml() throws IOException {
        ObjectMapper map = new YAMLMapper();
        String clientPath = "configuration/client.yml";
        File fi = new File(clientPath);
        return map.readTree(fi);
    }

    public List<Client> registeredClient() throws IOException {
        List<Client> clients = new ArrayList<>();
        clientYml().get("clients").forEach(it -> {
            List<String> grantType = StreamSupport
                    .stream(it.get("authorization-grant-types").spliterator(), false)
                    .toList()
                    .stream().map(i ->
                            i.toString().replace("\"", "")
                    ).toList();
            List<String> scopes = StreamSupport
                    .stream(it.get("access-token-expired").spliterator(), false)
                    .toList()
                    .stream().map(i ->
                            i.toString().replace("\"", "")
                    ).toList();
            Client c = new Client(
                    it.get("client-id").asText(),
                    it.get("client-secret").asText(),
                    grantType,
                    scopes,
                    Long.parseLong(it.get("access-token-expired").asText()),
                    Long.parseLong(it.get("refresh-token-expired").asText())
            );
            if (it.get("redirect-url") != null) {
                c.setRedirectUrl(it.get("redirect-url").asText());
            }
            clients.add(c);
        });
        return clients;
    }

    public Auth getAuth() throws IOException {
        String ip = configYml().get("auth").get("ip").asText();
        String port = configYml().get("auth").get("port").asText();
        return new Auth(ip, port);
    }

    public Resource getResource() throws IOException {
        String ip = configYml().get("resource").get("ip").asText();
        String port = configYml().get("resource").get("port").asText();
        return new Resource(ip, port);
    }

    public UnirestInstance accessClient() {
        UnirestInstance ni = Unirest.spawnInstance();
        ni.config().interceptor(new UInterceptor());
        return ni;
    }

}
