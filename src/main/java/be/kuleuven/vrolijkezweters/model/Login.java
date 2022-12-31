package be.kuleuven.vrolijkezweters.model;

public class Login {
    String userName;
    String passWord;
    String email;
    Boolean isAdmin;

    public Login(){

    }

    @Override
    public String toString() {
        return "Login{" +
                "userName=" + userName +
                ", passWord=" + passWord +
                ", email=" + email +
                ", isAdmin=" + isAdmin +
                '}';
    }

    public Login(String userName, String passWord, String email, Boolean isAdmin) {
        this.userName = userName;
        this.passWord = passWord;
        this.email = email;
        this.isAdmin = isAdmin;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(Boolean isAdmin) {
        this.isAdmin = isAdmin;
    }
}
