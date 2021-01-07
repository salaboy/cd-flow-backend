package com.salaboy.cdf;

import io.cloudevents.CloudEvent;

public interface CloudEventHandler {

     void handle(CloudEvent ce);

     boolean matches(CloudEvent ce);

}
