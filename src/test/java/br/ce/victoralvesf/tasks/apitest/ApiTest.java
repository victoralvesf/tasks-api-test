package br.ce.victoralvesf.tasks.apitest;

import org.hamcrest.CoreMatchers;
import org.json.JSONObject;
import org.junit.BeforeClass;
import org.junit.Test;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;

public class ApiTest {
	
	@BeforeClass
	public static void setup() {
		String baseUrl = System.getProperty("api.base.url") != null 
				? System.getProperty("api.base.url")
				: "http://localhost:8008/tasks-backend";
		
		RestAssured.baseURI = baseUrl;
	}

	@Test
	public void shouldReturnAllTasks() {		
		RestAssured
			.given()
			.when()
				.get("/todo")
			.then()
				.statusCode(200)
			;
	}
	
	@Test
	public void shouldCreateATask() {
		JSONObject body = new JSONObject();
		
		body.put("task", "API Test");
		body.put("dueDate", "2029-04-25");
		
		String serializedBody = body.toString();
		
		RestAssured
			.given()
				.body(serializedBody)
				.contentType(ContentType.JSON)
			.when()
				.post("/todo")
			.then()
				.statusCode(201)
			;
	}
	
	@Test
	public void shouldNotCreateAnInvalidTask() {
		JSONObject body = new JSONObject();
		
		body.put("task", "API Test Error");
		body.put("dueDate", "2003-04-25");
		
		String serializedBody = body.toString();
		
		RestAssured
			.given()
				.body(serializedBody)
				.contentType(ContentType.JSON)
			.when()
				.post("/todo")
			.then()
				.statusCode(400)
				.body("message", CoreMatchers.is("Due date must not be in past"))
			;
	}
}
