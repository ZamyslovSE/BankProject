package web.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Token {
    @JsonProperty("token_bank")
    String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Token(String token) {
        this.token = token;
    }

    private Token() {
    }
}
