package com.mainPackage.Controlers;

import com.mainPackage.database.ConnectionToDB;
import com.mainPackage.util.*;
import org.json.simple.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;
import yahoofinance.Stock;
import yahoofinance.YahooFinance;
import yahoofinance.histquotes.HistoricalQuote;
import yahoofinance.histquotes.Interval;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.text.ParseException;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by cozafiu on 13.04.2017.
 */
@RestController
public class RestControllerServices {

  @Autowired
  ConnectionToDB connectionToDB;
  @Autowired
  StocksBrief stocksBrief;
  @Autowired
  Portofolios portofolios;
  @Autowired
  NotificationCron notificationCron;

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

    boolean historyNull = false;
    Calendar from = Calendar.getInstance();
    from.add(Calendar.YEAR, -2);
    Stock stock;
    try {
      stock = YahooFinance.get(symbol, from, Interval.DAILY);
    } catch (Exception e) {
      stock = YahooFinance.get(symbol);
      historyNull = true;

    }

    JSONArray list = new JSONArray();
    float lastValue = stock.getQuote().getPrice().floatValue();

    if (!historyNull) {
      for (HistoricalQuote quote : stock.getHistory()) {
        JSONArray l = new JSONArray();
//      System.out.println(quote.getDate().toInstant().getEpochSecond() * 1000);
        l.add(quote.getDate().toInstant().getEpochSecond() * 1000);

        if (quote.getClose() != null) {
          lastValue = quote.getClose().floatValue();
        }
        l.add(lastValue);
        list.add(l);
      }
    }
    else {
      float price = stock.getQuote().getPrice().floatValue();
      int i;

      for(i = 0; i < 730; i++) {
        Random random = new Random();
        int randomNumber = (random.nextInt(5) - 3);
        from.add(Calendar.DATE, 1);
        JSONArray l = new JSONArray();
        l.add(from.getTimeInMillis());
        l.add((randomNumber * price)/100 + price);

        list.add(l);
      }
    }

