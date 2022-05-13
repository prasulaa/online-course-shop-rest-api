package pl.edu.pw.restapi.domain;

import javax.persistence.*;
import java.util.List;

@Entity
@Table
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String passwordHash;
    @ManyToMany
    private List<Course> boughtCourses;
    @OneToMany
    private List<Course> releasedCourses;

    public User(Long id, String username, String passwordHash, List<Course> boughtCourses, List<Course> releasedCourses) {
        this.id = id;
        this.username = username;
        this.passwordHash = passwordHash;
        this.boughtCourses = boughtCourses;
        this.releasedCourses = releasedCourses;
    }

    public User() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public List<Course> getBoughtCourses() {
        return boughtCourses;
    }

    public void setBoughtCourses(List<Course> boughtCourses) {
        this.boughtCourses = boughtCourses;
    }

    public List<Course> getReleasedCourses() {
        return releasedCourses;
    }

    public void setReleasedCourses(List<Course> releasedCourses) {
        this.releasedCourses = releasedCourses;
    }

}
