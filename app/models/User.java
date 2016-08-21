package models;

import com.avaje.ebean.Model;
import org.mindrot.jbcrypt.BCrypt;

import javax.persistence.*;
import java.util.*;

@Entity
public class User extends Model {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long id;

    public String email;

    public String password;

    @OneToMany(mappedBy = "sender")
    public List<ConnectionRequest> sent;

    @OneToMany(mappedBy = "receiver")
    public List<ConnectionRequest> received;

    @OneToOne
    public Profile profile;

    @ManyToMany
    @JoinTable(
        name = "user_connections",
        joinColumns = { @JoinColumn(name = "user_id")},
        inverseJoinColumns = { @JoinColumn(name = "connection_id")}
    )
    public Set<User> connections;

    public static Finder<Long, User> find = new Finder<Long, User>(User.class);

    public static User authenticate(String email, String password){
        User user = User.find.where().eq("email", email).findUnique();
        if(user!=null && BCrypt.checkpw(password, user.password)){
            return user;
        }
        return null;
    }

    public User(String email, String password){
        this.email = email;
        this.password = BCrypt.hashpw(password, BCrypt.gensalt());
    }

}

