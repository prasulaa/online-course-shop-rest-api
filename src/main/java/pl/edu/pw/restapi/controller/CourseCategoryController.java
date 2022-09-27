package pl.edu.pw.restapi.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import pl.edu.pw.restapi.dto.CourseCategoryDTO;
import pl.edu.pw.restapi.service.CourseCategoryService;

import java.util.List;

@RestController
@RequestMapping("/category")
public class CourseCategoryController {

    private final CourseCategoryService courseCategoryService;

    public CourseCategoryController(CourseCategoryService courseCategoryService) {
        this.courseCategoryService = courseCategoryService;
    }

    @GetMapping
    public ResponseEntity<?> getCategories() {
        List<CourseCategoryDTO> categories = courseCategoryService.getCategories();
        return ResponseEntity.ok(categories);
    }

}
