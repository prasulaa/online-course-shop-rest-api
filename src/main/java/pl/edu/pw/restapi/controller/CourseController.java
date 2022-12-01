package pl.edu.pw.restapi.controller;

import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import pl.edu.pw.restapi.dto.*;
import pl.edu.pw.restapi.service.CourseService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/courses")
public class CourseController {

    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @GetMapping
    public ResponseEntity<List<CourseDTO>> getCourses(@RequestParam(value = "title", required = false) String title,
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
    public ResponseEntity<CourseDetailsDTO> getCourseDetails(@PathVariable("id") Long id) {
        CourseDetailsDTO course = courseService.getCourseDetails(id);
        return ResponseEntity.ok(course);
    }

    @GetMapping("bought")
    public ResponseEntity<List<CourseDTO>> getBoughtCourses(@AuthenticationPrincipal String username) {
        List<CourseDTO> boughtCourses = courseService.getBoughtCourses(username);
        return ResponseEntity.ok(boughtCourses);
    }

    @GetMapping("released")
    public ResponseEntity<List<CourseDTO>> getReleasedCourses(@AuthenticationPrincipal String username) {
        List<CourseDTO> releasedCourses = courseService.getReleasedCourses(username);
        return ResponseEntity.ok(releasedCourses);
    }

    @PostMapping
    public ResponseEntity<?> createCourse(@RequestBody @Valid CreateCourseDTO course,
                                          @AuthenticationPrincipal String username) {
        CourseDetailsDTO returnDetails = courseService.createCourse(course, username);
        return new ResponseEntity<>(returnDetails, HttpStatus.CREATED);
    }

    @PutMapping("{id}")
    public ResponseEntity<CourseDetailsDTO> updateCourse(@RequestBody @Valid UpdateCourseDTO course,
                                                         @PathVariable("id") Long id,
                                                         @AuthenticationPrincipal String username) {
        CourseDetailsDTO returnDetails = courseService.updateCourse(course, id, username);
        return ResponseEntity.ok(returnDetails);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteCourse(@PathVariable("id") Long id,
                                             @AuthenticationPrincipal String username) {
        courseService.deleteCourse(id, username);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("{id}/buy")
    public ResponseEntity<BuyCourseResponseDTO> buyCourse(@PathVariable("id") Long id,
                                                          @AuthenticationPrincipal String username,
                                                          HttpServletRequest request) {
        BuyCourseResponseDTO response = courseService.buyCourse(id, username, request.getRemoteAddr());

        if (response == null) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(response);
    }


}
