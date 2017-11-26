package quickbucks;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.Override;

@Controller
public class MyErrorController implements ErrorController {
    private static final String PATH = "/error";

    @Override
    public String getErrorPath() {
       return PATH;
    }

    @RequestMapping(value = PATH)
    public String error() {
        return "redirect:/generic-error.html";
    }
}
