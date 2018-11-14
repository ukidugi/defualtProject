package com.wan.basis;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wan.basis.dto.user;

/**
 * Handles requests for the application home page.
 */
@Controller
public class HomeController {
	@Autowired
	CustomUserDetailsService customeUserDetailsService;

	@Autowired
	BCryptPasswordEncoder passwordEncoder;


	@Autowired
	UserDao dao;

	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(Locale locale, Model model) {
		logger.info("Welcome home! The client locale is {}.", locale);

		Date date = new Date();
		DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, locale);

		String formattedDate = dateFormat.format(date);
		model.addAttribute("serverTime", formattedDate );

		return "redirect:/user";
	}

	@RequestMapping(value = "/user", method = RequestMethod.GET)
	public String login(Locale locale, Model model) {
		logger.info("Welcome home! The client locale is {}.", locale);

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String username = auth.getName();


		model.addAttribute("username",username);

		Date date = new Date();
		DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, locale);
		String formattedDate = dateFormat.format(date);
		model.addAttribute("serverTime", formattedDate );

		return "security/user";
	}

	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String loginForm(Locale locale, Model model) {
		logger.info("Welcome home! The client locale is {}.", locale);

		Date date = new Date();
		DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, locale);
		String formattedDate = dateFormat.format(date);
		model.addAttribute("serverTime", formattedDate );

		return "security/loginForm";
	}

	@ResponseBody
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public String createUser(Locale locale, Model model,String userId,String userPassword) {
		logger.info("create()");

		logger.info(passwordEncoder.encode(userPassword));
		dao.createUser(userId, passwordEncoder.encode(userPassword));

		return "security/user";
	}

	@ResponseBody
	@RequestMapping(value = "/home", method = RequestMethod.POST)
	public String homeAs(Locale locale, Model model) {
		logger.info("create()");


		return "/home";
	}


}
