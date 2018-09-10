package web.pojo;

import java.time.LocalDate;

public class Operation {
    private long id;
    private String senderPassport;
    private String receiverPassport;
    private double amount;
    private LocalDate date;

    public Operation() {
    }

    public Operation(long id, String senderPassport, String receiverPassport, double amount, LocalDate date) {
        this.id=id;
        this.senderPassport = senderPassport;
        this.receiverPassport = receiverPassport;
        this.amount = amount;
        this.date = date;
    }

    public long getId() {
        return id;
    }

    public String getSenderPassport() {
        return senderPassport;
    }


    public String getReceiverPassport() {
        return receiverPassport;
    }

    public double getAmount() {
        return amount;
    }

    public LocalDate getDate() {
        return date;
    }

}
