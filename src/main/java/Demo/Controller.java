package Demo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {
    @Value("${test}")
    private String ok;

    @RequestMapping("/person")
    public Person person(@RequestParam(value="name") String name) {
        if (ok == null) {
            ok = System.getenv("test");
        }
        return new Person(1, ok);
    }
}
