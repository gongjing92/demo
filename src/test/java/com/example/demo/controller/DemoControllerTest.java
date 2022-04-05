package com.example.demo.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import org.junit.Assert;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.example.demo.dto.Result;
import com.example.demo.entity.User;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class DemoControllerTest {

	@Autowired
	MockMvc mockMvc;
	
	ObjectMapper objectMapper = new ObjectMapper();

	@BeforeAll
	public static void init(@Autowired MongoTemplate mongoTemplate) {
		mongoTemplate.dropCollection(User.class);
		mongoTemplate.save(new User("Tim",600), "users");
		mongoTemplate.save(new User("Mueller",6000), "users");
		mongoTemplate.save(new User("Gerald",3000), "users");
		mongoTemplate.save(new User("Zofia",2000), "users");
		mongoTemplate.save(new User("Eleasha",1000), "users");
		mongoTemplate.save(new User("Alan",1500), "users");
	}

	@AfterAll
	public static void cleanUp(@Autowired MongoTemplate mongoTemplate) {
		 mongoTemplate.dropCollection(User.class);
	}

	@Order(1)
	@Test
	public void getAllUsersSalaryLessThan4000() throws Exception {
		MvcResult mvcResult = mockMvc.perform(get("/api/v1/users")).andExpect(status().isOk()).andReturn();
		Result result = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), Result.class);
 		Assert.assertEquals(5, result.getUsers().size());
	}
	
	@Order(2)
	@Test
	public void getAllUsersSalaryLessThan7000() throws Exception {
		MvcResult mvcResult = mockMvc.perform(get("/api/v1/users?max=7000")).andExpect(status().isOk()).andReturn();
		Result result = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), Result.class);
 		Assert.assertEquals(6, result.getUsers().size());
	}
	
	@Order(3)
	@Test
	public void getAllUsersSalaryLessThan7000AndMoreThanEqual2000() throws Exception {
		MvcResult mvcResult = mockMvc.perform(get("/api/v1/users?max=7000&min=2000")).andExpect(status().isOk()).andReturn();
		Result result = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), Result.class);
 		Assert.assertEquals(3, result.getUsers().size());
	}
	
	@Order(4)
	@Test
	public void getAllUsersSalaryLessThan4000WithLimit3() throws Exception {
		MvcResult mvcResult = mockMvc.perform(get("/api/v1/users?limit=3")).andExpect(status().isOk()).andReturn();
		Result result = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), Result.class);
 		Assert.assertEquals(3, result.getUsers().size());
	}
	
	@Order(5)
	@Test
	public void getAllUsersSalaryLessThan4000WithOffset2() throws Exception {
		MvcResult mvcResult = mockMvc.perform(get("/api/v1/users?offset=2")).andExpect(status().isOk()).andReturn();
		Result result = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), Result.class);
 		Assert.assertEquals(3, result.getUsers().size());
	}
	
	@Order(6)
	@Test
	public void getAllUsersSalaryLessThan4000SortedByName() throws Exception {
		MvcResult mvcResult = mockMvc.perform(get("/api/v1/users?sort=name")).andExpect(status().isOk()).andReturn();
		Result result = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), Result.class);
 		Assert.assertEquals("Alan", result.getUsers().get(0).getName());
	}
	
	@Order(7)
	@Test
	public void getAllUsersSalaryLessThan4000SortedBySalary() throws Exception {
		MvcResult mvcResult = mockMvc.perform(get("/api/v1/users?sort=salary")).andExpect(status().isOk()).andReturn();
		Result result = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), Result.class);
 		Assert.assertEquals("Tim", result.getUsers().get(0).getName());
	}
	
	@Order(8)
	@Test
	public void getAllUsersWithInvalidParamsThrowBadRequest() throws Exception {
		mockMvc.perform(get("/api/v1/users?limit=a")).andExpect(status().isBadRequest()).andReturn();
	}
	
	@Order(9)
	@Test
	public void uploadValidCSVAndAssertEntry() throws Exception {
		File file = new File("valid.csv");
        MockMultipartFile multipartFile = new MockMultipartFile("file", file.getName(), "multipart/form-data",new FileInputStream(file));
		mockMvc.perform(MockMvcRequestBuilders.multipart("/api/v1/upload").file(multipartFile)).andExpect(status().isOk()).andReturn();
		MvcResult mvcResult = mockMvc.perform(get("/api/v1/users")).andExpect(status().isOk()).andReturn();
		Result result = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), Result.class);
 		Assert.assertEquals(8, result.getUsers().size());
	}
}
