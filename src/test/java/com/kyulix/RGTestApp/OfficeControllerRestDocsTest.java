package com.kyulix.RGTestApp;

import com.kyulix.RGTestApp.entities.Employee;
import com.kyulix.RGTestApp.entities.Office;
import com.kyulix.RGTestApp.repositories.EmployeeRepository;
import com.kyulix.RGTestApp.repositories.OfficeRepository;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
public class OfficeControllerRestDocsTest {

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
    public void showOffices() throws Exception {

        Office office = new Office("SomeOffice", "SomeAddress");
        officeRepository.save(office);

        this.mockMvc
                .perform(RestDocumentationRequestBuilders.get("/offices/showAll")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(MockMvcRestDocumentation.document("{class_name}/{method_name}",
                        PayloadDocumentation.responseFields()
                                .andWithPrefix("offices[]", RestDocsHelper.getOfficeFieldDescriptors())
                                .and(PayloadDocumentation.subsectionWithPath("_links").description("Links"))
                ));

        officeRepository.delete(office);
    }

    @Test
    public void showOffice() throws Exception {

        Office office = new Office("SomeOffice", "SomeAddress");
        officeRepository.save(office);

        this.mockMvc
                .perform(RestDocumentationRequestBuilders.get("/offices/show?id={id}", office.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(MockMvcRestDocumentation.document("{class_name}/{method_name}",
                        RequestDocumentation.requestParameters(
                                RequestDocumentation.parameterWithName("id").description("Office Id")
                        ),
                        PayloadDocumentation.responseFields()
                                .andWithPrefix("offices[]", RestDocsHelper.getOfficeFieldDescriptors())
                                .and(PayloadDocumentation.subsectionWithPath("_links").description("Links"))
                ));

        officeRepository.delete(office);
    }

    @Test
    public void addOffice() throws Exception {

        Office office = new Office("SomeOffice1", "SomeAddress1");
        officeRepository.save(office);

        this.mockMvc
                .perform(RestDocumentationRequestBuilders.get("/offices/add?name={name}&address={address}",
                        office.getName(), office.getAddress())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(MockMvcRestDocumentation.document("{class_name}/{method_name}",
                        RequestDocumentation.requestParameters(
                                RequestDocumentation.parameterWithName("name").description("Office Name"),
                                RequestDocumentation.parameterWithName("address").description("Office Address")
                        ),
                        PayloadDocumentation.responseFields()
                                .and(RestDocsHelper.getResponseMessageFieldDescriptors())
                                .and(PayloadDocumentation.subsectionWithPath("_links").description("Links"))
                ));

        officeRepository.delete(office);
    }

    @Test
    public void changeOffice() throws Exception {

        Office office = new Office("SomeOffice3", "SomeAddress3");
        officeRepository.save(office);

        office.setName("SomeOffice3Changed");
        office.setAddress("SomeAddress3Changed");
        officeRepository.save(office);

        this.mockMvc
                .perform(RestDocumentationRequestBuilders.get("/offices/change?id={id}&name={name}&address={address}",
                        office.getId(), office.getName(), office.getAddress())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(MockMvcRestDocumentation.document("{class_name}/{method_name}",
                        RequestDocumentation.requestParameters(
                                RequestDocumentation.parameterWithName("id").description("Office Id"),
                                RequestDocumentation.parameterWithName("name").description("Office Name"),
                                RequestDocumentation.parameterWithName("address").description("Office Address")
                        ),
                        PayloadDocumentation.responseFields()
                                .and(RestDocsHelper.getResponseMessageFieldDescriptors())
                                .and(PayloadDocumentation.subsectionWithPath("_links").description("Links"))
                ));

        officeRepository.delete(office);
    }

    @Test
    public void acceptEmployees() throws Exception {

        Office office = new Office("SomeOffice4", "SomeAddress4");
        officeRepository.save(office);

        Employee firstEmployee = new Employee("First", "Emplyee", "First Position");
        firstEmployee.setWorkingOffice(office);
        employeeRepository.save(firstEmployee);

        Employee secondEmployee = new Employee("Second", "Employee", "Second Position");
        secondEmployee.setWorkingOffice(office);
        employeeRepository.save(secondEmployee);

        this.mockMvc
                .perform(RestDocumentationRequestBuilders.get("/offices/acceptEmployees?id={id}&employeesId={employeesId}",
                        office.getId(), String.format("%d,%d", firstEmployee.getId(), secondEmployee.getId()))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(MockMvcRestDocumentation.document("{class_name}/{method_name}",
                        RequestDocumentation.requestParameters(
                                RequestDocumentation.parameterWithName("id").description("Office Id"),
                                RequestDocumentation.parameterWithName("employeesId").description("Comma Separated Employee Ids (example: 1,2,3)")
                        ),
                        PayloadDocumentation.responseFields()
                                .and(RestDocsHelper.getResponseMessageFieldDescriptors())
                                .and(PayloadDocumentation.subsectionWithPath("_links").description("Links"))
                ));

        employeeRepository.delete(firstEmployee);
        employeeRepository.delete(secondEmployee);
        officeRepository.delete(office);
    }

    @Test
    public void closeOffice() throws Exception {

        Office office = new Office("SomeOffice5", "SomeAddress5");
        office.setActive(false);
        officeRepository.save(office);

        this.mockMvc
                .perform(RestDocumentationRequestBuilders.get("/offices/close?id={id}",
                        office.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(MockMvcRestDocumentation.document("{class_name}/{method_name}",
                        RequestDocumentation.requestParameters(
                                RequestDocumentation.parameterWithName("id").description("Office Id")
                        ),
                        PayloadDocumentation.responseFields()
                                .and(RestDocsHelper.getResponseMessageFieldDescriptors())
                                .and(PayloadDocumentation.subsectionWithPath("_links").description("Links"))
                ));

        officeRepository.delete(office);
    }
}
