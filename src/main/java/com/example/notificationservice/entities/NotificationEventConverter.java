package com.example.notificationservice.entities;


import com.example.notificationservice.DTOs.NotificationEvent;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import tools.jackson.databind.ObjectMapper;

@Converter(autoApply = false)
public class NotificationEventConverter
        implements AttributeConverter<NotificationEvent, String> {

    private static final ObjectMapper mapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(NotificationEvent event) {
        try {
            return mapper.writeValueAsString(event);
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to serialize NotificationEvent", e);
        }
    }

    @Override
    public NotificationEvent convertToEntityAttribute(String dbData) {
        try {
            return mapper.readValue(dbData, NotificationEvent.class);
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to deserialize NotificationEvent", e);
        }
    }
}
