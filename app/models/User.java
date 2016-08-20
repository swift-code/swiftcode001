package models;

import com.avaje.ebean.Model;
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

}

