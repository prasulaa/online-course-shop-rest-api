package pl.edu.pw.restapi.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import pl.edu.pw.restapi.dto.CourseSectionDTO;
import pl.edu.pw.restapi.dto.CreateCourseSectionDTO;
import pl.edu.pw.restapi.dto.UpdateCourseSectionDTO;
import pl.edu.pw.restapi.service.CourseSectionService;

import javax.validation.Valid;

@RestController
@RequestMapping("/courses/{courseId}/sections")
public class CourseSectionController {

    private final CourseSectionService courseSectionService;

    public CourseSectionController(CourseSectionService courseSectionService) {
        this.courseSectionService = courseSectionService;
    }

    @GetMapping("{sectionId}")
    public ResponseEntity<CourseSectionDTO> getCourseSection(@PathVariable("courseId") Long courseId,
                                                          @PathVariable("sectionId") Long sectionId,
                                                          @AuthenticationPrincipal String username) {
        CourseSectionDTO section = courseSectionService.getCourseSection(courseId, sectionId, username);
        return ResponseEntity.ok(section);
    }

    @PostMapping
    public ResponseEntity<CourseSectionDTO> createCourseSection(@PathVariable("courseId") Long courseId,
                                                      @RequestBody @Valid CreateCourseSectionDTO section,
                                                      @AuthenticationPrincipal String username) {
        CourseSectionDTO createdSection = courseSectionService.createCourseSection(courseId, section, username);
        return new ResponseEntity<>(createdSection, HttpStatus.CREATED);
    }

    @PutMapping("{sectionId}")
    public ResponseEntity<CourseSectionDTO> updateCourseSection(@PathVariable("courseId") Long courseId,
                                                 @PathVariable("sectionId") Long sectionId,
                                                 @RequestBody @Valid UpdateCourseSectionDTO section,
                                                 @AuthenticationPrincipal String username) {
        CourseSectionDTO updatedSection = courseSectionService.updateCourseSection(courseId, sectionId, section, username);
        return ResponseEntity.ok(updatedSection);
    }

    @DeleteMapping("{sectionId}")
    public ResponseEntity<Void> deleteCourseSection(@PathVariable("courseId") Long courseId,
                                                 @PathVariable("sectionId") Long sectionId,
                                                 @AuthenticationPrincipal String username) {
        courseSectionService.deleteCourseSection(courseId, sectionId, username);
        return ResponseEntity.noContent().build();
    }
}
