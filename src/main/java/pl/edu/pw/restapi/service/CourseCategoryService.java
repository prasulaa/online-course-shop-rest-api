package pl.edu.pw.restapi.service;

import pl.edu.pw.restapi.domain.CourseCategory;
import pl.edu.pw.restapi.dto.CourseCategoryDTO;

import java.util.List;

public interface CourseCategoryService {

    List<CourseCategoryDTO> getCategories();
    
    List<CourseCategory> getCategories(List<Long> ids);

}
