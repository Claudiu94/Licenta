package mainservices;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by claudiu on 07.04.2017.
 */
@RestController
public class GreetingController {

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

