package com.softserveinc.ita.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "Roles")
public class Role implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(name = "Id", unique = true)
    private String id;
    private String ROLE_name;
    
    @Column(name = "Role")
    private String role;
    
    public Role() {}

    public Role(String role) {this.role = role;}
    /*@Id
    @GeneratedValue*/
    
       /*private Set<User> userCollection;*/
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRoleName() {return ROLE_name;}

    public void setRoleName(String name) {this.ROLE_name = name;}


     /*  public Set<User> getUserCollection() {return userCollection;}

    public void setUserCollection(Set<User> userCollection) {this.userCollection = userCollection;}*/
}