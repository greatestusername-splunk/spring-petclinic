/*
 * Decompiled with CFR 0.152.
 */
package com.appdynamics.agent.api;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

public class ServletContext {
    private final URL url;
    private final Map<String, String[]> parameters;
    private final Map<String, String> headers;
    private final Map<String, Object> cookies;
    private final Map<String, Object> sessionAttributes;
    private final String requestMethod;
    private final String hostValue;
    private final String hostOriginatingAddress;

    private ServletContext(URL url, Map<String, String[]> parameters, Map<String, String> headers, Map<String, Object> cookies, Map<String, Object> sessionAttributes, String requestMethod, String hostValue, String hostOriginatingAddress) {
        this.url = url;
        this.parameters = parameters;
        this.headers = headers;
        this.cookies = cookies;
        this.sessionAttributes = sessionAttributes;
        this.requestMethod = requestMethod;
        this.hostValue = hostValue;
        this.hostOriginatingAddress = hostOriginatingAddress;
    }

    public URL getUrl() {
        return this.url;
    }

    public Map<String, String[]> getParameters() {
        return this.parameters;
    }

    public Map<String, String> getHeaders() {
        return this.headers;
    }

    public Map<String, Object> getCookies() {
        return this.cookies;
    }

    public Map<String, Object> getSessionAttributes() {
        return this.sessionAttributes;
    }

    public String getRequestMethod() {
        return this.requestMethod;
    }

    public String getHostValue() {
        return this.hostValue;
    }

    public String getHostOriginatingAddress() {
        return this.hostOriginatingAddress;
    }

    public String toString() {
        return "ServletContext{, requestMethod='" + this.requestMethod + '\'' + ", hostValue=" + this.hostValue + ", hostOriginatingAddress=" + this.hostOriginatingAddress + '}';
    }

    public static class ServletContextBuilder {
        private URL url;
        private Map<String, String[]> parameters;
        private Map<String, String> headers;
        private Map<String, Object> cookies;
        private Map<String, Object> sessionAttributes;
        private String requestMethod;
        private String hostValue;
        private String hostOriginatingAddress;

        public ServletContextBuilder withURL(URL url) {
            this.url = url;
            return this;
        }

        public ServletContextBuilder withURL(String urlString) throws MalformedURLException {
            this.url = new URL(urlString);
            return this;
        }

        public ServletContextBuilder withParameters(Map<String, String[]> parameters) {
            this.parameters = parameters;
            return this;
        }

        public ServletContextBuilder withHeaders(Map<String, String> headers) {
            this.headers = headers;
            return this;
        }

        public ServletContextBuilder withCookies(Map<String, Object> cookies) {
            this.cookies = cookies;
            return this;
        }

        public ServletContextBuilder withSessionAttributes(Map<String, Object> attributes) {
            this.sessionAttributes = attributes;
            return this;
        }

        public ServletContextBuilder withRequestMethod(String requestMethodType) {
            String request = requestMethodType;
            if (request != null) {
                request = request.toUpperCase();
            }
            this.requestMethod = "POST".equals(request) ? "POST" : ("PUT".equals(request) ? "PUT" : ("DELETE".equals(request) ? "DELETE" : "GET"));
            return this;
        }

        public ServletContextBuilder withHostValue(String hostValue) {
            this.hostValue = hostValue;
            return this;
        }

        public ServletContextBuilder withHostOriginatingAddress(String hostOriginatingAddress) {
            this.hostOriginatingAddress = hostOriginatingAddress;
            return this;
        }

        public ServletContext build() {
            return new ServletContext(this.url, this.parameters, this.headers, this.cookies, this.sessionAttributes, this.requestMethod, this.hostValue, this.hostOriginatingAddress);
        }
    }
}

