package pl.edu.pw.restapi.dto.mapper;

import pl.edu.pw.restapi.domain.CourseCategory;
import pl.edu.pw.restapi.dto.CourseCategoryDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CourseCategoryMapper {

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

    private static List<CourseCategoryDTO> mapDistinct(List<CourseCategory> categories) {
        int size = categories.size();
        List<CourseCategoryDTO> mappedCategories = new ArrayList<>();

        for (int i = 0; i < size; i++) {
            if (!isPresent(i, categories)) {
                CourseCategoryDTO mappedCategory = map(categories.get(i));
                mappedCategories.add(mappedCategory);
            }
        }

        return mappedCategories;
    }

    private static boolean isPresent(int index, List<CourseCategory> categories) {
        int size = categories.size();
        long categoryId = categories.get(index).getId();

        for (int i = index + 1; i < size; i++) {
            if (isPresent(categoryId, categories.get(i).getSubcategories())) {
                return true;
            }
        }
        return false;
    }

    private static boolean isPresent(long id, List<CourseCategory> categories) {
        for (CourseCategory c : categories) {
            if (c.getId().equals(id)) {
                return true;
            } else if (isPresent(id, c.getSubcategories())) {
                return true;
            }
        }
        return false;
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

    private static List<CourseCategoryDTO> mapSubcategories(List<CourseCategory> categories) {
        return categories.stream()
                .map(CourseCategoryMapper::map)
                .collect(Collectors.toList());
    }

}
