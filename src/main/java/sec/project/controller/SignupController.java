package sec.project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import sec.project.domain.Signup;
import sec.project.repository.SignupRepository;

@Controller
public class SignupController {

    @Autowired
    private SignupRepository signupRepository;

    @RequestMapping("*")
    public String defaultMapping() {
        return "redirect:/form";
    }

    @RequestMapping(value = "/form", method = RequestMethod.GET)
    public String loadForm() {
        return "form";
    }

    @RequestMapping(value = "/form", method = RequestMethod.POST)
    public String submitForm(@RequestParam String name, @RequestParam String address, @RequestParam Integer vip) {
        boolean vipBool = false;

        if (vip == 1) {
            vipBool = true;
        }

        Signup signup = new Signup(name, address, vipBool);
        signupRepository.save(signup);
        return "redirect:/view/"+ signup.getId();
    }

    @RequestMapping(value = "/view/{id}", method = RequestMethod.GET)
    public String viewSignup( Model model, @PathVariable String id) {
        Signup signup = signupRepository.findOne(Long.parseLong(id));

        model.addAttribute("name", signup.getName());
        model.addAttribute("address", signup.getAddress());
        model.addAttribute("vip", signup.getVip());
        return "view";
    }
}
