package com.perosa.bello.server;

import com.perosa.bello.core.balancer.Balancer;
import com.perosa.bello.core.channel.ChannelInspector;
import io.vertx.core.Vertx;
import org.apache.commons.io.IOUtils;
import org.jboss.resteasy.spi.HttpRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.ws.rs.core.MultivaluedMap;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class DispatchLogic {

    private static final Logger LOGGER = LoggerFactory.getLogger(DispatchLogic.class);

    private Balancer balancer;

    @Inject
    Vertx vertx;

    public DispatchLogic(Balancer balancer) {
        this.balancer = balancer;
    }

    void dispatch(HttpRequest request) {

        String target = getBalancer().findTarget(getRequest(request));

    }

    InRequest getRequest(HttpRequest httpRequest) {
        InRequest request = new InRequest();

        request.setHost(getHost(httpRequest));
        request.setHeaders(getHeaders(httpRequest));
        request.setParameters(getParameters(httpRequest));
        request.setPayload(getBody(httpRequest));
        request.setChannel(new ChannelInspector().getChannel(request));

        LOGGER.info(request.toString());

        return request;
    }

    private String getHost(HttpRequest httpRequest) {
        return httpRequest.getUri().getBaseUri().getHost();
    }

    private String getBody(HttpRequest httpRequest) {
        String body = "";

        if(httpRequest.getInputStream() != null) {
            try {
                body = IOUtils.toString(httpRequest.getInputStream(), StandardCharsets.UTF_8.name());
            } catch (IOException e) {
                LOGGER.error(e.getMessage(), e);
            }
        }
        return body;

    }

    private Map<String, String> getHeaders(HttpRequest httpRequest) {

        MultivaluedMap<String, String> multivaluedMap = httpRequest.getHttpHeaders().getRequestHeaders();

        Map<String, String> headers = new HashMap<>();

        for (String str : multivaluedMap.keySet()) {
            headers.put(str, headers.get(str));
        }

        return headers;
    }

    private Map<String, String[]> getParameters(HttpRequest httpRequest) {

        MultivaluedMap<String, String> multivaluedMap = httpRequest.getUri().getQueryParameters();

        Map<String, String[]> parameters = new HashMap<>();

        for (String str : multivaluedMap.keySet()) {
            parameters.put(str, new String[]{multivaluedMap.getFirst(str)});
        }

        return parameters;
    }


    public Balancer getBalancer() {
        return balancer;
    }

    public void setBalancer(Balancer balancer) {
        this.balancer = balancer;
    }
}
