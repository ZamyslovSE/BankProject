package web.pojo;

public class User {
    private String id;
    private String login;
    private String password;

    public User(String login, String passport, String password) {
        this.login =passport;
        this.password=password;
    }

    public User() {
    }

    public User(String login, String password) {
        this.login =login;
        this.password=password;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
