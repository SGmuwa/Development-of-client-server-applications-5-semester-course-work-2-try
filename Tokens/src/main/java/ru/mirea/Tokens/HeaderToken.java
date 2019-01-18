package ru.mirea.Tokens;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.Objects;

public class HeaderToken {
    /**
     * Тип токена.
     */
    private String type;
    /**
     * Название метода подписания токена.
     */
    private String hashName;

    public HeaderToken() {
        type = "";
        hashName = "";
    }

    public HeaderToken(String type, String hashName) {
        this.type = type;
        this.hashName = hashName;
    }

    public String generateJSON() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException();
        }
    }

    public static HeaderToken decoderJson(String header) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(header, HeaderToken.class);
    }

    public String getType() {
        return type;
    }

    public String getHashName() {
        return hashName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HeaderToken that = (HeaderToken) o;
        return Objects.equals(type, that.type) &&
                Objects.equals(hashName, that.hashName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, hashName);
    }
}
