package Lesson6_Maven_BackEnd_test;

import Lesson6_Maven_BackEnd_test.api.ProductService;
import Lesson6_Maven_BackEnd_test.utils.RetrofitUtils;
import lombok.SneakyThrows;
import okhttp3.ResponseBody;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import retrofit2.Response;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class GetAllProductsTest {

    static ProductService productService;

    @BeforeAll
    static void beforeAll() {
        productService = RetrofitUtils.getRetrofit().create(ProductService.class);
    }

    @SneakyThrows
    @Test
    @Tag("Positive")
    @DisplayName("Get all products (Positive)")
    void getCategoryByIdPositiveTest() {
        Response<ResponseBody> response = productService.getProducts().execute();

        assertThat(response.isSuccessful(), CoreMatchers.is(true));
        assertThat(response.code(), equalTo(200));
    }
}
