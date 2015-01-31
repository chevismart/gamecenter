package gamecenter.core.dao;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Test;

import java.io.Reader;
import java.util.List;

public class AccessChannelDaoTest {
    @Test
    public void retrieveAllAccessChannelFromDB() throws Exception {
        String resource = "jdbc/mybatis-config.xml";
        Reader reader = Resources.getResourceAsReader(resource);
        SqlSessionFactoryBuilder builder = new SqlSessionFactoryBuilder();
        SqlSessionFactory factory = builder.build(reader);
        SqlSession session = factory.openSession();
        AccessChannelDao accessChannelDao = session.getMapper(AccessChannelDao.class);

        List<String> users = accessChannelDao.selectAll();


    }
}