package com.koval.onlineshop.services.implement;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumerService {

    @KafkaListener(topics = "changequantity")
    public void listen(String message) {
    }
}
