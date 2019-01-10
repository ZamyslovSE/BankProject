package web.pojo;

/**
 * Created by zdoba on 28.11.2018.
 */
public enum TransactionType {
    ADD(0), WITHDRAW(1), TRANSFER(2);
    int id;
    TransactionType(int id){
        this.id=id;
    }
    int id(){
        return id;
    }
}
