package web.pojo;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Created by zdoba on 04.12.2018.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Account {
    private String id;
    private String accountNumber;
    private String clientId;
    private Double balance;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }
}
