package web.pojo;

import java.math.BigDecimal;
import java.time.LocalDate;

public class TransactionRequest {
    private String id;
    private String senderId;
    private String receiverId;
    private BigDecimal amount;
    private LocalDate date;
    private TransactionType type;

    public TransactionRequest() {
    }

    public TransactionRequest(String id, String senderPassport, String receiverPassport, BigDecimal amount, LocalDate date) {
        this.id=id;
        this.senderId = senderPassport;
        this.receiverId = receiverPassport;
        this.amount = amount;
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public String getSenderId() {
        return senderId;
    }


    public String getReceiverId() {
        return receiverId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public LocalDate getDate() {
        return date;
    }

}
