package com.mainPackage;

import com.mainPackage.database.ConnectionToDB;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

/**
 * Created by claudiu on 02.04.2017.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = ConnectionToDB.class)
@WebAppConfiguration
public class ConnectionDBTest {

    @Autowired
    ConnectionToDB connectionToDB;

    @Test
    public void test() {
        connectionToDB.getConnection();
    }

}
