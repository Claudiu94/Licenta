package com.mainPackage.Controlers;

import com.mainPackage.database.ConnectionToDB;
import com.mainPackage.util.Greetings;
import com.mainPackage.util.StocksBrief;
import com.mainPackage.util.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.text.ParseException;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by cozafiu on 13.04.2017.
 */
@RestController
public class RestControllerServices {

  @Autowired
  ConnectionToDB connectionToDB;

  private String url = ":/localhost:3000/";
  private static final String template = "Hello, %s!";
  private final AtomicLong counter = new AtomicLong();

  @RequestMapping("/greeting")
  public Greetings greeting(@RequestParam(value="name", defaultValue="World") String name) {
    System.out.println("here!!!");
    return new Greetings(counter.incrementAndGet(),
            String.format(template, name));
  }

  @RequestMapping("/stocks")
  public StocksBrief symbols() throws IOException {
    return new StocksBrief();
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


}
