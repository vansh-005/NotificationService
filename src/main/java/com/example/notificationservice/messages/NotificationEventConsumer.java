package com.example.notificationservice.messages;


import com.example.notificationservice.DTOs.NotificationEnvelope;
import com.example.notificationservice.config.RabbitConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class NotificationEventConsumer  {
    private final NotificationEventRouter router;


    @RabbitListener(queues = "${rabbitmq.notification.queue}")
    public void consume(NotificationEnvelope envelope){
        try {
            router.route(envelope);
        }
        catch (Exception ex){
            log.error(
                    "Failed to process notification event {}",
                    envelope.eventId(),
                    ex
            );
            throw ex;
        }
    }

}
