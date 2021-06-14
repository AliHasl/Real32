package com.real32.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import com.real32.repositories.Real32UnitRepository;
import com.real32.services.CustomUserDetailsService;

@Controller
public class Real32UnitController {

	@Autowired
	private CustomUserDetailsService userService;
	
	@Autowired
	private Real32UnitRepository real32UnitRespository;
	
	@GetMapping(value = "/Real32")
	public ModelAndView real32Unit()
	{
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("Real32");
		return modelAndView;
	}
}