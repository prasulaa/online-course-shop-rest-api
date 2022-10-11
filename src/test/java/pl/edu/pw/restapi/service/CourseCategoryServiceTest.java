package pl.edu.pw.restapi.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.edu.pw.restapi.domain.CourseCategory;
import pl.edu.pw.restapi.dto.CourseCategoryDTO;
import pl.edu.pw.restapi.dto.mapper.CourseCategoryMapper;
import pl.edu.pw.restapi.repository.CourseCategoryRepository;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CourseCategoryServiceTest {

    @Spy
    private CourseCategoryMapper courseCategoryMapper;
    @Mock
    private CourseCategoryRepository courseCategoryRepository;
    @InjectMocks
    private CourseCategoryServiceImpl courseCategoryService;


    @Test
    public void shouldReturnDTOListWhenCategoriesArePresentInRepository() {
        CourseCategory courseCategory1 = new CourseCategory(1L, "Category1", List.of());
        CourseCategory courseCategory2 = new CourseCategory(2L, "Category2", List.of());
        CourseCategory courseCategory3 = new CourseCategory(3L, "Category3", List.of());
        List<CourseCategory> categoriesFromRepo = List.of(courseCategory1, courseCategory2, courseCategory3);
        when(courseCategoryRepository.findAll())
                .thenReturn(categoriesFromRepo);

        List<CourseCategoryDTO> actualCategories = courseCategoryService.getCategories();

        assertAll(
                () -> assertEquals(categoriesFromRepo.size(), actualCategories.size()),
                () -> assertEqualsCategories(categoriesFromRepo.get(0), actualCategories.get(0)),
                () -> assertEqualsCategories(categoriesFromRepo.get(1), actualCategories.get(1)),
                () -> assertEqualsCategories(categoriesFromRepo.get(2), actualCategories.get(2))
        );
    }

    @Test
    public void shouldReturnEmptyListWhenRepositoryIsEmpty() {
        List<CourseCategory> categoriesFromRepo = List.of();
        when(courseCategoryRepository.findAll())
                .thenReturn(categoriesFromRepo);

        List<CourseCategoryDTO> actualCategories = courseCategoryService.getCategories();

        assertEquals(0, actualCategories.size());
    }

    @Test
    public void shouldReturnCategoriesWithSubcategoriesWhenSubcategoriesArePresentInRepository() {
        CourseCategory subcategory = new CourseCategory(1L, "Subcategory", List.of());
        CourseCategory category1 = new CourseCategory(2L, "Category1", List.of());
        CourseCategory category2 = new CourseCategory(3L, "Category2", List.of(subcategory));
        List<CourseCategory> categoriesFromRepo = List.of(category1, category2, subcategory);
        when(courseCategoryRepository.findAll())
                .thenReturn(categoriesFromRepo);

        List<CourseCategoryDTO> actualCategories = courseCategoryService.getCategories();

        assertAll(
                () -> assertEquals(2, actualCategories.size()),
                () -> assertEqualsCategories(categoriesFromRepo.get(0), actualCategories.get(0)),
                () -> assertEqualsCategories(categoriesFromRepo.get(1), actualCategories.get(1))
        );
    }

    @Test
    public void shouldReturnCategoriesWhenIdsAreGiven() {
        List<CourseCategory> categoriesFromRepo = List.of(
                new CourseCategory(1L, "category1", null),
                new CourseCategory(2L, "category2", null),
                new CourseCategory(3L, "category3", null)
        );
        List<Long> givenIds = List.of(1L, 3L);

        when(courseCategoryRepository.findAll())
                .thenReturn(categoriesFromRepo);

        List<CourseCategory> actualCategories = courseCategoryService.getCategories(givenIds);

        assertAll(
                () -> assertEquals(givenIds.size(), actualCategories.size()),
                () -> assertEqualsCategories(categoriesFromRepo.get(0), actualCategories.get(0)),
                () -> assertEqualsCategories(categoriesFromRepo.get(2), actualCategories.get(1))
        );
    }

    @Test
    public void shouldThrowIllegalArgumentExceptionWhenIdIsNotInRepository() {
        List<CourseCategory> categoriesFromRepo = List.of(
                new CourseCategory(1L, "category1", null),
                new CourseCategory(2L, "category2", null),
                new CourseCategory(3L, "category3", null)
        );
        List<Long> givenIds = List.of(1L, 4L);

        when(courseCategoryRepository.findAll())
                .thenReturn(categoriesFromRepo);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> courseCategoryService.getCategories(givenIds)
        );
        assertEquals("Category not found", exception.getMessage());
    }

    private void assertEqualsCategories(CourseCategory expected, CourseCategory actual) {
        assertAll(
                () -> assertEquals(expected.getId(), actual.getId()),
                () -> assertEquals(expected.getCategory(), actual.getCategory())
        );
    }

    private void assertEqualsCategories(CourseCategory expected, CourseCategoryDTO actual) {
        assertAll(
                () -> assertEquals(expected.getId(), actual.getId()),
                () -> assertEquals(expected.getCategory(), actual.getName()),
                () -> assertEqualsSubcategories(expected.getSubcategories(), actual.getSubcategories())
        );
    }

    private void assertEqualsSubcategories(List<CourseCategory> expected, List<CourseCategoryDTO> actual) {
        int expectedSize = expected.size();

        assertEquals(expectedSize, actual.size());

        for (int i = 0; i < expectedSize; i++) {
            assertEqualsCategories(expected.get(i), actual.get(i));
        }
    }

}