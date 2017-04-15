package com.mainPackage.Controlers;

import com.mainPackage.util.Greetings;
import com.mainPackage.util.StocksBrief;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.io.IOException;
import java.text.ParseException;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by cozafiu on 13.04.2017.
 */
@RestController
public class RestControllerServices {

  private String url = ":/localhost:3000/";
  private static final String template = "Hello, %s!";
  private final AtomicLong counter = new AtomicLong();

  @RequestMapping("/greeting")
  public Greetings greeting(@RequestParam(value="name", defaultValue="World") String name) {
    return new Greetings(counter.incrementAndGet(),
            String.format(template, name));
  }

  @RequestMapping("/stocks")
  public StocksBrief symbols() throws IOException {
    return new StocksBrief();
  }

  @RequestMapping(value = "/login", method = RequestMethod.POST)
  public RedirectView login(@RequestBody String body) throws ParseException {
    System.out.println("LOGIN!!!!!!!!!!!");
    System.out.println(body.toString());

    RedirectView redirectView = new RedirectView();
    redirectView.setUrl("http://localhost:3000/");
    return redirectView;
  }

  @RequestMapping(value = "/new-account", method = RequestMethod.POST)
  public RedirectView newAccount(@RequestBody String body) throws ParseException {
    System.out.println("CREATE!!!!!!!!!!!");
    System.out.println(body.toString());

    RedirectView redirectView = new RedirectView();
    redirectView.setUrl("http://localhost:3000/");
    return redirectView;
  }


}
