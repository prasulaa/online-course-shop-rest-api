package pl.edu.pw.restapi.dto.mapper;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.edu.pw.restapi.domain.CourseCategory;
import pl.edu.pw.restapi.dto.CourseCategoryDTO;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class CourseCategoryMapper {

    public List<String> mapNames(List<CourseCategory> categories) {
        if (categories == null) {
            return null;
        } else {
            return categories.stream()
                    .map(CourseCategory::getCategory)
                    .collect(Collectors.toList());
        }
    }

    public List<CourseCategoryDTO> map(List<CourseCategory> categories) {
        if (categories == null) {
            return null;
        } else {
            return mapDistinct(categories);
        }
    }

    public CourseCategoryDTO map(CourseCategory category) {
        if (category == null) {
            return null;
        } else {
            return CourseCategoryDTO.builder()
                    .id(category.getId())
                    .name(category.getCategory())
                    .subcategories(mapSubcategories(category.getSubcategories()))
                    .build();
        }
    }

    private List<CourseCategoryDTO> mapDistinct(List<CourseCategory> categories) {
        return categories.stream()
                .filter((c) -> !isPresentInSubcategories(c.getId(), categories))
                .map(this::map)
                .collect(Collectors.toList());
    }

    private boolean isPresentInSubcategories(long categoryId, List<CourseCategory> categories) {
        return categories.stream()
                .anyMatch((c) -> isPresent(categoryId, c.getSubcategories()));
    }

    private boolean isPresent(long id, List<CourseCategory> categories) {
        return categories.stream()
                .anyMatch((c) -> (c.getId().equals(id) || isPresent(id, c.getSubcategories())));
    }

    private List<CourseCategoryDTO> mapSubcategories(List<CourseCategory> categories) {
        return categories.stream()
                .map(this::map)
                .collect(Collectors.toList());
    }

}
