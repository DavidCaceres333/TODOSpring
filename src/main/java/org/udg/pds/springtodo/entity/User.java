package org.udg.pds.springtodo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import org.udg.pds.springtodo.controller.exceptions.ServiceException;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Entity(name = "users")
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"email", "username","phoneNumber"}))
public class User implements Serializable {
  /**
   * Default value included to remove warning. Remove or modify at will. *
   */
  private static final long serialVersionUID = 1L;

  public User() {
  }

  public User(String username, String email, String password, int phoneNumber) {
    this.username = username;
    this.email = email;
    this.password = password;
    this.phoneNumber = phoneNumber;
    this.tasks = new ArrayList<>();
  }

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotNull
  private String username;

  @NotNull
  private String email;

  @NotNull
  private String password;

  @NotNull
  private Integer phoneNumber;

  @NotNull
  private String description = "Hey! I'm using Avarst";

  @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
  private Collection<Task> tasks;

  @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
  private Collection<Group> groups;

  @ManyToMany(cascade = CascadeType.PERSIST)
  private Set<Group> group = new HashSet<>();

  @JsonView(Views.Private.class)
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  @JsonView(Views.Private.class)
  public String getEmail() {
    return email;
  }

  @JsonView(Views.Public.class)
  public String getUsername() {
    return username;
  }

  @JsonView(Views.Private.class)
  public int getPhoneNumber() { return phoneNumber;}

  @JsonIgnore
  public String getPassword() {
    return password;
  }

  @JsonView(Views.Private.class)
  public String getDescription() { return description;}

  @JsonView(Views.Complete.class)
  public Collection<Task> getTasks() {
    // Since tasks is collection controlled by JPA, it has LAZY loading by default. That means
    // that you have to query the object (calling size(), for example) to get the list initialized
    // More: http://www.javabeat.net/jpa-lazy-eager-loading/
    tasks.size();
    return tasks;
  }

    @JsonView(Views.Complete.class)
    public Collection<Group> getGroups() {
        // Since tasks is collection controlled by JPA, it has LAZY loading by default. That means
        // that you have to query the object (calling size(), for example) to get the list initialized
        // More: http://www.javabeat.net/jpa-lazy-eager-loading/
        groups.size();
        return groups;
    }

  public void addTask(Task task) {
    tasks.add(task);
  }

  public void addGroup(Group group) {
        this.groups.add(group);
    }
}
