package pl.edu.pw.restapi.controller;

import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import pl.edu.pw.restapi.dto.CourseDTO;
import pl.edu.pw.restapi.dto.CourseDetailsDTO;
import pl.edu.pw.restapi.dto.CreateCourseDTO;
import pl.edu.pw.restapi.dto.UpdateCourseDTO;
import pl.edu.pw.restapi.service.CourseService;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/course")
public class CourseController {

    //TODO logger

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
        try {
            List<CourseDTO> courses = courseService.getCourses(title, categories, difficulties, priceMin, priceMax, pageNumber, pageSize, sort);
            return ResponseEntity.ok(courses);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("{id}/details")
    public ResponseEntity<?> getCourseDetails(@PathVariable("id") Long id) {
        //TODO check if forbidden
        try {
            CourseDetailsDTO course = courseService.getCourseDetails(id);
            return ResponseEntity.ok(course);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping
    public ResponseEntity<?> createCourse(@RequestBody @Valid CreateCourseDTO course) {
        //TODO check if forbidden
        // check if each scope is empty
        try {
            CourseDetailsDTO returnDetails = courseService.createCourse(course);
            return new ResponseEntity<>(returnDetails, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @PutMapping("{id}")
    public ResponseEntity<?> updateCourse(@RequestBody UpdateCourseDTO course, @PathVariable("id") Long id) {
        //TODO check if forbidden
        // check if each scope is empty
        try {
            CourseDetailsDTO returnDetails = courseService.updateCourse(course, id);
            return ResponseEntity.ok(returnDetails);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

}
