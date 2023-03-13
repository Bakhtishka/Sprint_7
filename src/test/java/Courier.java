import static io.restassured.RestAssured.given;

public class Courier {
    private String login;
    private String password;
    private String lastName;


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

    public String getLastName() {
        return lastName;
    }


    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Courier(String login, String password){
        this.login = login;
        this.password = password;
    }


    public Courier(String login, String password, String lastName) {
        this.login = login;
        this.password = password;
        this.lastName = lastName;
    }

    public Courier() {
    }



}
