package com.demo.config;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.demo.model.User;

@SpringBootApplication
public class SpringRestTemplateApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringRestTemplateApplication.class, args);
	}
	
	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder) {
		return builder.build();
	}

	@Bean
	public CommandLineRunner run(RestTemplate restTemplate) throws Exception {
		return args -> {
			//getMethods(restTemplate);
			//putMethod(restTemplate);
			//postMethods(restTemplate);
			//deleteMethod(restTemplate);
			miscellaneousMethods(restTemplate);
		};
	}
	
	void getMethods(RestTemplate restTemplate){
		int id=1;
		User user = restTemplate.getForObject("http://localhost:8080/SpringBootRest/{id}", User.class,id);
		System.out.println("User found using getForObject with id value of " + id + " is " + user.toString());

		var result = user.toString().isBlank();
		
		Map<String, String> urlVariables = new HashMap<String, String>();
		urlVariables.put("id", "1");
		ResponseEntity<User> response= restTemplate.getForEntity("http://localhost:8080/SpringBootRest/{id}", User.class,urlVariables);
		if(response.getStatusCode() == HttpStatus.OK){
			System.out.println("ResponseEntity returned when using getForEntity with id value of " + id + " is " + response.getBody());
		}
	}
	
	void putMethod(RestTemplate restTemplate){
		User user=new User();
		user.setUserName("Batman");
		user.setUserId(1);
		user.setPhone("1234567890");
		String url = "http://localhost:8080/SpringBootRest/update/";
		restTemplate.put(URI.create(url), user);
		int id=1;
		User userFound = restTemplate.getForObject("http://localhost:8080/SpringBootRest/{id}", User.class,id);
		System.out.println("User found using getForObject with id value of " + id + " is " + userFound.toString());

	}

	//BEFORE running this method set user id value to unique value
	void postMethods(RestTemplate restTemplate){
		User user=new User();
		user.setUserId(4);
		user.setUserName("newUser");
		user.setPhone("0987654321");
		
		restTemplate.postForObject("http://localhost:8080/SpringBootRest/create/", user, User.class);

		int id=4;
		User userFound = restTemplate.getForObject("http://localhost:8080/SpringBootRest/{id}", User.class,id);
		System.out.println("User found using getForObject with id value of " + id + " is " + userFound.toString());

//		URI location=restTemplate.postForLocation("http://localhost:8080/SpringBootRest/create/", user);
//		if(location!=null){
//			System.out.println(location.toString());
//		}
	}
	
	void deleteMethod(RestTemplate restTemplate){
		int id=2;
		restTemplate.delete("http://localhost:8080/SpringBootRest/delete/{id}", id);
	}
	
	void miscellaneousMethods(RestTemplate restTemplate){
		MultiValueMap<String, String> headerMap =	new LinkedMultiValueMap<String, String>();
		headerMap.add("Accept", "application/json");
		headerMap.add("connection", "keep-alive");
		HttpEntity<Object> requestEntity = new HttpEntity<Object>(headerMap);
		int id=1;
		ResponseEntity<User> response= restTemplate.exchange("http://localhost:8080/SpringBootRest/{id}",HttpMethod.GET,requestEntity, User.class,id);
		System.out.println(response.getBody());
		
		HttpHeaders httpHeaders = restTemplate.headForHeaders("http://localhost:8080/SpringBootRest/");
		System.out.println(httpHeaders.getContentType());
	}
}
