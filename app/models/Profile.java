package models;

import com.avaje.ebean.Model;
import javax.persistence.*;

@Entity
public class Profile extends Model {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long id;

    public String firstName;

    public String lastName;

    public String company;

    public static Finder<Long, Profile> find = new Finder<Long, Profile>(Profile.class);

    public Profile(String firstName, String lastName){
        this.firstName = firstName;
        this.lastName = lastName;
    }

}
