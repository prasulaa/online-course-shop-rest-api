package pl.edu.pw.restapi.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import pl.edu.pw.restapi.dto.*;
import pl.edu.pw.restapi.service.CourseService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.xml.bind.DatatypeConverter;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/courses")
@RequiredArgsConstructor
public class CourseController {

    @Value("${payu.secondKey}")
    private String secondKey;
    private final CourseService courseService;
    private final ObjectMapper objectMapper;

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

    @PostMapping("payment-notification")
    public ResponseEntity<Void> buyCoursePaymentNotification(@RequestParam("id") Long courseId,
                                                             @RequestParam("username") String username,
                                                             @RequestBody String body,
                                                             @RequestHeader("openpayu-signature") String singature) throws JsonProcessingException {
        if (verifyPayuSignature(body, singature)) {
            PayuNotificationDTO notification = objectMapper.readValue(body, PayuNotificationDTO.class);
            courseService.boughtCourse(courseId, username, notification);
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    private boolean verifyPayuSignature(String body, String signatureHeader) {
        try {
            String expectedSignature = getSignature(signatureHeader);
            String actualSignature = sign(body);

            return expectedSignature.equals(actualSignature.toLowerCase());
        } catch (Exception e) {
            return false;
        }
    }

    private String getSignature(String signatureHeader) {
        String[] splittedHeader = signatureHeader.split(";");
        return Arrays.stream(splittedHeader)
                .filter((value) -> value.startsWith("signature="))
                .findFirst()
                .orElseThrow()
                .substring(10)
                .toLowerCase();
    }

    private String sign(String body) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update((body + secondKey).getBytes(StandardCharsets.UTF_8));
        byte[] digest = md.digest();
        return DatatypeConverter.printHexBinary(digest).toLowerCase();
    }


}
