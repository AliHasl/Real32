package com.real32.controllers;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.real32.models.Notes;
import com.real32.models.User;
import com.real32.repositories.NotesRepository;
import com.real32.services.CustomUserDetailsService;

@Controller
public class NotesController {

	@Autowired
	private CustomUserDetailsService userService;

	@Autowired
	private NotesRepository noteRepository;

	
	//Send to mount page
	@RequestMapping(value = "/mount", method = RequestMethod.GET)
	public ModelAndView mount() {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("mount");
		return modelAndView;
	}
	
	
	@RequestMapping(value = "/notes", method = RequestMethod.GET)
	public ModelAndView notes() {
		ModelAndView modelAndView = new ModelAndView();
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User user = userService.findUserByEmail(auth.getName());
		modelAndView.addObject("notes", noteRepository.findByAuthor(auth.getName()));
		modelAndView.addObject("currentUser", user);
		modelAndView.addObject("fullName", "Welcome " + user.getFullname());
		modelAndView.addObject("adminMessage", "Content Available Only for Users with Admin Role");
		modelAndView.setViewName("notes");
		return modelAndView;
	}

	@RequestMapping("/notes/create")
	public ModelAndView create() {
		ModelAndView modelAndView = new ModelAndView();
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User user = userService.findUserByEmail(auth.getName());
		modelAndView.addObject("currentUser", user);
		modelAndView.addObject("fullName", "Welcome " + user.getFullname());
		modelAndView.addObject("adminMessage", "Content Available Only for Users with Admin Role");
		modelAndView.setViewName("create");
		return modelAndView;
	}

	@RequestMapping("/notes/save")
	public String save(@RequestParam String title, @RequestParam String content) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String userName = auth.getName();
		Notes note = new Notes();
		note.setAuthor(userName);
		note.setTitle(title);
		note.setContent(content);
		note.setUpdated(new Date());
		noteRepository.save(note);

		return "redirect:/notes/show/" + note.getId();
	}

	@RequestMapping("/notes/show/{id}")
	public ModelAndView show(@PathVariable Long id) {
		ModelAndView modelAndView = new ModelAndView();
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User user = userService.findUserByEmail(auth.getName());
		modelAndView.addObject("currentUser", user);
		modelAndView.addObject("fullName", "Welcome " + user.getFullname());
		modelAndView.addObject("adminMessage", "Content Available Only for Users with Admin Role");
		modelAndView.addObject("note", noteRepository.findById(id).orElse(null));
		modelAndView.setViewName("show");
		return modelAndView;
	}

	@RequestMapping("/notes/delete")
	public String delete(@RequestParam Long id) {
		Notes note = noteRepository.findById(id).orElse(null);
		noteRepository.delete(note);

		return "redirect:/notes";
	}

	@RequestMapping("/notes/edit/{id}")
	public ModelAndView edit(@PathVariable Long id) {
		ModelAndView modelAndView = new ModelAndView();
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User user = userService.findUserByEmail(auth.getName());
		modelAndView.addObject("currentUser", user);
		modelAndView.addObject("fullName", "Welcome " + user.getFullname());
		modelAndView.addObject("adminMessage", "Content Available Only for Users with Admin Role");
		modelAndView.addObject("note", noteRepository.findById(id).orElse(null));
		modelAndView.setViewName("edit");
		return modelAndView;
	}

	@RequestMapping("/notes/update")
	public String update(@RequestParam Long id, @RequestParam String title, @RequestParam String content) {
		Notes note = noteRepository.findById(id).orElse(null);
		note.setTitle(title);
		note.setContent(content);
		note.setUpdated(new Date());
		noteRepository.save(note);

		return "redirect:/notes/show/" + note.getId();
	}

}
