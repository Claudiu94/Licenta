package com.mainPackage.Controlers;

import com.mainPackage.database.ConnectionToDB;
import com.mainPackage.util.Greetings;
import com.mainPackage.util.JsonParser;
import com.mainPackage.util.StocksBrief;
import com.mainPackage.util.User;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;
import yahoofinance.Stock;
import yahoofinance.YahooFinance;
import yahoofinance.histquotes.HistoricalQuote;
import yahoofinance.histquotes.Interval;

import javax.enterprise.inject.Produces;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.text.ParseException;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by cozafiu on 13.04.2017.
 */
@RestController
public class RestControllerServices {

  @Autowired
  ConnectionToDB connectionToDB;

  @Autowired
  JsonParser jsonParser;

  private String url = ":/localhost:3000/";
  private static final String template = "Hello, %s!";
  private final AtomicLong counter = new AtomicLong();



  @RequestMapping(value = "/greeting", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
  @ResponseBody
  @CrossOrigin(origins = "http://localhost:3000")
  public Greetings greeting(@RequestParam(value="name", defaultValue="World") String name) {
    System.out.println("here!!!");
//    jsonParser.getSymbols();
    return new Greetings(counter.incrementAndGet(),
            String.format(template, name));
  }

  @RequestMapping(value = "/history", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
  @ResponseBody
  @CrossOrigin(origins = "http://localhost:3000")
  public String getHistory(@RequestParam(value="symbol", required=false) String symbol) throws IOException {
    System.out.println("History symbol: " + symbol);

    if (symbol == null)
      symbol = "AAPL";

    Calendar from = Calendar.getInstance();
    from.add(Calendar.YEAR, -2);
    Stock appl = YahooFinance.get(symbol, from, Interval.DAILY);
    JSONArray list = new JSONArray();

    for (HistoricalQuote quote : appl.getHistory()) {
      JSONArray l = new JSONArray();
//      System.out.println(quote.getDate().toInstant().getEpochSecond() * 1000);
      l.add(quote.getDate().toInstant().getEpochSecond() * 1000);
      l.add(quote.getClose().floatValue());
      list.add(l);
    }
    Collections.reverse(list);

    return list.toJSONString();
  }

  @RequestMapping(value = "/stocks", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
  @CrossOrigin(origins = "http://localhost:3000")
  @ResponseBody
  public StocksBrief symbols() throws IOException {
    return new StocksBrief(jsonParser.getSymbols());
  }

  @RequestMapping(value = "/login", method = RequestMethod.POST)
  public RedirectView login(HttpServletRequest request, @RequestBody String body) throws ParseException {
    System.out.println("LOGIN!!!!!!!!!!!");
    System.out.println(request.getParameter("username"));
    System.out.println(request.getParameter("password"));

    RedirectView redirectView = new RedirectView();
    User user;
    String username = request.getParameter("username");
    String password = request.getParameter("password");

    user = connectionToDB.getRecord(username);
    redirectView.setUrl("http://localhost:3000/");
    redirectView.setContentType("application/json");

    if (user == null) {
      redirectView.addStaticAttribute("err", "user_not_found");
    }
    else if (!user.getPassword().equals(password)){
      redirectView.addStaticAttribute("err", "incorrect_password");
    }
    else {
      redirectView.addStaticAttribute("username", username);
    }

    return redirectView;
  }

  @RequestMapping(value = "/new-account", method = RequestMethod.POST)
  public RedirectView newAccount(HttpServletRequest request, @RequestBody String body) throws ParseException {
    int id;
    String firstName, lastName, userName, email, password;

    System.out.println("CREATE!!!!!!!!!!!");
    System.out.println(request.getParameter("first_name"));
    System.out.println(request.getParameter("last_name"));

    firstName = request.getParameter("first_name");
    lastName = request.getParameter("last_name");
    userName = request.getParameter("username");
    email = request.getParameter("email_address");
    password = request.getParameter("password");
    id = connectionToDB.getNextId();

    connectionToDB.saveRecord(id, firstName, lastName, email, userName, password);

    RedirectView redirectView = new RedirectView();
    redirectView.setUrl("http://localhost:3000/");
    redirectView.setContentType("application/json");

    return redirectView;
  }

  @RequestMapping(value = "/portofolio", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
  @ResponseBody
  @CrossOrigin(origins = "http://localhost:3000")
  public String portofolio(@RequestParam(value="name", defaultValue="Claudiu_94") String user) {
    int userId = connectionToDB.getId(user);
    List<Map<String, Object>> data = connectionToDB.getShares(userId);

    for (Map<String, Object> map : data) {
      
    }
    return null;
  }

}
