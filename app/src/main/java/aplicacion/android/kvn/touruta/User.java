package aplicacion.android.kvn.touruta;

public class User {

    private int userId;
    private String email;
    private String password;
    private String name;
    private String lastName;
    private String nickName;

    public User( String email, String password, String name, String lastName, String nickName) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.lastName = lastName;
        this.nickName = nickName;
    }

    public int getUserId() {
        return userId;
    }

    public User() {
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }
}
