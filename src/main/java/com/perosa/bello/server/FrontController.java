package com.perosa.bello.server;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import com.perosa.bello.core.balancer.Balancer;
import org.jboss.resteasy.spi.HttpRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Path("/belloadc")
public class FrontController {

    private static final Logger LOGGER = LoggerFactory.getLogger(FrontController.class);

    private DispatchLogic dispatchLogic = null;

    @GET @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public String main(@Context HttpRequest request) {
        new DispatchLogic(Balancer.getInstance()).dispatch(request);
        return "tmp"; // todo forward response from downstream
    }


    public DispatchLogic getDispatchLogic() {
        return dispatchLogic;
    }

    public void setDispatchLogic(DispatchLogic dispatchLogic) {
        this.dispatchLogic = dispatchLogic;
    }
}