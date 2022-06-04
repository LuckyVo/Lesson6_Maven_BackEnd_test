package Lesson6_Maven_BackEnd_test.api;

import Lesson6_Maven_BackEnd_test.dto.CategoryResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface CategoryService {

    @GET("categories/{id}")
    Call<CategoryResponse> getCategory(@Path("id") int id);
}
