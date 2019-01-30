package com.wan.basis;

import java.sql.Connection;

import javax.inject.Inject;
import javax.sql.DataSource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"file:src/main/webapp/WEB-INF/spring/*.xml"})
public class JDBCConnectTest {
	private static final Logger logger = LoggerFactory.getLogger(JDBCConnectTest.class);

	@Inject
	DataSource ds;

	@Test
	public void test() throws Exception {
		try (Connection con = ds.getConnection()) {

            System.out.println("\n >>>>>>>>>> Connection  123123 출력 : " + con + "\n");
 
        } catch (Exception e) {
            e.printStackTrace();
        }
	}

}

