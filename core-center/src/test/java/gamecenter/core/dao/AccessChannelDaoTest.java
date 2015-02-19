package gamecenter.core.dao;

import gamecenter.core.services.db.AccessChannelDBService;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.Reader;

public class AccessChannelDaoTest {

    private AccessChannelDBService accessChannelDBService;

    //    @Test
    public void retrieveAllAccessChannelFromDB() throws Exception {

        //这里获取spring总配置文件
        ApplicationContext aCtx = new ClassPathXmlApplicationContext(
                "classpath:applicationContext.xml");

        accessChannelDBService = (AccessChannelDBService) aCtx.getBean("accessChannelDBService");

        String resource = "jdbc/mybatis-config.xml";
        Reader reader = Resources.getResourceAsReader(resource);
        SqlSessionFactoryBuilder builder = new SqlSessionFactoryBuilder();
        SqlSessionFactory factory = builder.build(reader);
        SqlSession session = factory.openSession();
        AccessChannelDao accessChannelDao = session.getMapper(AccessChannelDao.class);

//        List<String> users = accessChannelDao.selectAll();


    }
}