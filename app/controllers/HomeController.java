package controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import models.*;
import play.data.DynamicForm;
import play.data.FormFactory;
import play.mvc.*;
import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class HomeController extends Controller {

    @Inject
    ObjectMapper objectMapper;

    @Inject
    FormFactory formFactory;


    public Result getProfile(Long userId){

        User user = User.find.byId(userId);
        Profile profile = Profile.find.byId(user.profile.id);
        ObjectNode data = objectMapper.createObjectNode();

        List<Long> connectedUserId = user.connections.stream().map(x -> x.id).collect(Collectors.toList());
        List<Long> requestedUserId = user.sent.stream().map(x -> x.receiver.id).collect(Collectors.toList());

        data.set(
            "suggestions",
            objectMapper.valueToTree(
                User.find.all().stream()
                .filter(x -> !connectedUserId.contains(x.id) && !requestedUserId.contains(x.id) && x.id!=user.id )
                .map(x -> {
                    ObjectNode jsonNode = objectMapper.createObjectNode();
                    Profile p = Profile.find.byId(x.profile.id);
                    jsonNode.put("id", x.id );
                    jsonNode.put("email", x.email );
                    jsonNode.put("firstName", p.firstName );
                    jsonNode.put("lastName", p.lastName );
                    return jsonNode;
                })
                .collect(Collectors.toList())
            )
        );

        data.set(
            "connections",
            objectMapper.valueToTree(
                user.connections.stream()
                .map( x -> {
                    User connectedUser = User.find.byId(x.id);
                    Profile connectedProfile = Profile.find.byId(connectedUser.profile.id);
                    ObjectNode connectionJSON = objectMapper.createObjectNode();
                    connectionJSON.put("id", connectedUser.id);
                    connectionJSON.put("email", connectedUser.email);
                    connectionJSON.put("firstName", connectedProfile.firstName);
                    connectionJSON.put("lastName", connectedProfile.lastName);
                    return connectionJSON;
                })
                .collect(Collectors.toList())
            )
        );

        data.set(
            "connectionRequestsReceived",
            objectMapper.valueToTree(
                user.received.stream()
                .map(x -> {
                    User requestor = User.find.byId(x.sender.id);
                    Profile requestorProfile = Profile.find.byId(requestor.profile.id);
                    ObjectNode requestorjson = objectMapper.createObjectNode();
                    requestorjson.put("email", requestor.email);
                    requestorjson.put("firstName", requestorProfile.firstName);
                    requestorjson.put("lastName", requestorProfile.lastName);
                    requestorjson.put("connectionRequestId", requestorProfile.lastName);
                    return requestorjson;
                })
                .collect(Collectors.toList())
            )
        );


        return ok(data);
    }

    public Result updateProfile(Long userId){
        DynamicForm form = formFactory.form().bindFromRequest();
        User user = User.find.byId(userId);
        Profile profile = Profile.find.byId(user.profile.id);
        profile.company = form.get("company");
        profile.firstName = form.get("firstName");
        profile.lastName = form.get("lastName");
        Profile.db().update(profile);
        return ok();
    }
}
