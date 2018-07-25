package com.kyulix.RGTestApp;

import com.kyulix.RGTestApp.entities.Employee;
import com.kyulix.RGTestApp.entities.Office;
import com.kyulix.RGTestApp.repositories.EmployeeRepository;
import com.kyulix.RGTestApp.repositories.OfficeRepository;
import org.hibernate.validator.internal.metadata.raw.ConstrainedField;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.PayloadDocumentation;
import org.springframework.restdocs.request.RequestDocumentation;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
public class EmployeeControllerRestDocsTest {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

	@Autowired
	EmployeeRepository employeeRepository;

	@Autowired
	OfficeRepository officeRepository;

	@Rule
    public JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation();

    @Before
    public void setUp() {

        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context)
                .apply(documentationConfiguration(this.restDocumentation)).build();
    }

	@Test
	public void showEmployees() throws Exception {

		this.mockMvc
				.perform(get("/employees/showAll").accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andDo(MockMvcRestDocumentation.document("{class_name}/{method_name}",
						PayloadDocumentation.responseFields()
								.andWithPrefix("employees[]", RestDocsHelper.getEmployeeFieldDescriptors())
								.and(PayloadDocumentation.subsectionWithPath("_links").description("Links"))
				));
	}

	@Test
	public void showEmployee() throws Exception {

    	Employee employee = new Employee("John", "Doe", "SomePosition");
		employee.setEMail("johndoe@unknownmailservice.com");
		employee.setPhoneNumber("555-555");
		employeeRepository.save(employee);

		this.mockMvc
				.perform(RestDocumentationRequestBuilders.get("/employees/show?id={id}", employee.getId())
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andDo(MockMvcRestDocumentation.document("{class_name}/{method_name}",
						RequestDocumentation.requestParameters(
								RequestDocumentation.parameterWithName("id").description("Employee ID")
						),
						PayloadDocumentation.responseFields()
								.andWithPrefix("employees[]", RestDocsHelper.getEmployeeFieldDescriptors())
								.and(PayloadDocumentation.subsectionWithPath("_links").description("Links"))
				));
    }

    @Test
    public void addEmployee() throws Exception {

		Employee employee = new Employee("John", "Doe", "SomePosition");
		employee.setEMail("johndoe@unknownmailservice.com");
		employee.setPhoneNumber("555-555");
		employeeRepository.save(employee);

		this.mockMvc
				.perform(RestDocumentationRequestBuilders
						.get("/employees/add?firstName={firstName}&lastName={lastName}&position={position}&eMail={email}&phoneNumber={phoneNumber}",
								employee.getFirstName(), employee.getLastName(),
								employee.getPosition(), employee.getEMail(), employee.getPhoneNumber())
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andDo(MockMvcRestDocumentation.document("{class_name}/{method_name}",
						RequestDocumentation.requestParameters(
								RequestDocumentation.parameterWithName("firstName").description("First Name"),
								RequestDocumentation.parameterWithName("lastName").description("Last Name"),
								RequestDocumentation.parameterWithName("position").description("Position"),
								RequestDocumentation.parameterWithName("eMail").description("EMail"),
								RequestDocumentation.parameterWithName("phoneNumber").description("Phone Number")
						),
						PayloadDocumentation.responseFields()
								.and(RestDocsHelper.getResponseMessageFieldDescriptors())
								.and(PayloadDocumentation.subsectionWithPath("_links").description("Links"))
				));
	}

	@Test
	public void changeEmployee() throws Exception {

		Employee employee = new Employee("John", "Doe", "SomePosition");
		employee.setEMail("johndoe@unknownmailservice.com");
		employee.setPhoneNumber("555-555");
		employeeRepository.save(employee);

		Employee employeeToChange = employeeRepository.findById(employee.getId()).get();
		employeeToChange.setFirstName("Jane");
		employeeToChange.setLastName("Doe");
		employeeToChange.setPosition("Nothing");
		employeeToChange.setEMail("jane@doe.com");
		employeeToChange.setPhoneNumber("555-666");
		employeeRepository.save(employeeToChange);

		this.mockMvc
				.perform(RestDocumentationRequestBuilders
						.get("/employees/change?id={id}&firstName={firstName}&lastName={lastName}&position={position}&eMail={email}&phoneNumber={phoneNumber}",
								employeeToChange.getId(), employeeToChange.getFirstName(), employeeToChange.getLastName(),
								employeeToChange.getPosition(), employeeToChange.getEMail(), employeeToChange.getPhoneNumber())
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andDo(MockMvcRestDocumentation.document("{class_name}/{method_name}",
						RequestDocumentation.requestParameters(
								RequestDocumentation.parameterWithName("id").description("Employee Id"),
								RequestDocumentation.parameterWithName("firstName").description("First Name"),
								RequestDocumentation.parameterWithName("lastName").description("Last Name"),
								RequestDocumentation.parameterWithName("position").description("Position"),
								RequestDocumentation.parameterWithName("eMail").description("EMail"),
								RequestDocumentation.parameterWithName("phoneNumber").description("Phone Number")
						),
						PayloadDocumentation.responseFields()
								.and(RestDocsHelper.getResponseMessageFieldDescriptors())
								.and(PayloadDocumentation.subsectionWithPath("_links").description("Links"))
				));
	}

	@Test
	public void bindEmployeeToOffice() throws Exception {

    	Office office = new Office("Any Office", "Any Address");
    	officeRepository.save(office);

		Employee employee = new Employee("John", "Doe", "SomePosition");
		employee.setEMail("johndoe@unknownmailservice.com");
		employee.setPhoneNumber("555-555");
		employee.setWorkingOffice(office);
		employeeRepository.save(employee);

		this.mockMvc
				.perform(RestDocumentationRequestBuilders.get("/employees/bindToOffice?id={id}&officeId={officeId}",
						employee.getId(), office.getId()).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andDo(MockMvcRestDocumentation.document("{class_name}/{method_name}",
						RequestDocumentation.requestParameters(
								RequestDocumentation.parameterWithName("id").description("Employee Id"),
								RequestDocumentation.parameterWithName("officeId").description("Office Id")
						),
						PayloadDocumentation.responseFields()
								.and(RestDocsHelper.getResponseMessageFieldDescriptors())
								.and(PayloadDocumentation.subsectionWithPath("_links").description("Links"))
				));
	}

	@Test
	public void dismissEmployee() throws Exception {

		Employee employee = new Employee("John", "Doe", "SomePosition");
		employee.setEMail("johndoe@unknownmailservice.com");
		employee.setPhoneNumber("555-555");
		employeeRepository.save(employee);

		this.mockMvc
				.perform(RestDocumentationRequestBuilders.get("/employees/dismiss?id={id}", employee.getId())
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andDo(MockMvcRestDocumentation.document("{class_name}/{method_name}",
						RequestDocumentation.requestParameters(
								RequestDocumentation.parameterWithName("id").description("Employee Id")
								),
						PayloadDocumentation.responseFields()
								.and(RestDocsHelper.getResponseMessageFieldDescriptors())
								.and(PayloadDocumentation.subsectionWithPath("_links").description("Links"))
				));
	}

	@Test
	public void returnEmployee() throws Exception {

		Employee employee = new Employee("John", "Doe", "SomePosition");
		employee.setEMail("johndoe@unknownmailservice.com");
		employee.setPhoneNumber("555-555");
		employeeRepository.save(employee);

		this.mockMvc
				.perform(RestDocumentationRequestBuilders.get("/employees/return?id={id}", employee.getId()).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andDo(MockMvcRestDocumentation.document("{class_name}/{method_name}",
						RequestDocumentation.requestParameters(
								RequestDocumentation.parameterWithName("id").description("Employee Id")
								),
						PayloadDocumentation.responseFields()
								.and(RestDocsHelper.getResponseMessageFieldDescriptors())
								.and(PayloadDocumentation.subsectionWithPath("_links").description("Links"))
				));
	}
}
