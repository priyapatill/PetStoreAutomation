package api.test;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.github.javafaker.Faker;

import api.endpoints.UserEndPoints2;
import api.payload.User;
import io.restassured.response.Response;

public class UserTest2 {
	
	Faker fake;
	User userPayload;
	
	public Logger logger;
	
	@BeforeClass
	public void setUp() {
		
		fake= new Faker();
		userPayload=new User();
		
		userPayload.setId(fake.idNumber().hashCode());
		userPayload.setUsername(fake.name().username());
		userPayload.setFirstname(fake.name().firstName());
		userPayload.setLastname(fake.name().lastName());
		userPayload.setEmail(fake.internet().safeEmailAddress());
		userPayload.setPassword(fake.internet().password());
		userPayload.setPhone(fake.phoneNumber().cellPhone());
		
		//logs
		logger=LogManager.getLogger(this.getClass());
		logger.debug("debuggung.....");
	}
	
	@Test(priority = 1)
	public void testPostUser(){
		
		logger.info("*******Creating user *********");
		
		Response response=UserEndPoints2.cerateUser(userPayload);
		response.then().log().all();
		
		Assert.assertEquals(response.getStatusCode(), 200);
		logger.info("******* User is created *********");
	}
	
	@Test(priority = 2)
	public void testGetUserByName() {
		logger.info("******* Reading user Info ********");
		Response res=UserEndPoints2.readUser(this.userPayload.getUsername());
		res.then().log().all();
		
		Assert.assertEquals(res.getStatusCode(),200);
		logger.info("*******User Info is displayed *********");
	}
	
	@Test(priority = 3)
	public void testUpdateUserByName(){
		
		logger.info("*******Updating user *********");
		//Update data using payload
		userPayload.setFirstname(fake.name().firstName());
		userPayload.setLastname(fake.name().lastName());
		userPayload.setEmail(fake.internet().safeEmailAddress());
		
		
		Response response=UserEndPoints2.updateUser(this.userPayload.getUsername(),userPayload);
		response.then().log().body();
		
		//response.then().log().body().statusCode(200);
		Assert.assertEquals(response.getStatusCode(), 200);
		
		//Checking data update
		Response resafterUpdate=UserEndPoints2.readUser(this.userPayload.getUsername());
		
		Assert.assertEquals(resafterUpdate.getStatusCode(),200);
		logger.info("******* User is Updated *********");
	}
	
	@Test(priority = 4)
	public void deleteUserByName() {
		
		logger.info("******* Deleting user *********");
		Response res=UserEndPoints2.deleteUser(this.userPayload.getUsername());
		Assert.assertEquals(res.getStatusCode(), 200);
		logger.info("******* User is Deleted *********");
	}
}
