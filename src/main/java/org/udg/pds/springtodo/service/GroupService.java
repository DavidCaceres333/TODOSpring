package org.udg.pds.springtodo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.udg.pds.springtodo.controller.exceptions.ServiceException;
import org.udg.pds.springtodo.entity.*;
import org.udg.pds.springtodo.repository.GroupRepository;
import org.udg.pds.springtodo.repository.UserRepository;

import java.util.Collection;
import java.util.Optional;

@Service
public class GroupService {

    @Autowired
    GroupRepository groupRepository;

    @Autowired
    UserService userService;

    @Autowired
    UserRepository userRepository;

    public GroupRepository crud() {
        return groupRepository;
    }

    public Collection<Group> getGroups(Long id) {
        return userService.getUser(id).getGroups();
    }

    public Group getGroup(Long userId, Long id) {
        Optional<Group> t = groupRepository.findById(id);
        if (!t.isPresent())
            throw new ServiceException("Group does not exists");
        if (t.get().getUser().getId() != userId)
            throw new ServiceException("User does not own this group");
        return t.get();
    }

    @Transactional
    public IdObject addGroup(String name, String description, Long userId) {
        try {
            User user = userService.getUser(userId);

            Group group = new Group
                (name, description);

            group.setUser(user);

            user.addGroup(group);

            groupRepository.save(group);
            return new IdObject(group.getId());
        } catch (Exception ex) {
            // Very important: if you want that an exception reaches the EJB caller, you
            // have to throw an ServiceException
            // We catch the normal exception and then transform it in a ServiceException
            throw new ServiceException(ex.getMessage());
        }
    }

    @Transactional
    public void addUserToGroup(Long userIdLog, Long groupId, Collection<Long> users) {
        Group group = this.getGroup(userIdLog, groupId);

        if (group.getUser().getId() != userIdLog)
            throw new ServiceException("This user is not the owner of the group");

        try {
            for (Long userId : users) {
                User user = userService.getUser(userId);
                group.addUser(user);
            }
        } catch (Exception ex) {
            // Very important: if you want that an exception reaches the EJB caller, you have to throw an ServiceException
            // We catch the normal exception and then transform it in a ServiceException
            throw new ServiceException(ex.getMessage());
        }
    }
}
