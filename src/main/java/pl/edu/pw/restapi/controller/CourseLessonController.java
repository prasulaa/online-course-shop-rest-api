package pl.edu.pw.restapi.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import pl.edu.pw.restapi.dto.CourseLessonDTO;
import pl.edu.pw.restapi.dto.CreateCourseDTO;
import pl.edu.pw.restapi.dto.CreateCourseLessonDTO;
import pl.edu.pw.restapi.dto.UpdateCourseLessonDTO;
import pl.edu.pw.restapi.service.CourseLessonService;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;

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
                                       @PathVariable("lessonId") Long lessonId,
                                       @AuthenticationPrincipal String username) {
        try {
            CourseLessonDTO lesson = courseLessonService.getLesson(courseId, sectionId, lessonId, username);
            return ResponseEntity.ok(lesson);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping
    public ResponseEntity<?> createLesson(@PathVariable("courseId") Long courseId,
                                          @PathVariable("sectionId") Long sectionId,
                                          @RequestBody @Valid CreateCourseLessonDTO lesson,
                                          @AuthenticationPrincipal String username) {
        try {
            CourseLessonDTO createdLesson = courseLessonService.createLesson(courseId, sectionId, lesson, username);
            return new ResponseEntity<>(createdLesson, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("{lessonId}")
    public ResponseEntity<?> updateLesson(@PathVariable("courseId") Long courseId,
                                          @PathVariable("sectionId") Long sectionId,
                                          @PathVariable("lessonId") Long lessonId,
                                          @RequestBody @Valid UpdateCourseLessonDTO lesson,
                                          @AuthenticationPrincipal String username) {
        try {
            CourseLessonDTO updatedLesson = courseLessonService.updateLesson(courseId, sectionId, lessonId, lesson, username);
            return ResponseEntity.ok(updatedLesson);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("{lessonId}")
    public ResponseEntity<?> deleteLesson(@PathVariable("courseId") Long courseId,
                                          @PathVariable("sectionId") Long sectionId,
                                          @PathVariable("lessonId") Long lessonId,
                                          @AuthenticationPrincipal String username) {
        try {
            courseLessonService.deleteLesson(courseId, sectionId, lessonId, username);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
