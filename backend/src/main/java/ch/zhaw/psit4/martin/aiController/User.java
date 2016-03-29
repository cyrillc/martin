package ch.zhaw.psit4.martin.aiController;

import java.sql.Date;

/**
 * This class represents User's specific informations.
 * 
 * @ version 1.0
 */
public class User {

    private String name;
    private String lastname;
    private Date age;
    private String residence;
    private Date lastLogin;

    /**
     * @param name
     * @param lastname
     * @param age
     * @param residence
     * @param lastLogin
     */
    public User(String name, String lastname, Date age, String residence,
            Date lastLogin) {
        this.name = name;
        this.lastname = lastname;
        this.age = age;
        this.residence = residence;
        this.lastLogin = lastLogin;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public Date getAge() {
        return age;
    }

    public void setAge(Date age) {
        this.age = age;
    }

    public String getResidence() {
        return residence;
    }

    public void setResidence(String residence) {
        this.residence = residence;
    }

    public Date getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(Date lastLogin) {
        this.lastLogin = lastLogin;
    }

}
