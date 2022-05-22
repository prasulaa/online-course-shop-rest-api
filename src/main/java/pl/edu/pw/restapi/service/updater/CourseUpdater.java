package pl.edu.pw.restapi.service.updater;

import pl.edu.pw.restapi.domain.Course;
import pl.edu.pw.restapi.domain.CourseCategory;
import pl.edu.pw.restapi.domain.CourseDifficulty;
import pl.edu.pw.restapi.dto.UpdateCourseDTO;

import java.util.List;

public class CourseUpdater {

    public void update(UpdateCourseDTO course, Course courseToUpdate, List<CourseCategory> categories) {
        updateTitle(courseToUpdate, course.getTitle());
        updateThumbnail(courseToUpdate, course.getThumbnail());
        updatePrice(courseToUpdate, course.getPrice());
        updateCategories(courseToUpdate, categories);
        updateDifficulty(courseToUpdate, course.getDifficulty());
        updateScopes(courseToUpdate, course.getScopes());
        updateDescription(courseToUpdate, course.getDescription());
    }

    private void updateTitle(Course course, String title) {
        if (title != null) {
            if (title.isEmpty()) {
                throw new IllegalArgumentException("Title cannot be empty");
            } else {
                course.setTitle(title);
            }
        }
    }

    private void updateThumbnail(Course course, String thumbnail) {
        if (thumbnail != null) {
            if (thumbnail.isEmpty()) {
                throw new IllegalArgumentException("Thumbnail cannot be empty");
            } else {
                course.setThumbnail(thumbnail);
            }
        }
    }

    private void updatePrice(Course course, Double price) {
        if (price != null) {
            if (price < 0) {
                throw new IllegalArgumentException("Price cannot be negative");
            } else {
                course.setPrice(price);
            }
        }
    }

    private void updateCategories(Course course, List<CourseCategory> categories) {
        if (categories != null) {
            if (categories.isEmpty()) {
                throw new IllegalArgumentException("Categories cannot be empty");
            } else {
                course.setCategories(categories);
            }
        }
    }

    private void updateDifficulty(Course course, CourseDifficulty difficulty) {
        if (difficulty != null) {
            course.setDifficulty(difficulty);
        }
    }

    private void updateScopes(Course course, List<String> scopes) {
        if (scopes != null) {
            if (scopes.isEmpty()) {
                throw new IllegalArgumentException("Scopes cannot be empty");
            } else {
                course.setScopes(scopes);
            }
        }
    }

    private void updateDescription(Course course, String description) {
        if (description != null) {
            if (description.isEmpty()) {
                throw new IllegalArgumentException("Description cannot be empty");
            } else {
                course.setDescription(description);
            }
        }
    }

}
