package net.codejava;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class AppController {
	@Autowired
	private ProductService service;
	
	@RequestMapping("/")
	public String viewHomePage(Model model, @Param("keyword") String keyword) {
		List<Product> listProducts = service.listAll(keyword);
		model.addAttribute("listProducts", listProducts);
		model.addAttribute("keyword", keyword);
		
		return "index";
	}
	
	@RequestMapping("/new")
	public String showNewProductForm(Model model) {
		Product product = new Product();
		model.addAttribute("product", product);
		
		return "new_product";
	}
	
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String saveProduct(@ModelAttribute("product") Product product) {
		service.save(product);
		
		return "redirect:/";
	}
	
	@RequestMapping("/edit/{id}")
	public ModelAndView showEditProductForm(@PathVariable(name = "id") Long id) {
		ModelAndView mav = new ModelAndView("edit_product");
		
		Product product = service.get(id);
		mav.addObject("product", product);
		
		return mav;
	}	
//	
//	@RequestMapping("/delete/{id}")
//	public String deleteProduct(@PathVariable(name = "id") Long id) {
//		service.delete(id);
//		
//		return "redirect:/";
//	}
//	
//	@GetMapping("")
//    public String viewHomePage() {
//        return "newusers";
//    }
//	@GetMapping("/login")
//    public String viewLoginPage() {
//        // custom logic before showing login page...
//         
//        return "login";
//    }
	
	@GetMapping("/login")
    public String viewLoginPage() {
        // custom logic before showing login page...
         
        return "login";
    }

//	
//	@RequestMapping(value = "/forgot_password", method = RequestMethod.GET)
//	public ModelAndView displayForgotPasswordPage() {
//		System.out.println("hi");
//		return new ModelAndView("forgot_password_form");
//    }
	
}
