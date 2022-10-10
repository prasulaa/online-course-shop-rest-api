package pl.edu.pw.restapi.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.edu.pw.restapi.dto.CourseCategoryDTO;
import pl.edu.pw.restapi.service.CourseCategoryService;

import java.util.List;

@RestController
@RequestMapping("/categories")
public class CourseCategoryController {

    private final CourseCategoryService courseCategoryService;

    public CourseCategoryController(CourseCategoryService courseCategoryService) {
        this.courseCategoryService = courseCategoryService;
    }

    @GetMapping
    public ResponseEntity<List<CourseCategoryDTO>> getCategories() {
        List<CourseCategoryDTO> categories = courseCategoryService.getCategories();
        return ResponseEntity.ok(categories);
    }

}
