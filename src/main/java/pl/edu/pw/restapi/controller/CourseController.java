package pl.edu.pw.restapi.controller;

import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import pl.edu.pw.restapi.dto.CourseDTO;
import pl.edu.pw.restapi.dto.CourseDetailsDTO;
import pl.edu.pw.restapi.dto.CreateCourseDTO;
import pl.edu.pw.restapi.dto.UpdateCourseDTO;
import pl.edu.pw.restapi.service.CourseService;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/course")
public class CourseController {

    //TODO logger and
    // status 500 without msg and
    // getCourseDetails without authorization

    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @GetMapping
    public ResponseEntity<?> getCourses(@RequestParam(value = "title", required = false) String title,
                                        @RequestParam(value = "category", required = false) List<Long> categories,
                                        @RequestParam(value = "difficulty", required = false) List<Long> difficulties,
                                        @RequestParam(value = "priceMin", required = false) Double priceMin,
                                        @RequestParam(value = "priceMax", required = false) Double priceMax,
                                        @RequestParam(value = "pageNumber", required = false) Integer pageNumber,
                                        @RequestParam(value = "pageSize", required = false) Integer pageSize,
                                        @RequestParam(value = "sort", required = false) Sort.Direction sort) {
        List<CourseDTO> courses = courseService.getCourses(title, categories, difficulties, priceMin, priceMax, pageNumber, pageSize, sort);
        return ResponseEntity.ok(courses);
    }

    @GetMapping("{id}/details")
    public ResponseEntity<?> getCourseDetails(@PathVariable("id") Long id,
                                              @AuthenticationPrincipal String username) {
        CourseDetailsDTO course = courseService.getCourseDetails(id, username);
        return ResponseEntity.ok(course);
    }

    @PostMapping
    public ResponseEntity<?> createCourse(@RequestBody @Valid CreateCourseDTO course,
                                          @AuthenticationPrincipal String username) {
        CourseDetailsDTO returnDetails = courseService.createCourse(course, username);
        return new ResponseEntity<>(returnDetails, HttpStatus.CREATED);
    }

    @PutMapping("{id}")
    public ResponseEntity<?> updateCourse(@RequestBody @Valid UpdateCourseDTO course,
                                          @PathVariable("id") Long id,
                                          @AuthenticationPrincipal String username) {
        CourseDetailsDTO returnDetails = courseService.updateCourse(course, id, username);
        return ResponseEntity.ok(returnDetails);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteCourse(@PathVariable("id") Long id,
                                          @AuthenticationPrincipal String username) {
        courseService.deleteCourse(id, username);
        return ResponseEntity.noContent().build();
    }

}
