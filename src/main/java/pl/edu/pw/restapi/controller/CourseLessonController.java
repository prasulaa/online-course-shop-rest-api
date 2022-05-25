package pl.edu.pw.restapi.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import pl.edu.pw.restapi.dto.CourseLessonDTO;
import pl.edu.pw.restapi.service.CourseLessonService;

import javax.persistence.EntityNotFoundException;

@RestController
@RequestMapping("/course/{courseId}/section/{sectionId}/lesson")
public class CourseLessonController {

    private final CourseLessonService courseLessonService;

    public CourseLessonController(CourseLessonService courseLessonService) {
        this.courseLessonService = courseLessonService;
    }

    @GetMapping("{lessonId}")
    public ResponseEntity<?> getLesson(@PathVariable("courseId") Long courseId,
                                       @PathVariable("sectionId") Long sectionId,
                                       @PathVariable("lessonId") Long lessonId) {
        //TODO check if forbidden
        try {
            CourseLessonDTO lesson = courseLessonService.getLesson(courseId, sectionId, lessonId);
            return ResponseEntity.ok(lesson);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
