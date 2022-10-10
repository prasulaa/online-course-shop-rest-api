package pl.edu.pw.restapi.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import pl.edu.pw.restapi.dto.CourseLessonDTO;
import pl.edu.pw.restapi.dto.CreateCourseLessonDTO;
import pl.edu.pw.restapi.dto.UpdateCourseLessonDTO;
import pl.edu.pw.restapi.service.CourseLessonService;

import javax.validation.Valid;

@RestController
@RequestMapping("/courses/{courseId}/sections/{sectionId}/lessons")
public class CourseLessonController {

    private final CourseLessonService courseLessonService;

    public CourseLessonController(CourseLessonService courseLessonService) {
        this.courseLessonService = courseLessonService;
    }

    @GetMapping("{lessonId}")
    public ResponseEntity<CourseLessonDTO> getLesson(@PathVariable("courseId") Long courseId,
                                       @PathVariable("sectionId") Long sectionId,
                                       @PathVariable("lessonId") Long lessonId,
                                       @AuthenticationPrincipal String username) {
        CourseLessonDTO lesson = courseLessonService.getLesson(courseId, sectionId, lessonId, username);
        return ResponseEntity.ok(lesson);
    }

    @PostMapping
    public ResponseEntity<CourseLessonDTO> createLesson(@PathVariable("courseId") Long courseId,
                                          @PathVariable("sectionId") Long sectionId,
                                          @RequestBody @Valid CreateCourseLessonDTO lesson,
                                          @AuthenticationPrincipal String username) {
        CourseLessonDTO createdLesson = courseLessonService.createLesson(courseId, sectionId, lesson, username);
        return new ResponseEntity<>(createdLesson, HttpStatus.CREATED);
    }

    @PutMapping("{lessonId}")
    public ResponseEntity<CourseLessonDTO> updateLesson(@PathVariable("courseId") Long courseId,
                                          @PathVariable("sectionId") Long sectionId,
                                          @PathVariable("lessonId") Long lessonId,
                                          @RequestBody @Valid UpdateCourseLessonDTO lesson,
                                          @AuthenticationPrincipal String username) {
        CourseLessonDTO updatedLesson = courseLessonService.updateLesson(courseId, sectionId, lessonId, lesson, username);
        return ResponseEntity.ok(updatedLesson);
    }

    @DeleteMapping("{lessonId}")
    public ResponseEntity<Void> deleteLesson(@PathVariable("courseId") Long courseId,
                                          @PathVariable("sectionId") Long sectionId,
                                          @PathVariable("lessonId") Long lessonId,
                                          @AuthenticationPrincipal String username) {
        courseLessonService.deleteLesson(courseId, sectionId, lessonId, username);
        return ResponseEntity.noContent().build();
    }

}
