package com.ferb.expenseMoneyTracker.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.*;

import java.util.UUID;

@RequiredArgsConstructor
@NoArgsConstructor(force = true)
@AllArgsConstructor
@Data
public class JwtSubject<T> {

    @JsonProperty("id")
    private String id = UUID.randomUUID().toString();

    @JsonProperty("subject")
    @NonNull
    private T data;

    private static final ObjectMapper mapper = new ObjectMapper();

    @Override
    public String toString() {
        try {
            return mapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize JwtSubject to JSON", e);
        }
    }

    public static <T> JwtSubject<T> fromString(String json, Class<T> subjectClass) {
        try {
            JavaType type = mapper.getTypeFactory()
                    .constructParametricType(JwtSubject.class, subjectClass);
            return mapper.readValue(json, type);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to deserialize JwtSubject from JSON", e);
        }
    }

}
