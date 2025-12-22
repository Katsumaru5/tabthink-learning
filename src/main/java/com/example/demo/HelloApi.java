package com.example.demo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloApi {
	
	@GetMapping("/helloapi")
	public String hello() { 
		return "Hello World";
	}
}