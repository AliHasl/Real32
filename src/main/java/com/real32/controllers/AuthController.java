package com.real32.controllers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.ResourceUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.real32.models.User;
import com.real32.services.CustomUserDetailsService;

@Controller
public class AuthController {

	@Autowired
	private CustomUserDetailsService userService;

	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public ModelAndView login() {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("login");
		return modelAndView;
	}

	@RequestMapping(value = "/signup", method = RequestMethod.GET)
	public ModelAndView signup() {
		ModelAndView modelAndView = new ModelAndView();
		User user = new User();
		modelAndView.addObject("user", user);
		modelAndView.setViewName("signup");
		return modelAndView;
	}

	@RequestMapping(value = "/signup", method = RequestMethod.POST)
	public ModelAndView createNewUser(@Validated User user, BindingResult bindingResult) {
		ModelAndView modelAndView = new ModelAndView();
		User userExists = userService.findUserByEmail(user.getEmail());
		if (userExists != null) {
			bindingResult.rejectValue("email", "error.user",
					"There is already a user registered with the username provided");
		}
		if (bindingResult.hasErrors()) {
			modelAndView.setViewName("signup");
		} else {
			userService.saveUser(user);
			modelAndView.addObject("successMessage", "User has been registered successfully");
			modelAndView.addObject("user", new User());
			modelAndView.setViewName("login");

		}
		return modelAndView;
	}

	@GetMapping(value = "/models/{model}", produces = "application/json")
	public ResponseEntity<String> GetModelData(@PathVariable String model) throws IOException {

		File file = ResourceUtils.getFile("classpath:static/models/" + model + ".obj");
		BufferedReader bufferedReader = new BufferedReader(new FileReader(file));

		String line;
		ArrayList<float[]> positions = new ArrayList<>();
		ArrayList<float[]> normals = new ArrayList<>();
		ArrayList<float[]> vertices = new ArrayList<>();
		while ((line = bufferedReader.readLine()) != null) {
			String parts[] = line.trim().split(" ");
			switch (parts[0]) {
			case "v":
				float position[] = { Float.parseFloat(parts[1]), Float.parseFloat(parts[2]),
						Float.parseFloat(parts[3]) };
				positions.add(position);
				break;
			case "vn":
				float normal[] = { Float.parseFloat(parts[1]), Float.parseFloat(parts[2]), Float.parseFloat(parts[3]) };
				normals.add(normal);
				break;
			case "f":
				String f1[] = {};

				for (int i = 1; i < 4; i++) {
					f1 = parts[i].split("/");
					int posIndex = Integer.parseInt(f1[0]);
					int normalIndex = Integer.parseInt(f1[2]);
					vertices.add(positions.get(posIndex - 1));
					vertices.add(normals.get(normalIndex - 1));
				}
				break;
			default:
				break;
			}
		}
		bufferedReader.close();
		int vertexCount = vertices.size() / 2;

		HashMap<String, Object> jsonMap = new HashMap<>();
		jsonMap.put("RenderFormat", "TRIANGLES");
		jsonMap.put("VertexCount", vertexCount);
		jsonMap.put("Vertices", vertices);

		return ResponseEntity.ok(new ObjectMapper().writeValueAsString(jsonMap));
	}

	@GetMapping(value = "/shaders/{shaderName}")
	public ResponseEntity<String> LoadShader(@PathVariable String shaderName) throws FileNotFoundException {

		if (shaderName.equals("vertexShader")) {
			File file = ResourceUtils.getFile("classpath:static/shaders/camera.vs");

			return ResponseEntity.ok(ParseFile(file));
		} else if (shaderName.equals("fragmentShader")) {
			File file = ResourceUtils.getFile("classpath:static/shaders/diffuse.fs");
			return ResponseEntity.ok(ParseFile(file));
		} else {
			return ResponseEntity.badRequest().body("Incorrect Shader selected");
		}

	}

	private String ParseFile(File inFile) {
		try {
			BufferedReader bufferedReader = new BufferedReader(new FileReader(inFile));
			String line = null;
			StringBuilder stringBuilder = new StringBuilder();
			String ls = System.getProperty("line.separator");

			while ((line = bufferedReader.readLine()) != null) {
				stringBuilder.append(line);
				stringBuilder.append(ls);
			}
			bufferedReader.close();
			return stringBuilder.toString();
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		return "";
	}

}
