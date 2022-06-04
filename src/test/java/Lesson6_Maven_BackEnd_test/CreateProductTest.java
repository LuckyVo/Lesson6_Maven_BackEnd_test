package Lesson6_Maven_BackEnd_test;

import com.github.javafaker.Faker;
import Lesson6_Maven_BackEnd_test.api.ProductService;
import Lesson6_Maven_BackEnd_test.dto.Product;
import Lesson6_Maven_BackEnd_test.utils.RetrofitUtils;
import lombok.SneakyThrows;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.*;
import retrofit2.Response;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class CreateProductTest {
    static SqlSession session;

    static ProductService productService;
    Product product = null;
    Faker faker = new Faker();
    String category;
    int id;

    @BeforeAll
    static void beforeAll() throws IOException {

        productService = RetrofitUtils.getRetrofit().create(ProductService.class);

        session = null;
        String resource = "mybatis-config.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        session = sqlSessionFactory.openSession();
    }

    void setUp() {
        product = new Product()
                .withTitle(faker.food().ingredient())
                .withPrice((int) (Math.random() * 10000))
                .withCategoryTitle(category);
    }

    @Test
    @Tag("Positive")
    @DisplayName("Product creation (Positive)")
    void createProductPositiveTest() throws IOException {
        category = "Food";
        setUp();
        Response<Product> response = productService.createProduct(product).execute();

        assertThat(response.code(), equalTo(201));
        assert response.body() != null;
        assertThat(response.body().getCategoryTitle(), equalTo(category));
        assertThat(response.isSuccessful(), CoreMatchers.is(true));

        id =  response.body().getId();

        Lesson6_Maven_BackEnd_test.db.dao.ProductsMapper productsMapper
                = session.getMapper(Lesson6_Maven_BackEnd_test.db.dao.ProductsMapper.class);
        Lesson6_Maven_BackEnd_test.db.dao.CategoriesMapper categoriesMapper
                = session.getMapper(Lesson6_Maven_BackEnd_test.db.dao.CategoriesMapper.class);

        //ищем созданный продукт по ID
        Lesson6_Maven_BackEnd_test.db.model.Products selected = productsMapper.selectByPrimaryKey((long) id);

        //ищем изменяемую категорию по наименованию
        Lesson6_Maven_BackEnd_test.db.model.CategoriesExample example
                = new Lesson6_Maven_BackEnd_test.db.model.CategoriesExample();
        example.createCriteria().andTitleLike(response.body().getCategoryTitle());
        List<Lesson6_Maven_BackEnd_test.db.model.Categories> list = categoriesMapper.selectByExample(example);
        Lesson6_Maven_BackEnd_test.db.model.Categories categories = list.get(0);
        Long category_id = categories.getId();

        //проверяем реквизиты созданного продукта
        assertThat(selected.getTitle(), equalTo(response.body().getTitle()));
        assertThat(selected.getPrice(), equalTo(response.body().getPrice()));
        assertThat(selected.getCategory_id(), equalTo(category_id));

        //удаляем созданный продукт
        tearDown();

        category = "Electronic";
        setUp();
        response = productService.createProduct(product).execute();

        assertThat(response.code(), equalTo(201));
        assert response.body() != null;
        assertThat(response.body().getCategoryTitle(), equalTo(category));
        assertThat(response.isSuccessful(), CoreMatchers.is(true));

        id = response.body().getId();

        //ищем созданный продукт по ID
        selected = productsMapper.selectByPrimaryKey((long) id);

        //ищем изменяемую категорию по наименованию
        example = new Lesson6_Maven_BackEnd_test.db.model.CategoriesExample();
        example.createCriteria().andTitleLike(response.body().getCategoryTitle());
        list = categoriesMapper.selectByExample(example);
        categories = list.get(0);
        category_id = categories.getId();

        //проверяем реквизиты созданного продукта
        assertThat(selected.getTitle(), equalTo(response.body().getTitle()));
        assertThat(selected.getPrice(), equalTo(response.body().getPrice()));
        assertThat(selected.getCategory_id(), equalTo(category_id));

        //удаляем созданный продукт
        tearDown();
    }

    @SneakyThrows
    void tearDown() {
        Lesson6_Maven_BackEnd_test.db.dao.ProductsMapper productsMapper
                = session.getMapper(Lesson6_Maven_BackEnd_test.db.dao.ProductsMapper.class);

        //ищем созданный продукт по ID
        Lesson6_Maven_BackEnd_test.db.model.Products selected = productsMapper.selectByPrimaryKey((long) id);

        //удаляем созданный продукт по ID
        productsMapper.deleteByPrimaryKey(selected.getId());
        session.commit();
    }

    @AfterAll
    static void afterAll() {
        session.close();
    }
}
