## BelloOnQuarkus
Advanced Delivery Controller (ADC) for Chatbots #adc #loadbalancer #chatbot #webhook #crossplatform

### Deploying Bello with Quarkus

Refactored Bello to be able to build and run on top of Quarkus!

```
mvn package -Pnative -Dquarkus.native.container-build=true

docker build -f src/main/docker/Dockerfile.native -t belloonquarkus .

docker run -i --rm -p 8080:8080 -v /config:/software/config -e "strategy=weightedroundrobin" belloonquarkus
```

### Image sizes ###
* pre-Quarkus 438MB
* with Quarkus: 135MB 😮


### What happened ###

This was achieved with few code changes: use RESTEasy Controller instead of Undertow HttpListener, add reflection-config.json to register classes for reflection, leveraging on javax.json.bind for JSON deserialization.













