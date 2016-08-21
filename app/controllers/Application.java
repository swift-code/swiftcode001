package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import forms.SignupForm;
import models.Profile;
import models.User;
import play.data.*;
import play.mvc.*;
import javax.inject.Inject;
import java.util.Map;

public class Application extends Controller{

    @Inject
    FormFactory formFactory;

    @Inject
    ObjectMapper objectMapper;

    public Result signup(){
        Form<SignupForm> signupForm = formFactory.form(SignupForm.class).bindFromRequest();
        if(signupForm.hasErrors()){
            return ok(signupForm.errorsAsJson());
        }

        Profile profile = new Profile( signupForm.data().get("firstName"), signupForm.data().get("lastName") );
        Profile.db().save(profile);

        User user = new User(signupForm.data().get("email"), signupForm.data().get("password"));

        user.profile = profile;
        User.db().save(user);

        return ok((JsonNode) objectMapper.valueToTree(user));
    }



}
