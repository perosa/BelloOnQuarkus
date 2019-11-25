package com.perosa.bello.core.resource.host;

import com.perosa.bello.core.config.Env;
import com.perosa.bello.core.resource.ResourceHost;
import com.perosa.bello.core.resource.ResourceHostList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class HostDatastore {

    private static final Logger LOGGER = LoggerFactory.getLogger(HostDatastore.class);

    private Env env;

    public HostDatastore() {
        this.env = new Env();
    }

    public List<ResourceHost> load() {

        List<ResourceHost> resourceHosts = new ArrayList<>();

        try {
            resourceHosts = unmarshal(getJson(getFilePath()));

        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        }

        return resourceHosts;

    }

    List<ResourceHost> unmarshal(String json) throws IOException {
        List<ResourceHost> resourceHosts = null;

        Jsonb jsonb = JsonbBuilder.create();

        ResourceHostList resourceHostList = new ResourceHostList();
        resourceHostList = jsonb.fromJson(json, ResourceHostList.class);

        return resourceHostList.getList();
    }

    String getJson(String filepath) throws IOException {
        String json = "";

        File file = new File(filepath);

        if (!file.exists()) {
            throw new RuntimeException("File not found: " + filepath);
        } else {
            byte[] b = Files.readAllBytes(file.toPath());
            json = new String(b);
        }

        return json;
    }

    String getFilePath() {
        return getEnv().getConfig() + "hosts.json";
    }

    public Env getEnv() {
        return env;
    }

    public void setEnv(Env env) {
        this.env = env;
    }
}
