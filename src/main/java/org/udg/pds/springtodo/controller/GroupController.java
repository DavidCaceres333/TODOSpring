package org.udg.pds.springtodo.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.udg.pds.springtodo.entity.Group;
import org.udg.pds.springtodo.entity.IdObject;
import org.udg.pds.springtodo.entity.Task;
import org.udg.pds.springtodo.entity.Views;
import org.udg.pds.springtodo.serializer.JsonDateDeserializer;
import org.udg.pds.springtodo.service.GroupService;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.Date;

@RequestMapping(path = "/groups")
@RestController
public class GroupController extends BaseController {

    @Autowired
    GroupService groupService;

    @GetMapping(path = "/{id}")
    public Group getGroup(HttpSession session, @PathVariable("id") Long id) {
        Long userId = getLoggedUser(session);

        return groupService.getGroup(userId, id);
    }

    @GetMapping
    @JsonView(Views.Private.class)
    public Collection<Group> listAllGroups(HttpSession session) {
        Long userId = getLoggedUser(session);

        return groupService.getGroups(userId);
    }

    @PostMapping(consumes = "application/json")
    public IdObject addGroup(HttpSession session, @Valid @RequestBody GroupController.R_Group group) {

        Long userId = getLoggedUser(session);

        return groupService.addGroup(group.name, group.description, userId);
    }

    @PostMapping(path="/{id}/users")
    public String addUser(@RequestBody Collection<Long> users, HttpSession session,
                          @PathVariable("id") Long groupId) {

        Long userId = getLoggedUser(session);
        groupService.addUserToGroup(userId, groupId, users);
        return BaseController.OK_MESSAGE;
    }

    static class R_Group {

        @NotNull
        public String name;

        @NotNull
        public String description;

    }
}
