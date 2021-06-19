package com.real32.controllers;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.real32.models.Mount;
import com.real32.models.Mount.Status;
import com.real32.models.ProductionLog;
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
		modelAndView.addObject("mounts", mountRepository.findAll());
		modelAndView.setViewName("mount");
		return modelAndView;
	}

	@RequestMapping(value = "/mount/save")
	public String save(@RequestParam String serial) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User user = userService.findUserByEmail(auth.getName());
		Mount mount = new Mount();
		mount.setSerial(serial);
		mount.setManufacturedBy(user.getFullname());
		mount.setManufacturedOn(new Date());
		mount.setStatus(Status.AVAILABLE);
		mount.getProductionLog().add(new ProductionLog(ProductionLog.Status.CREATED, user, "Mount Created"));
		mountRepository.save(mount);
		return "redirect:/notes";
	}

	@GetMapping(value = "/mount/destroy")
	public ResponseEntity<String> destroy(@RequestParam String serial) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User user = userService.findUserByEmail(auth.getName());

		Mount targetMount;
		try {
			targetMount = mountRepository.findBySerial(serial);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		if (targetMount.getStatus() != Mount.Status.AVAILABLE) {
			return new ResponseEntity<>("Mount is not available. Mount Status:" + targetMount.getStatus(),
					HttpStatus.BAD_REQUEST);
		}
		targetMount.setStatus(Status.RETIRED);
		targetMount.getProductionLog().add(new ProductionLog(ProductionLog.Status.DESTROYED, user, "Mount Destroyed"));
		mountRepository.save(targetMount);
		return ResponseEntity.ok("Mount:" + targetMount + " retired.");
	}

}
