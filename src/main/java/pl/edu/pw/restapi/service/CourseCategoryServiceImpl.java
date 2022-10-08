package pl.edu.pw.restapi.service;

import org.springframework.stereotype.Service;
import pl.edu.pw.restapi.domain.CourseCategory;
import pl.edu.pw.restapi.dto.CourseCategoryDTO;
import pl.edu.pw.restapi.dto.mapper.CourseCategoryMapper;
import pl.edu.pw.restapi.repository.CourseCategoryRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    @Override
    public List<CourseCategory> getCategories(List<Long> ids) {
        if (ids == null) {
            return null;
        } else {
            List<CourseCategory> categories = courseCategoryRepository.findAll();
            return ids.stream()
                    .map(id -> getCategory(id, categories)
                            .orElseThrow(() -> new IllegalArgumentException("Category not found")))
                    .collect(Collectors.toList());
        }
    }

    private Optional<CourseCategory> getCategory(Long id, List<CourseCategory> categories) {
        return categories.stream()
                .filter(courseCategory -> courseCategory.getId().equals(id))
                .findAny();
    }

}
