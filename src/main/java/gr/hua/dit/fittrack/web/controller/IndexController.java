package gr.hua.dit.fittrack.web.controller;

import org.springframework.stereotype.Controller; // <--- ΠΡΟΣΘΗΚΗ
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {
    @GetMapping("/")
    public String index() {
        return "index"; //θα ψάξει το αρχείο index.html
    }

    @GetMapping("/about")
    public String showAboutPage() {
        return "about"; // Θα ψάξει το αρχείο about.html
    }
}