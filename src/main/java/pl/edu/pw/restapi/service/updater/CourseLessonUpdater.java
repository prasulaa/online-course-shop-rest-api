package pl.edu.pw.restapi.service.updater;

import pl.edu.pw.restapi.domain.CourseLesson;
import pl.edu.pw.restapi.dto.UpdateCourseLessonDTO;

public class CourseLessonUpdater {

    public void update(CourseLesson lessonToUpdate, UpdateCourseLessonDTO lesson) {
        updateName(lessonToUpdate, lesson.getName());
        updateData(lessonToUpdate, lesson.getData());
    }

    private void updateName(CourseLesson lessonToUpdate, String name) {
        if (name != null) {
            if (name.isEmpty()) {
                throw new IllegalArgumentException("Name cannot be empty");
            } else {
                lessonToUpdate.setName(name);
            }
        }
    }

    private void updateData(CourseLesson lessonToUpdate, String data) {
        if (data != null) {
            if (data.isEmpty()) {
                throw new IllegalArgumentException("Data cannot be empty");
            } else {
                lessonToUpdate.setData(data);
            }
        }
    }

}
