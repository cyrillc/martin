/**
 * 
 */
package aiController;
import java.sql.Date;

/**
 * @ marco
 *
 */
public class User {
    
    private String name; 
    private String lastname; 
    private Date age;
    private String residence; 
    private Date lastLogin;
    
    
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


    

    /**
     * 
     */
    public User() {
        // TODO Auto-generated constructor stub
    }

}
