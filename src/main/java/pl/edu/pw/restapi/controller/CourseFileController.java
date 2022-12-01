package pl.edu.pw.restapi.controller;

import lombok.AllArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.edu.pw.restapi.dto.CourseFileDTO;
import pl.edu.pw.restapi.dto.CourseFileInfoDTO;
import pl.edu.pw.restapi.service.CourseFileService;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/courses/{courseId}/files")
@AllArgsConstructor
public class CourseFileController {

    private final CourseFileService courseFileService;

    @GetMapping
    public ResponseEntity<List<CourseFileInfoDTO>> getCourseFiles(@PathVariable("courseId") Long courseId,
                                            @AuthenticationPrincipal String username) {
        List<CourseFileInfoDTO> courseFiles = courseFileService.getCourseFiles(courseId, username);
        return ResponseEntity.ok(courseFiles);
    }

    @GetMapping("{fileId}")
    public ResponseEntity<Resource> getCourseFile(@PathVariable("courseId") Long courseId,
                                                  @PathVariable("fileId") Long fileId,
                                                  @AuthenticationPrincipal String username) {
        CourseFileDTO file = courseFileService.getCourseFile(courseId, fileId, username);
        return ResponseEntity.ok(new ByteArrayResource(file.getData()));
    }

    @PostMapping
    public ResponseEntity<CourseFileInfoDTO> uploadCourseFile(@PathVariable("courseId") Long courseId,
                                              @RequestParam("file") MultipartFile file,
                                              @AuthenticationPrincipal String username) throws IOException {
        CourseFileInfoDTO fileInfo = courseFileService.createCourseFile(courseId, file, username);
        return new ResponseEntity<>(fileInfo, HttpStatus.CREATED);
    }

    @DeleteMapping("{fileId}")
    public ResponseEntity<Void> deleteCourseFile(@PathVariable("courseId") Long courseId,
                                              @PathVariable("fileId") Long fileId,
                                              @AuthenticationPrincipal String username) {
        courseFileService.deleteCourseFile(courseId, fileId, username);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
