package org.launchcode.blogz.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.launchcode.blogz.models.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class AuthenticationController extends AbstractController {
	
	@RequestMapping(value = "/signup", method = RequestMethod.GET)
	public String signupForm() {
		return "signup";
	}
	
	@RequestMapping(value = "/signup", method = RequestMethod.POST)
	public String signup(HttpServletRequest request, Model model) {
		
		// TODO - implement signup
		
		//Get parameters from request
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		String verify = request.getParameter("verify");
		
		//validate parameters (username, password, verify)
		if(!User.isValidUsername(username))
		{
			model.addAttribute("username_error", "That is not a valid username!");
			return "signup";
		}
		
		if(!User.isValidPassword(password))
		{
			model.addAttribute("password_error", "That is not a valid password!");
			return "signup";
		}
		
		//make sure passwords match		
		if(!verify.equals(password))
		{
			model.addAttribute("verify_error", "The passwords do not match!");
			return "signup";
		}
		
		//if they validate, create a new user, and put them in the session
		User user = new User(username, password);
		userDao.save(user);
		
		HttpSession thisSession = request.getSession();
		setUserInSession(thisSession, user);
		
		return "redirect:blog/newpost";
	}
	
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String loginForm() {
		return "login";
	}
	
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public String login(HttpServletRequest request, Model model) {
		
		// TODO - implement login
		
		//get parameters from request
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		
		//get user by their username
		User user = userDao.findByUsername(username);
		if(user == null)
		{
			model.addAttribute("error", "That username does not exist!");
			return "login";
		}
		
		//check password is correct
		if(!user.isMatchingPassword(password))
		{
			model.addAttribute("error", "The password you entered is incorrect!");
			return "login";
		}
		
		//log them in by setting them in session
		HttpSession thisSession = request.getSession();
		setUserInSession(thisSession, user);
		return "redirect:blog/newpost";
	}
	
	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	public String logout(HttpServletRequest request){
        request.getSession().invalidate();
		return "redirect:/";
	}
}
