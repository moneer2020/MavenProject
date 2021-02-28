package com.hrms.api;

import com.hrms.utils.apiConstants;
import com.hrms.utils.apiPayloadConstants;
import io.cucumber.java.en.*;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import net.minidev.json.JSONObject;

import static io.restassured.RestAssured.*;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

public class apiTestingFinalSteps {
     RequestSpecification request;
     Response response;
     static  String employeeID;
     static String updated_employee_middle_name;
     static String partialupdated_first_name;

//    RequestSpecification requestToRetrieveCreatedEmployee;
//    Response responseToRetrieveCreatedEmployee;

    @Given("a request is prepared to create an employee")
    public void a_request_is_prepared_to_create_an_employee() {
        //preparing request to create Employee
        request=given().header(apiConstants.Header_Content_type,apiConstants.Content_type)
                .header(apiConstants.Header_Authorization,generateTokenSteps.token)
                .body(apiPayloadConstants.createEmployeeBody());

    }
    @When("a POST call is made to create an Employee")
    public void a_POST_call_is_made_to_create_an_Employee() {
      //sending the request to create Employee
     response=request.when().post(apiConstants.CREATE_EMPLOYEE_URI);

    }
    @Then("the status code for creating an employee is {int}")
    public void the_status_code_for_creating_an_employee_is(int status_code ) {
        //assert that the status code is 201
       response.then().assertThat().statusCode(status_code);
    }

   @Then("the employee is created contains key {string} and value {string}")
    public void the_employee_is_created_contains_key_and_value(String key, String value) {
    //assert that the message contains entry created
    response.then().assertThat().body(key,equalTo(value));
    }


    @Then("the employeeID {string} is stored in the global variable to be used for other calls")
    public void the_employeeID_is_stored_in_the_global_variable_to_be_used_for_other_calls(String value) {
        // extract the emploeeID from the json response
         employeeID=response.jsonPath().getString(value);
    }


    //------------------------------------Scenario: Retrieving the created Employee-------------------------------
    @Given("a request is prepared to retrieve the created employee")
    public void a_request_is_prepared_to_retrieve_the_created_employee() {
         //preparing the request to retrieve the created employee

       request = given()
                .header(apiConstants.Header_Content_type, apiConstants.Content_type)
                .header(apiConstants.Header_Authorization, generateTokenSteps.token)
                .queryParam("employee_id",employeeID).log().all();
    }

    @When("a GET call is made to retrieve the created Employee")
    public void a_GET_call_is_made_to_retrieve_the_created_Employee() {
        //sending the request to retrieve the create employee
        response=request.when().get(apiConstants.GET_ONE_EMPLOYEE_URI);

    }
    @Then("the status code for retrieving the created employee is {int}")
    public void the_status_code_for_retrieving_the_created_employee_is(int status_code) {
      //assert that status code is 200
        response.then().assertThat().statusCode(status_code);
    }
    @Then("the retrieved EmployeeID {string} matches the globally stored EmployeeID")
    public void the_retrieved_EmployeeID_matches_the_globally_stored_EmployeeID(String value) {
        //assert that employee id is same as the one stored globally
        response.then().assertThat().body(value,equalTo(employeeID));

    }
    @Then("the retrieved data matches the data used to create the employee")
    public void the_retrieved_data_matches_the_data_used_to_create_the_employee() {

//        response.then().assertThat().body("employee[0].emp_firstname",equalTo("moazzam"));

        JsonPath jpath= response.jsonPath();
        String emp_first_name = jpath.getString("employee[0].emp_firstname");
        String emp_last_name = jpath.getString("employee[0].emp_lastname");
        String emp_middle_name = jpath.getString("employee[0].emp_middle_name");
        String emp_birthday = jpath.getString("employee[0].emp_birthday");
        String emp_gender = jpath.getString("employee[0].emp_gender");
        String emp_job_title = jpath.getString("employee[0].emp_job_title");
        String emp_status = jpath.getString("employee[0].emp_status");

       assertThat(emp_first_name,equalTo("moazzam"));
       assertThat(emp_last_name,equalTo("sadiq"));
       assertThat(emp_middle_name,equalTo("s"));
       assertThat(emp_birthday,equalTo("2021-02-27"));
       assertThat(emp_gender,equalTo("Male"));
       assertThat(emp_job_title,equalTo("Cloud Architect"));
       assertThat(emp_status,equalTo("Employee"));

    }

