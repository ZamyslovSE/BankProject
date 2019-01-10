package web.pojo;

import java.util.List;

/**
 * Created by zdoba on 07.01.2019.
 */
public class UserInfo {
    private String clientId;
    private List<String> accountIdList;

    public UserInfo(String clientId) {
        this.clientId = clientId;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public List<String> getAccountIdList() {
        return accountIdList;
    }

    public void setAccountIdList(List<String> accountIdList) {
        this.accountIdList = accountIdList;
    }
}
