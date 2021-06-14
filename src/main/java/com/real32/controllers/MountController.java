package com.real32.controllers;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.real32.models.Mount;
import com.real32.models.User;
import com.real32.repositories.MountRepository;
import com.real32.services.CustomUserDetailsService;

@Controller
public class MountController {

	@Autowired
	private CustomUserDetailsService userService;
	
	@Autowired
	private MountRepository mountRepository;
	
	@GetMapping(value = "/mount")
	public ModelAndView mounts() {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("mount");
		return modelAndView;
	}
	
	@RequestMapping(value = "/mount/save")
	public  String save(@RequestParam String serial){
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			User user = userService.findUserByEmail(auth.getName());
			Mount mount = new Mount();
			mount.setSerial(serial);
			mount.setManufacturedBy(user.getFullname());
			mount.setManufacturedOn(new Date());
			mountRepository.save(mount);

		return "redirect:/notes";
	}
	
}