    //.........................................Updating the created employe---------------------------------
    @Given("a request is prepared to update the created employee")
    public void a_request_is_prepared_to_update_the_created_employee() {
        updated_employee_middle_name="updated middle name";
        JSONObject payload =new JSONObject();
        payload.put("employee_id",employeeID);
        payload.put("emp_firstname","moazzam");
        payload.put("emp_lastname","sadiq");
        payload.put("emp_middle_name",updated_employee_middle_name);
        payload.put("emp_gender","M");
        payload.put("emp_birthday","2021-02-27");
        payload.put("emp_status","Employee");
        payload.put("emp_job_title","Cloud Architect");
        String final_payload=payload.toString();

        request= given().header(apiConstants.Header_Content_type,apiConstants.Content_type)
                 .header(apiConstants.Header_Authorization,generateTokenSteps.token)
                 .body(final_payload);
    }

    @When("a PUT call is made to update the employee")
    public void a_PUT_call_is_made_to_update_the_employee() {
   //sending the put request to update the employee
      response=request.when().put(apiConstants.UPDATE_EMPLOYEE_URI);
    }
    @Then("the status code is {int}")
    public void the_status_code_is( int status_code) {
        //asserting that the status code is 200
      response.then().assertThat().statusCode(status_code);
    }
    @Then("the updated employee contains key {string} and value {string}")
    public void the_updated_employee_contains_key_and_value(String key, String value) {
      //assert that response contains message entry updated
        response.then().assertThat().body(key,equalTo(value));
    }


    //--------------------Retrieving the updated employee information--------------------------------------------------

    @Given("a request is prepared to retrieve the updated employee")
    public void a_request_is_prepared_to_retrieve_the_updated_employee() {

        request=given().header(apiConstants.Header_Content_type,apiConstants.Content_type)
                .header(apiConstants.Header_Authorization,generateTokenSteps.token)
                .queryParam("employee_id",employeeID);

    }


    @When("a GET call is made to retrieve the updated Employee")
    public void a_GET_call_is_made_to_retrieve_the_updated_Employee() {
        //make a get call to retrieve the updated employee
        response=request.when().get(apiConstants.GET_ONE_EMPLOYEE_URI);

    }
    @Then("the status code for retrieving the updated employee is {int}")
    public void the_status_code_for_retrieving_the_updated_employee_is(int status_code) {
        //assert the status code
        response.then().assertThat().statusCode(status_code);

    }
    @Then("the retrieved Employee_Middle_Name {string} matches the globally sotred employee middle name")
    public void the_retrieved_Employee_Middle_Name_matches_the_globally_sotred_employee_middle_name(String updtedMiddleName) {

//    response.then().assertThat().body(updtedMiddleName,equalTo(updated_employee_middle_name));
        String actualMiddleName = response.jsonPath().getString(updtedMiddleName);
        assertThat(actualMiddleName, equalTo(updated_employee_middle_name));

    }

    //------------------partially updating the employee-----------------------------------

    @Given("a request is preapared to partially update the employee")
    public void a_request_is_preapared_to_partially_update_the_employee() {
        partialupdated_first_name="UpdatedName_sucess";
        JSONObject payload =new JSONObject();
        payload.put("employee_id",employeeID);
        payload.put("emp_firstname",partialupdated_first_name);
        String final_payload = payload.toString();

        request=given().header(apiConstants.Header_Content_type,apiConstants.Content_type)
                .header(apiConstants.Header_Authorization,generateTokenSteps.token)
                .body(final_payload);

    }


    @When("a PATCH call is made to partially update the employee")
    public void a_PATCH_call_is_made_to_partially_update_the_employee() {
       response=request.when().patch(apiConstants.PARTIALLY_UPDATE_EMPLOYEE_URI);

    }


    @Then("the partailly updated employee contains key {string} and value {string}")
    public void the_partailly_updated_employee_contains_key_and_value(String key, String value) {

        response.then().assertThat().body(key,equalTo(value));

    }

    @Then("the partially update employee {string} matches the globally stored employee first name")
    public void the_partially_update_employee_matches_the_globally_stored_employee_middle_name(String value) {

        response.then().assertThat().body(value,equalTo(partialupdated_first_name));
    }








}
