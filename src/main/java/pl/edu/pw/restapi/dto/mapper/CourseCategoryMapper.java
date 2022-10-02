package pl.edu.pw.restapi.dto.mapper;

import pl.edu.pw.restapi.domain.CourseCategory;
import pl.edu.pw.restapi.dto.CourseCategoryDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CourseCategoryMapper {

    //TODO change static methods to non static in every mapper

    public static List<String> mapNames(List<CourseCategory> categories) {
        if (categories == null) {
            return null;
        } else {
            return categories.stream()
                    .map(CourseCategory::getCategory)
                    .collect(Collectors.toList());
        }
    }

    public static List<CourseCategoryDTO> map(List<CourseCategory> categories) {
        if (categories == null) {
            return null;
        } else {
            return mapDistinct(categories);
        }
    }

    public static CourseCategoryDTO map(CourseCategory category) {
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

    private static List<CourseCategoryDTO> mapDistinct(List<CourseCategory> categories) {
        return categories.stream()
                .filter((c) -> !isPresentInSubcategories(c.getId(), categories))
                .map(CourseCategoryMapper::map)
                .collect(Collectors.toList());
    }

    private static boolean isPresentInSubcategories(long categoryId, List<CourseCategory> categories) {
        return categories.stream()
                .anyMatch((c) -> isPresent(categoryId, c.getSubcategories()));
    }

    private static boolean isPresent(long id, List<CourseCategory> categories) {
        return categories.stream()
                .anyMatch((c) -> (c.getId().equals(id) || isPresent(id, c.getSubcategories())));
    }

    private static List<CourseCategoryDTO> mapSubcategories(List<CourseCategory> categories) {
        return categories.stream()
                .map(CourseCategoryMapper::map)
                .collect(Collectors.toList());
    }

}
