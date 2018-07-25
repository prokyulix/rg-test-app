package com.kyulix.RGTestApp;

import com.kyulix.RGTestApp.controllers.EmployeeController;
import com.kyulix.RGTestApp.entities.Employee;
import com.kyulix.RGTestApp.repositories.EmployeeRepository;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
//@AutoConfigureRestDocs
//@AutoConfigureMockMvc
public class RgTestAppApplicationTests {

    @Autowired
    private WebApplicationContext context;

//    @Autowired
    private MockMvc mockMvc;

	@Autowired
	EmployeeRepository employeeRepository;

	@Rule
    public JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation("custom");

    @Before
    public void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context)
                .apply(documentationConfiguration(this.restDocumentation))
                .build();
    }

	@Test
	public void showEmployees() throws Exception {

		this.mockMvc.perform(get("/employees/showAll").accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andDo(MockMvcRestDocumentation.document("{class_name}/{method_name}"));
	}

	@Test
	public void showEmployee() throws Exception {

		Employee employee = new Employee("John", "Doe", "SomePosition");
		employee.setEMail("johndoe@unknownmailservice.com");
		employee.setPhoneNumber("555-555");

		employeeRepository.save(employee);

		this.mockMvc.perform(get("/employees/show?id={id}", employee.getId()))
				.andExpect(status().isOk())
				.andDo(MockMvcRestDocumentation.document("{class_name}/{method_name}"));
		//this.mockMvc.perform(get('/employees/show/{1}'))
	}
}
