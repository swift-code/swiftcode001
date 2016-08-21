package controllers;

import forms.LoginForm;
import play.data.Form;
import play.data.FormFactory;
import play.mvc.Controller;
import play.mvc.Result;

import javax.inject.Inject;

public class LoginController extends Controller{

    @Inject
    FormFactory formFactory;

    public Result login(){

        Form<LoginForm> loginForm = formFactory.form(LoginForm.class).bindFromRequest();
        if(loginForm.hasErrors()){
            return ok(loginForm.errorsAsJson());
        }

        return ok();
    }



}
