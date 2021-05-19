package com.real32.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import com.real32.models.Mount;
import com.real32.models.User;
import com.real32.repositories.MountRepository;
import com.real32.services.CustomUserDetailsService;

public class MountController {

	@Autowired
	private CustomUserDetailsService userService;
	
	@Autowired
	private MountRepository mountRepository;
	
	@GetMapping(value = "/mount")
	public ModelAndView mounts() {
		ModelAndView modelAndView = new ModelAndView();
		return modelAndView;
	}
	
	@GetMapping("/mount/create")
	public ModelAndView create() {
		ModelAndView modelAndView = new ModelAndView();
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User user = userService.findUserByEmail(auth.getName());
		Mount mount = new Mount("1");	//Read form for serial number here
		mountRepository.save(mount);
		return modelAndView;
	}
	
}
