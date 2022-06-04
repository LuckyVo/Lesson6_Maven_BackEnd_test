package Lesson6_Maven_BackEnd_test;

import Lesson6_Maven_BackEnd_test.api.CategoryService;
import Lesson6_Maven_BackEnd_test.dto.CategoryResponse;
import Lesson6_Maven_BackEnd_test.utils.RetrofitUtils;
import lombok.SneakyThrows;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import retrofit2.Response;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class GetCategoryTest {

    static CategoryService categoryService;

    @BeforeAll
    static void beforeAll() {
        categoryService = RetrofitUtils.getRetrofit().create(CategoryService.class);
    }

    @SneakyThrows
    @Test
    @Tag("Positive")
    @DisplayName("Get all products of a category (Positive)")
    void getCategoryByIdPositiveTest() {
        Response<CategoryResponse> response = categoryService.getCategory(1).execute();

        assertThat(response.isSuccessful(), CoreMatchers.is(true));
        assertThat(response.code(), equalTo(200));
        assert response.body() != null;
        assertThat(response.body().getId(), equalTo(1));
        String category1 = response.body().getTitle();
        response.body().getProducts().forEach(product ->
                assertThat(product.getCategoryTitle(), equalTo(category1)));

        response = categoryService.getCategory(2).execute();

        assertThat(response.isSuccessful(), CoreMatchers.is(true));
        assertThat(response.code(), equalTo(200));
        assert response.body() != null;
        assertThat(response.body().getId(), equalTo(2));
        String category2 = response.body().getTitle();
        response.body().getProducts().forEach(product ->
                assertThat(product.getCategoryTitle(), equalTo(category2)));
    }

    @SneakyThrows
    @Test
    @Tag("Negative")
    @DisplayName("Get all products of a category (Negative)")
    void getCategoryByIdNegativeTest() {
        Response<CategoryResponse> response = categoryService.getCategory(3).execute();

        assertThat(response.isSuccessful(), CoreMatchers.is(false));
        assertThat(response.code(), equalTo(404));
    }
}
