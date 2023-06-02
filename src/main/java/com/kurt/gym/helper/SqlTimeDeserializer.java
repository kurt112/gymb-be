package com.kurt.gym.helper;

import java.io.IOException;
import java.sql.Time;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

@Service
public class SqlTimeDeserializer extends JsonDeserializer<Time> {
    @Override
    public Time deserialize(JsonParser jp, DeserializationContext ctxt) {
        try {
            return Time.valueOf(jp.getValueAsString() + ":00");
        } catch (IOException e) {

        }
        return null;
    }
}
