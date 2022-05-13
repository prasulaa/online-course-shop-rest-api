package pl.edu.pw.restapi.domain;

import javax.persistence.*;
import java.util.List;

@Table
@Entity
public class CourseCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String category;
    @OneToMany
    private List<CourseCategory> subcategories;

    public CourseCategory(Long id, String category, List<CourseCategory> subcategories) {
        this.id = id;
        this.category = category;
        this.subcategories = subcategories;
    }

    public CourseCategory() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public List<CourseCategory> getSubcategories() {
        return subcategories;
    }

    public void setSubcategories(List<CourseCategory> subcategories) {
        this.subcategories = subcategories;
    }
}
