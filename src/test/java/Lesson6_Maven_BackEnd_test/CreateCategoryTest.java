package Lesson6_Maven_BackEnd_test;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class CreateCategoryTest {
    static SqlSession session;

    String category;

    @BeforeAll
    static void beforeAll() throws IOException {
        session = null;
        String resource = "mybatis-config.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        session = sqlSessionFactory.openSession();
    }

    @Test
    @Tag("Positive")
    @DisplayName("Category creation (Positive)")
    void createCategoryPositiveTest() {

        category = "test";

        Lesson6_Maven_BackEnd_test.db.dao.CategoriesMapper categoriesMapper
                = session.getMapper(Lesson6_Maven_BackEnd_test.db.dao.CategoriesMapper.class);

        //ищем каегорию по наименованию
        Lesson6_Maven_BackEnd_test.db.model.CategoriesExample categoriesExample
                = new Lesson6_Maven_BackEnd_test.db.model.CategoriesExample();
        categoriesExample.createCriteria().andTitleLike(category);
        List<Lesson6_Maven_BackEnd_test.db.model.Categories> list = categoriesMapper.selectByExample(categoriesExample);

        //проверяем отсутствие категорий
        assertThat(list.size(), equalTo(0));

        //создаем категорию
        Lesson6_Maven_BackEnd_test.db.model.Categories categories
                = new Lesson6_Maven_BackEnd_test.db.model.Categories();
        categories.setTitle(category);
        categoriesMapper.insert(categories);
        session.commit();

        //находим созданную категорию
        categoriesExample = new Lesson6_Maven_BackEnd_test.db.model.CategoriesExample();
        categoriesExample.createCriteria().andTitleLike(category);
        list = categoriesMapper.selectByExample(categoriesExample);
        Lesson6_Maven_BackEnd_test.db.model.Categories selected = list.get(0);

        //проверяем наименование
        assertThat(selected.getTitle(), equalTo(category));

        //удаляем созданную категорию
        categoriesMapper.deleteByPrimaryKey(selected.getId());
        session.commit();
    }

    @AfterAll
    static void afterAll() {
        session.close();
    }
}
