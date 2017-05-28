/**
 * Created by claudiu on 02.04.2017.
 */

package com.mainPackage;

import com.mainPackage.database.ConnectionToDB;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import yahoofinance.Stock;
import yahoofinance.YahooFinance;
import yahoofinance.histquotes.Interval;

import java.io.IOException;
import java.util.Calendar;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = ConnectionToDB.class)
@WebAppConfiguration
public class ConnectionDBTest {

    @Autowired
    ConnectionToDB connectionToDB;

    @Test
    public void test() {
        connectionToDB.testConnection();
    }

//    @Test
//    public void testNextId() {
//        System.out.println(connectionToDB.getNextId());
//    }
//
//    @Test
//    public void getId() {
//        connectionToDB.deleteSharesRow(3, "AAPL");
//    }

//    @Test
//    public void getHistory() throws IOException {
//        System.setProperty("yahoofinance.baseurl.histquotes", "https://ichart.yahoo.com/table.csv");
//        Calendar from = Calendar.getInstance();
//        from.add(Calendar.YEAR, -2);
//        Stock appl = YahooFinance.get("AAPL");
//        appl.getHistory(from, Interval.DAILY);
//
//    }
}
