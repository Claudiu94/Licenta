package com.mainPackage.Controlers;

import com.mainPackage.util.Greetings;
import com.mainPackage.util.StocksBrief;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by cozafiu on 13.04.2017.
 */
@RestController
public class RestControllerServices {

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

}
