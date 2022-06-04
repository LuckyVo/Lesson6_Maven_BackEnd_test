package Lesson6_Maven_BackEnd_test;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.io.InputStream;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class ChangeCategoryTest {
    static SqlSession session;

    Long category_id;
    String category;
    String categoryNew;

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
    @DisplayName("Change category (Positive)")
    void changeCategoryPositiveTest() {

        category_id = 1L;
        categoryNew = "test";

        Lesson6_Maven_BackEnd_test.db.dao.CategoriesMapper categoriesMapper
                = session.getMapper(Lesson6_Maven_BackEnd_test.db.dao.CategoriesMapper.class);

        //ищем изменяемую категорию по ID
        Lesson6_Maven_BackEnd_test.db.model.Categories selected = categoriesMapper.selectByPrimaryKey(category_id);

        //получаем переменные по реквизитам найденной категории
        category = selected.getTitle();

        //проверяем отсутствие совпадений реквизитов
        assertThat(category != categoryNew, is(true));

        //меняем реквизиты найденной категории
        selected.setTitle(categoryNew);
        categoriesMapper.updateByPrimaryKey(selected);
        session.commit();

        //проверяем изменение реквизитов
        assertThat(selected.getTitle(), equalTo(categoryNew));

        //возвращаем измененные реквизиты
        selected.setTitle(category);
        categoriesMapper.updateByPrimaryKey(selected);
        session.commit();

        //проверяем изменение реквизитов
        assertThat(selected.getTitle(), equalTo(category));

    }

    @AfterAll
    static void afterAll() {
        session.close();
    }
}
