package pl.edu.pw.restapi.controller;

import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import pl.edu.pw.restapi.dto.CourseDTO;
import pl.edu.pw.restapi.service.CourseService;

import java.util.List;

@RestController
@RequestMapping("/course")
public class CourseController {

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
                                        @RequestParam(value = "sort", required = false)Sort.Direction sort) {
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

}
