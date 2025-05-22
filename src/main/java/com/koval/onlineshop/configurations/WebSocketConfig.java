package com.koval.onlineshop.configurations;


import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.*;

/*
  * Конфигурация websocket. STOMP
  * enableSimpleBroker /changequantity - брокер. Для коннекта с клиента
  * new StompJs.Client({
  *   brokerURL: 'ws://{host}:{port}/changequantity' });
  *   addEndpoint("/changequantity") - endpoint для отправки сообщений контроллер
  *   JS function для отправки сообщения об изменениях function send() {
  *        stompClient.publish({
  *        destination: "/app/changequantity",
  *        body: jsonMessage
  *  });
  *}
 */
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/changequantity");
        config.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/changequantity");
    }

}
