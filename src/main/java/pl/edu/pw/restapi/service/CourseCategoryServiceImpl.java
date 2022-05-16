package pl.edu.pw.restapi.service;

import org.springframework.stereotype.Service;
import pl.edu.pw.restapi.domain.CourseCategory;
import pl.edu.pw.restapi.dto.CourseCategoryDTO;
import pl.edu.pw.restapi.dto.mapper.CourseCategoryMapper;
import pl.edu.pw.restapi.repository.CourseCategoryRepository;

import java.util.List;

@Service
public class CourseCategoryServiceImpl implements CourseCategoryService {

    private final CourseCategoryRepository courseCategoryRepository;

    public CourseCategoryServiceImpl(CourseCategoryRepository courseCategoryRepository) {
        this.courseCategoryRepository = courseCategoryRepository;
    }

    @Override
    public List<CourseCategoryDTO> getCategories() {
        List<CourseCategory> categories = courseCategoryRepository.findAll();
        return CourseCategoryMapper.map(categories);
    }

}
