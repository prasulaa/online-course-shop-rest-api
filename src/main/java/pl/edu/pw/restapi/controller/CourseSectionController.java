package pl.edu.pw.restapi.controller;

import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import pl.edu.pw.restapi.dto.CourseSectionDTO;
import pl.edu.pw.restapi.dto.CreateCourseSectionDTO;
import pl.edu.pw.restapi.dto.UpdateCourseSectionDTO;
import pl.edu.pw.restapi.service.CourseSectionService;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;

@RestController
@RequestMapping("/course/{courseId}/section")
public class CourseSectionController {

    private final CourseSectionService courseSectionService;

    public CourseSectionController(CourseSectionService courseSectionService) {
        this.courseSectionService = courseSectionService;
    }

    @GetMapping("{sectionId}")
    public ResponseEntity<?> getCourseSection(@PathVariable("courseId") Long courseId,
                                              @PathVariable("sectionId") Long sectionId) {
        //TODO check if forbidden
        try {
            CourseSectionDTO section = courseSectionService.getCourseSection(courseId, sectionId);
            return ResponseEntity.ok(section);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping
    public ResponseEntity<?> createCourseSection(@PathVariable("courseId") Long courseId,
                                                 @RequestBody @Valid CreateCourseSectionDTO section) {
        //TODO check if forbidden
        try {
            CourseSectionDTO createdSection = courseSectionService.createCourseSection(courseId, section);
            return new ResponseEntity<>(createdSection, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("{sectionId}")
    public ResponseEntity<?> updateCourseSection(@PathVariable("courseId") Long courseId,
                                                 @PathVariable("sectionId") Long sectionId,
                                                 @RequestBody @Valid UpdateCourseSectionDTO section) {
        //TODO check if forbidden
        try {
            CourseSectionDTO updatedSection = courseSectionService.updateCourseSection(courseId, sectionId, section);
            return ResponseEntity.ok(updatedSection);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("{sectionId}")
    public ResponseEntity<?> deleteCourseSection(@PathVariable("courseId") Long courseId,
                                                 @PathVariable("sectionId") Long sectionId) {
        //TODO check if forbidden
        try {
            courseSectionService.deleteCourseSection(courseId, sectionId);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
