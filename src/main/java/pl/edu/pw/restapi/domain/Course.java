package pl.edu.pw.restapi.domain;

import javax.persistence.*;
import java.util.List;

@Entity
@Table
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private Double price;
    @ManyToMany
    private List<CourseCategory> categories;
    private CourseDifficulty difficulty;
    @ElementCollection
    private List<String> scopes;
    private String description;
    @OneToMany(cascade = CascadeType.ALL)
    private List<CourseSection> sections;
    private String thumbnail;

    public Course(Long id, String title, Double price, List<CourseCategory> categories, CourseDifficulty difficulty, List<String> scopes, String description, List<CourseSection> sections, String thumbnail) {
        this.id = id;
        this.title = title;
        this.price = price;
        this.categories = categories;
        this.difficulty = difficulty;
        this.scopes = scopes;
        this.description = description;
        this.sections = sections;
        this.thumbnail = thumbnail;
    }

    public Course() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public List<CourseCategory> getCategories() {
        return categories;
    }

    public void setCategories(List<CourseCategory> categories) {
        this.categories = categories;
    }

    public CourseDifficulty getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(CourseDifficulty difficulty) {
        this.difficulty = difficulty;
    }

    public List<String> getScopes() {
        return scopes;
    }

    public void setScopes(List<String> scopes) {
        this.scopes = scopes;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<CourseSection> getSections() {
        return sections;
    }

    public void setSections(List<CourseSection> sections) {
        this.sections = sections;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }
}
