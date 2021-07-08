package net.codejava;

import java.io.UnsupportedEncodingException;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import net.bytebuddy.utility.RandomString;

@Controller
public class ForgotPasswordController {

	@Autowired
	private UserService userservice;
	
	@Autowired
	private JavaMailSender mailSender;
	
	@GetMapping("/forgot_password")
	public String showForgotPasswordForm() {
		System.out.println("hi");
	    return "forgot_password_form";
	}
	
	@PostMapping("/forgot_password")
	public String processForgotPassword(HttpServletRequest request, Model model) {
	    String email = request.getParameter("email");
	    String token = RandomString.make(30);
	     
	    try {
	    	userservice.updateResetPasswordToken(token, email);
	        String resetPasswordLink = Utility.getSiteURL(request) + "/reset_password?token=" + token;
	        System.out.println(resetPasswordLink);
	        sendEmail(email, resetPasswordLink);
	        model.addAttribute("message", "We have sent a reset password link to your email. Please check.");
	         
	    } catch (UserNotFoundException ex) {
	        model.addAttribute("error", ex.getMessage());
	    }
	    catch (UnsupportedEncodingException | MessagingException e) {
	        model.addAttribute("error", "Error while sending email");
	    }
	    
	    System.out.println("email:" +email);
	    System.out.println("token:" + token);
	    
	    model.addAttribute("pageTitle", "forgot password");
	    return "forgot_password_form";
	}
	
	
	public void sendEmail(String recipientEmail, String link)
	        throws MessagingException, UnsupportedEncodingException {
	    MimeMessage message = mailSender.createMimeMessage();              
	    MimeMessageHelper helper = new MimeMessageHelper(message);
	     
	    helper.setFrom("contact@sportyshoes.com", "SportyShoe Support");
	    helper.setTo(recipientEmail);
	     
	    String subject = "Here's the link to reset your password";
	     System.out.println(link);
	    String content = "<p>Hello,</p>"
	            + "<p>You have requested to reset your password.</p>"
	            + "<p>Click the link below to change your password:</p>"
	            + "<p><a href=\"" + link + "\">Change my password</a></p>"
	            + "<br>"
	            + "<p>Ignore this email if you do remember your password, "
	            + "or you have not made the request.</p>";
	     
	    helper.setSubject(subject);
	     
	    helper.setText(content, true);
	     
	    mailSender.send(message);
	}
	
	@GetMapping("/reset_password")
	public String showResetPasswordForm(@Param(value = "token") String token, Model model) {
	    User customer = userservice.getByResetPasswordToken(token);
	    model.addAttribute("token", token);
	     
	    if (customer == null) {
	    	model.addAttribute("token", "Reset you Password");
	        model.addAttribute("message", "Invalid Token");
	        return "message";
	    }
	    
        model.addAttribute("token", token);
        model.addAttribute("pageTitle", "Reset Password");
	    return "reset_password_form";
	}
	
	@PostMapping("/reset_password")
	public String processResetPassword(HttpServletRequest request, Model model) {
	    String token = request.getParameter("token");
	    String password = request.getParameter("password");
	     
	    User customer = userservice.getByResetPasswordToken(token);
	    model.addAttribute("title", "Reset your password");
	     
	    if (customer == null) {
	        model.addAttribute("message", "Invalid Token");
	        return "message";
	    } else {           
	    	userservice.updatePassword(customer, password);
	         
	        model.addAttribute("message", "You have successfully changed your password.");
	    }
	     
	    return "message";
	}
}
