package ru.mirea.Tokens;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class PayloadToken {
    private long id;
    private String login;
    private List<Role> role;

    public PayloadToken() {
        id = Integer.MIN_VALUE;
        login = "";
        role = Collections.emptyList();
    }

    public PayloadToken(long id, String login, List<Role> role) {
        this.id = id;
        this.login = login;
        if(role != null)
            this.role = role;
        else
            this.role = Collections.emptyList();
    }

    public PayloadToken(long id, String login, Role role) {
        this.id = id;
        this.login = login;
        this.role = Collections.singletonList(role);
    }

    public long getId() {
        return id;
    }

    public String getLogin() {
        return login;
    }

    public List<Role> getRole() {
        return role;
    }

    public String generateJSON() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException();
        }
    }

    /**
     * Разбирает текстовое представление Payload в текущий класс.
     * @param payload example {"id":2, "login":"pup", "role": "ADMIN"}.
     * @return Экземпляр этого класса. Значение ид, значение логина, значение роли.
     */
    public static PayloadToken decoderJson(String payload) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(payload, PayloadToken.class);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PayloadToken that = (PayloadToken) o;
        return id == that.id &&
                Objects.equals(login, that.login) &&
                Objects.equals(role, that.role);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, login, role);
    }

    @Override
    public String toString() {
        return generateJSON();
    }
}
