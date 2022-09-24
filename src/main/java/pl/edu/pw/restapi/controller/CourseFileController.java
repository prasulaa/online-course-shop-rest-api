package pl.edu.pw.restapi.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import pl.edu.pw.restapi.dto.CourseFileDTO;
import pl.edu.pw.restapi.dto.CourseFileInfoDTO;
import pl.edu.pw.restapi.service.CourseFileService;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@RestController
@RequestMapping("/course/{courseId}/file")
@AllArgsConstructor
public class CourseFileController {

    private final CourseFileService courseFileService;

    @GetMapping
    public ResponseEntity<?> getCourseFiles(@PathVariable("courseId") Long courseId,
                                            @AuthenticationPrincipal String username) {
        try {
            List<CourseFileInfoDTO> courseFiles = courseFileService.getCourseFiles(courseId, username);
            return ResponseEntity.ok(courseFiles);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("{fileId}")
    public ResponseEntity<?> getCourseFile(@PathVariable("courseId") Long courseId,
                                           @PathVariable("fileId") Long fileId,
                                           @AuthenticationPrincipal String username) {
        try {
            username = username.equals("anonymousUser") ? "string" : username; // TODO delete this
            CourseFileDTO file = courseFileService.getCourseFile(courseId, fileId, username);
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getName() + "\"")
                    .body(file.getData());
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping
    public ResponseEntity<?> uploadCourseFile(@PathVariable("courseId") Long courseId,
                                              @RequestParam("file") MultipartFile file,
                                              @AuthenticationPrincipal String username) {
        try {
            username = username.equals("anonymousUser") ? "string" : username; // TODO delete this
            CourseFileInfoDTO fileInfo = courseFileService.createCourseFile(courseId, file, username);
            return new ResponseEntity<>(fileInfo, HttpStatus.CREATED);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("{fileId}")
    public ResponseEntity<?> deleteCourseFile(@PathVariable("courseId") Long courseId,
                                              @PathVariable("fileId") Long fileId,
                                              @AuthenticationPrincipal String username) {
        try {
            username = username.equals("anonymousUser") ? "string" : username; // TODO delete this
            courseFileService.deleteCourseFile(courseId, fileId, username);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
