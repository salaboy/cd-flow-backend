package com.salaboy.cdf;

import com.salaboy.cdf.model.dao.CloudEventRepository;
import com.salaboy.cdf.model.entities.CloudEventEntity;
import com.salaboy.cdf.services.EventStoreService;
import io.cloudevents.CloudEvent;
import io.cloudevents.spring.webflux.CloudEventHttpMessageReader;
import io.cloudevents.spring.webflux.CloudEventHttpMessageWriter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.codec.CodecCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.codec.CodecConfigurer;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.HandlerMapping;
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import org.springframework.web.reactive.socket.server.WebSocketService;
import org.springframework.web.reactive.socket.server.support.HandshakeWebSocketService;
import org.springframework.web.reactive.socket.server.support.WebSocketHandlerAdapter;
import org.springframework.web.reactive.socket.server.upgrade.ReactorNettyRequestUpgradeStrategy;
import reactor.core.publisher.EmitterProcessor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@SpringBootApplication
@RestController
@RequestMapping("/api/")
@Slf4j
public class CDFApplication {

    public static void main(String[] args) {
        SpringApplication.run(CDFApplication.class, args);
    }


    @Autowired
    private WebSocketHandler webSocketHandler;

    @Autowired
    private EventStoreService eventStoreService;

//    @Bean
//    public GlobalFilter customFilter() {
//        return new LoggingFilter();
//    }




    @Bean
    public HandlerMapping webSocketHandlerMapping() {

        Map<String, WebSocketHandler> map = new HashMap<>();
        map.put("/ws", webSocketHandler);

        SimpleUrlHandlerMapping handlerMapping = new SimpleUrlHandlerMapping();

        handlerMapping.setOrder(1);
        handlerMapping.setUrlMap(map);

        return handlerMapping;
    }

    @Bean
    public WebSocketHandlerAdapter handlerAdapter() {
        return new WebSocketHandlerAdapter(webSocketService());
    } //

    //
    public WebSocketService webSocketService() {
        return new HandshakeWebSocketService(new ReactorNettyRequestUpgradeStrategy());
    }


    @Autowired
    private ReactiveWebSocketHandler handler;

    @GetMapping("/version")
    public String version() {
        return "1.0.0";
    }




    @Autowired
    private CloudEventsProcessor cloudEventsProcessor;

    @Autowired
    private CloudEventRepository cloudEventRepository;




    @GetMapping("/cloudevents")
    public Iterable<CloudEventEntity> getCloudEvents() {
        return cloudEventRepository.findAll();
    }

    @PostMapping("/events")
    public Mono<CloudEvent> event(@RequestBody Mono<CloudEvent> body) {
        return body.map(
                event -> {

                    String cloudEventSerialized = eventStoreService.addEventToStore(event);
                    cloudEventsProcessor.handleEvent(event);

                    List<String> sessionsId = handler.getSessionsId();
                    for (String sid : sessionsId) {
                        handler.getEmitterProcessor(sid).onNext(cloudEventSerialized);
                    }
                    return event;
                });
    }


    @Configuration
    public static class CloudEventHandlerConfiguration implements CodecCustomizer {

        @Override
        public void customize(CodecConfigurer configurer) {
            configurer.customCodecs().register(new CloudEventHttpMessageReader());
            configurer.customCodecs().register(new CloudEventHttpMessageWriter());
        }

    }

}

@Component
@Slf4j
class ReactiveWebSocketHandler implements WebSocketHandler {

    private List<String> sessions = new CopyOnWriteArrayList<>();
    private Map<String, EmitterProcessor<String>> processors = new ConcurrentHashMap<>();


    public ReactiveWebSocketHandler() {
    }

    public EmitterProcessor<String> getEmitterProcessor(String id) {
        return processors.get(id);
    }

    public Set<String> getProcessors() {
        return processors.keySet();
    }

    public List<String> getSessionsId() {
        return sessions;
    }


    @Override
    public Mono<Void> handle(WebSocketSession webSocketSession) {

        String sessionId = webSocketSession.getHandshakeInfo().getUri().getQuery().split("=")[1];
        if (sessions.add(sessionId)) {
            log.info("Session Id added: " + sessionId);
            processors.put(sessionId, EmitterProcessor.create());
            Flux<String> cloudEventsFlux = processors.get(sessionId).map(x -> x);

            // Send the session id back to the client
            String msg = String.format("{\"session\":\"%s\"}", sessionId);
            // Register the outbound flux as the source of outbound messages //.filter(cloudEvent -> cloudEvent.contains(sessionId))
            final Flux<WebSocketMessage> outFlux = Flux.concat(Flux.just(msg), cloudEventsFlux)
                    // final Flux<WebSocketMessage> outFlux = cloudEventsFlux
                    .map(cloudEvent -> {
                        log.info("Sending message to client [{}]: {}", sessionId, cloudEvent);

                        return webSocketSession.textMessage(cloudEvent);
                    });


            return webSocketSession.send(outFlux).and(webSocketSession.receive().doFinally(sig -> {
                log.info("Terminating WebSocket Session (client side) sig: [{}], [{}]", sig.name(), sessionId);
                webSocketSession.close();
                sessions.remove(sessionId);  // remove the stored session id
                processors.remove(sessionId);
                log.info("remove session and processor for id: " + sessionId);
            }).map(WebSocketMessage::getPayloadAsText).log());

        }
        return Mono.empty();

    }
}