    return list.toJSONString();
  }

  @RequestMapping(value = "/stocks", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
  @CrossOrigin(origins = "http://localhost:3000")
  @ResponseBody
  public StocksBrief symbols() throws IOException {
      return stocksBrief;
  }


  @RequestMapping(value = "/notifications", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
  @CrossOrigin(origins = "http://localhost:3000")
  @ResponseBody
  public List<Alert> notifications(@RequestParam(value="user", required=true) String username) throws IOException {
    List<Alert> alerts = new ArrayList<>();

    if(username != null) {
      int id = connectionToDB.getId(username);

      if(id != -1) {
        List<Map<String, Object>> data = connectionToDB.getAlerts(id);

        for (Map<String, Object> map : data) {
          int app = (Integer) map.get(("App"));

          if (app == 1) {
            String price = (String) map.get("Price");
            int type = (Integer) map.get("Type");
            String symbol = (String) map.get("Symbol");
            float currentPrice = stocksBrief.retreivePriceForSymbol(symbol);
            String company = stocksBrief.retreiveNameForSymbol(symbol);

            if (currentPrice != -1
                    && ((type == 1 && Float.compare(Float.valueOf(price), currentPrice) == -1)
                    || (type == -1 && Float.compare(Float.valueOf(price), currentPrice) == 1))) {

              String message = type == 1 ? "bigger" : "smaller";

              alerts.add(new Alert(symbol, company, price, String.valueOf(currentPrice), message));
            }
          }
        }
      }
    }

    return alerts;
  }

  @RequestMapping(value = "/notifications-seen", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
  @CrossOrigin(origins = "http://localhost:3000")
  @ResponseBody
  public boolean notificationsSeen(@RequestParam(value="user", required=true) String username) throws IOException {
    boolean status = false;

    if(username != null) {
      int id = connectionToDB.getId(username);

      if(id != -1) {
        List<Map<String, Object>> data = connectionToDB.getAlerts(id);

        for (Map<String, Object> map : data) {
          int app = (Integer) map.get(("App"));

          if (app == 1) {
            String price = (String) map.get("Price");
            int type = (Integer) map.get("Type");
            String symbol = (String) map.get("Symbol");
            float currentPrice = stocksBrief.retreivePriceForSymbol(symbol);

            if (currentPrice != -1
                    && ((type == 1 && Float.compare(Float.valueOf(price), currentPrice) == -1)
                    || (type == -1 && Float.compare(Float.valueOf(price), currentPrice) == 1))) {
              int email = (Integer) map.get(("Email"));

              if(email == 0)
                connectionToDB.deleteNotification(id, symbol, price, type);
              else
                connectionToDB.appNotificationSent(id, symbol, price, type);
            }
          }
        }
      }
    }

    return status;
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
  public SharesList portofolio(@RequestParam(value="name", defaultValue="Claudiu_94") String user,
                               @RequestParam(value="portofolio", defaultValue = "Portofolio 1") String portofolio) {

    try {
      return portofolios.retreiveSharesFromDb(user, portofolio);
    } catch (ExecutionException e) {
      return new SharesList(Collections.EMPTY_LIST);
    }
  }


  @RequestMapping(value = "/portofolioNames", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
  @ResponseBody
  @CrossOrigin(origins = "http://localhost:3000")
  public List<String> getPortofolios(@RequestParam(value="name", defaultValue="Claudiu_94") String user) {
    int userId = connectionToDB.getId(user);

    return connectionToDB.getPortofolios(userId);
  }

  @RequestMapping(value = "/create-portofolio", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
  @ResponseBody
  @CrossOrigin(origins = "http://localhost:3000")
  public List<String> addNewPortofolio(@RequestParam(value="user", defaultValue="Claudiu_94") String user,
                                       @RequestParam(value="portofolioName") String portofolio) {
    int userId = connectionToDB.getId(user);

    if (portofolio != null && !portofolio.isEmpty()) {
      connectionToDB.addPortofolio(userId, portofolio);
    }

    return connectionToDB.getPortofolios(userId);
  }

  @RequestMapping(value = "/delete-portofolio", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
  @ResponseBody
  @CrossOrigin(origins = "http://localhost:3000")
  public List<String> deletePortofolio(@RequestParam(value="user", defaultValue="Claudiu_94") String user,
                                       @RequestParam(value="portofolioName") String portofolio) {
    int userId = connectionToDB.getId(user);

    if (portofolio != null && !portofolio.isEmpty()) {
      connectionToDB.deletePortofolio(userId, portofolio);
    }

    return connectionToDB.getPortofolios(userId);
  }

  @RequestMapping(value = "/sellBuyShares/{userName}", method = RequestMethod.POST)
  public RedirectView modifyShares(HttpServletRequest request, @RequestBody String body,
                                   @PathVariable String userName) throws ParseException {

    String type = request.getParameter("type");
    String symbol = request.getParameter("symbol");
    String portofolio = request.getParameter("portofolio");
    String name = stocksBrief.retreiveNameForSymbol(symbol);
    int shares = Integer.valueOf(request.getParameter("sharesNumber"));
    int userId = connectionToDB.getId(userName);
    RedirectView redirectView = new RedirectView();

    redirectView.setContentType("application/json");
    if (portofolio == null || portofolio.isEmpty())
      portofolio = "Portofolio 1";

    System.out.println("User: " + userName + " Symbol: "+ symbol + " Type: " + type + " Shares: " + shares);

    if (type.equals("sell"))
      shares = -1 * shares;

    List<Map<String, Object>> data = connectionToDB.getSharesForSymbolAndPortofolio(userId, symbol, portofolio);

    if (data.size() == 1) {
        Map<String, Object> row = data.get(0);
        int currentShares = (Integer)row.get("Shares") + shares;

        if (currentShares < 0) {
          redirectView.setUrl("http://localhost:3000/error");

          return redirectView;
        } else if (currentShares == 0) {
          connectionToDB.deleteSharesRow(userId, symbol, portofolio);
        } else {
          connectionToDB.updateSharesRow(userId, symbol, currentShares, portofolio);
        }
    } else if (data.size() == 0 && shares > 0) {
      connectionToDB.saveShareRecord(userId, symbol, name, shares, portofolio);
    } else {
      redirectView.setUrl("http://localhost:3000/error");

      return redirectView;
    }

    portofolios.invalidatePortofoliosCache();
    redirectView.setUrl("http://localhost:3000/portofolio");

    return redirectView;
  }

  @RequestMapping(value = "/move-shares", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
  @CrossOrigin(origins = "http://localhost:3000")
  public boolean moveShares(@RequestParam(value="name", defaultValue="Claudiu_94") String user,
                                 @RequestParam(value="from") String from,
                                 @RequestParam(value = "to") String to,
                                 @RequestParam(value = "symbol") String sym,
                                 @RequestParam(value = "shares") int shares) {
    int userId = connectionToDB.getId(user);

    if (user == null || from == null || to == null
            || sym == null || shares == 0) {
      return false;
    }

    //update from portofolio
    List<Map<String, Object>> data = connectionToDB.getSharesForSymbolAndPortofolio(userId, sym, from);

    if (data.size() == 1) {
      Map<String, Object> row = data.get(0);
      int currentShares = (Integer) row.get("Shares") - shares;

      if (currentShares < 0) {
        return false;
      } else if (currentShares == 0) {
        connectionToDB.deleteSharesRow(userId, sym, from);
      } else {
        connectionToDB.updateSharesRow(userId, sym, currentShares, from);
      }
    }

    //update to portofolio
    data = connectionToDB.getSharesForSymbolAndPortofolio(userId, sym, to);

    if (data.size() == 1) {
      Map<String, Object> row = data.get(0);
      int currentShares = (Integer) row.get("Shares") + shares;

      if (currentShares < 0) {
        return false;
      } else if (currentShares == 0) {
        connectionToDB.deleteSharesRow(userId, sym, to);
      } else {
        connectionToDB.updateSharesRow(userId, sym, currentShares, to);
      }
    }
    else if (data.size() == 0 && shares > 0) {
      connectionToDB.saveShareRecord(userId, sym, stocksBrief.retreiveNameForSymbol(sym), shares, to);
    }

    portofolios.invalidatePortofoliosCache();

    return true;
  }

  @RequestMapping(value = "/create-notification", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
  @ResponseBody
  @CrossOrigin(origins = "http://localhost:3000")
  public boolean createNotification(@RequestParam(value="user", defaultValue="Claudiu_94") String user,
                                       @RequestParam(value="symbol") String symbol,
                                       @RequestParam(value="type") int type,
                                       @RequestParam(value="price") String price) {

    if (user == null || symbol == null || price == null)
      return false;

    return connectionToDB.addNotification(connectionToDB.getId(user), type, symbol, price);
  }
//  Share getShare(String symbol) {
//    return shareList.stream()
//            .filter(share -> share.getName().equals(symbol))
//            .findFirst()
//            .orElse(null);
//  }

}
