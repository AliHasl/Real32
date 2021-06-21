package com.real32.controllers;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;

import javax.validation.ConstraintViolationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.real32.models.Mount;
import com.real32.models.ProductionLog;
import com.real32.models.Real32Unit;
import com.real32.models.User;
import com.real32.models.ProductionLog.Status;
import com.real32.repositories.MountRepository;
import com.real32.repositories.Real32UnitRepository;
import com.real32.services.CustomUserDetailsService;

@Controller
public class Real32UnitController {

	@Autowired
	private CustomUserDetailsService userService;

	@Autowired
	private Real32UnitRepository real32UnitRepository;

	@Autowired
	private MountRepository mountRepository;

	@GetMapping(value = "/real32")
	public ModelAndView real32Unit() {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("real32Units", real32UnitRepository.findAll());
		modelAndView.addObject("mounts", mountRepository.findAll());
		modelAndView.setViewName("Real32");
		return modelAndView;
	}

	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<Object> handleConstraintViolationException(ConstraintViolationException ex,
			WebRequest request) {
		return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
	}

	@GetMapping(value = "/real32/save")
	public ResponseEntity<String> Save(@RequestParam String serial, @RequestParam String mountASerial,
			@RequestParam String mountBSerial) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User user = userService.findUserByEmail(auth.getName());
		Real32Unit real32Unit = new Real32Unit();

		Mount mountA = mountRepository.findBySerial(mountASerial);
		Mount mountB = mountRepository.findBySerial(mountBSerial);

		real32Unit.setSerial(serial);
		real32Unit.setAssembledBy(user);
		real32Unit.setAssembledOn(new Date());
		real32Unit.setMountA(mountA);
		real32Unit.setMountB(mountB);
		real32Unit.getProductionLog().add(new ProductionLog(Status.CREATED, user, "Real32Unit Created"));
		real32Unit.getProductionLog().add(new ProductionLog(Status.INSTALLED, user, mountA, "Mount A Installed"));
		real32Unit.getProductionLog().add(new ProductionLog(Status.INSTALLED, user, mountB, "Mount B Installed"));
		mountA.getProductionLog().add(new ProductionLog(Status.INSTALLED, user, real32Unit, "Installed in Mount A"));
		mountB.getProductionLog().add(new ProductionLog(Status.INSTALLED, user, real32Unit, "Installed in Mount B"));
		real32UnitRepository.save(real32Unit);

		return ResponseEntity.ok("Real32 Unit: " + serial + "\nMount A: " + mountA.getSerial() + "\nMountB: "
				+ mountB.getSerial() + "\nCreated.");
	}

	@GetMapping(value = "/real32/show")
	public ResponseEntity<String> Show(@RequestParam String serial) throws IOException {
		Real32Unit real32Unit = real32UnitRepository.findBySerial(serial);
		Mount mountA = (Mount) real32Unit.getMountA();
		Mount mountB = (Mount) real32Unit.getMountB();

		HashMap<String, Object> jsonMap = new HashMap<>();
		jsonMap.put("mountA", mountA.getSerial());
		jsonMap.put("mountB", mountB.getSerial());
		jsonMap.put("operator", real32Unit.getAssembledBy());
		jsonMap.put("installationDate", real32Unit.getAssembledOn());

		return ResponseEntity.ok(new ObjectMapper().writeValueAsString(jsonMap));
	}

}
