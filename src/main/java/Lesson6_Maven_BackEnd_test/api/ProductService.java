package Lesson6_Maven_BackEnd_test.api;

import Lesson6_Maven_BackEnd_test.dto.Product;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.*;

public interface ProductService {

    @GET("products")
    Call<ResponseBody> getProducts();

    @POST("products")
    Call<Product> createProduct(@Body Product createProductRequest);

    @PUT("products")
    Call<Product> modifyProduct(@Body Product modifyProductRequest);

    @GET("products/{id}")
    Call<Product> getProductById(@Path("id") int id);

    @DELETE("products/{id}")
    Call<ResponseBody> deleteProduct(@Path("id") int id);
}
