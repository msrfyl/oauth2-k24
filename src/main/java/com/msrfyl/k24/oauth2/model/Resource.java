package com.msrfyl.k24.oauth2.model;

public class Resource {
    String ip;
    String port;
    String url;

    public Resource(String ip, String port) {
        this.ip = ip;
        this.port = port;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getUrl() {
        String u = "";
        if (ip.startsWith("http")) {
            u = ip + ":" + port;
        } else {
            u = "http://" + ip + ":" + port;
        }
        return u;
    }
}
