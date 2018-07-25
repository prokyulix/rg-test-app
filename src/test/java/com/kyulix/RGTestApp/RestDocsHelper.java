package com.kyulix.RGTestApp;

import com.kyulix.RGTestApp.entities.Office;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.PayloadDocumentation;

public final class RestDocsHelper {

    public static FieldDescriptor[] getEmployeeFieldDescriptors() {

        return
                new FieldDescriptor[] {
                    PayloadDocumentation.fieldWithPath("id").description("Employee ID"),
                    PayloadDocumentation.fieldWithPath("firstName").description("First Name"),
                    PayloadDocumentation.fieldWithPath("lastName").description("Last Name"),
                    PayloadDocumentation.fieldWithPath("phoneNumber").optional().description("Phone Number"),
                    PayloadDocumentation.fieldWithPath("position").description("Current Position"),
                    PayloadDocumentation.fieldWithPath("active").description("Active State (working / dismissed)"),
                    PayloadDocumentation.subsectionWithPath("workingOffice").optional().type("Office")
                            .description("Working Office Object"),
                    PayloadDocumentation.fieldWithPath("email").optional().description("EMail")
                };
    }

    public static FieldDescriptor[] getOfficeFieldDescriptors() {

        return
                new FieldDescriptor[] {
                        PayloadDocumentation.fieldWithPath("id").description("Office ID"),
                        PayloadDocumentation.fieldWithPath("name").description("Name"),
                        PayloadDocumentation.fieldWithPath("address").description("Address"),
                        PayloadDocumentation.fieldWithPath("active").description("Active State (opened / closed)"),
                };
    }

    public static FieldDescriptor[] getResponseMessageFieldDescriptors() {

        return
                new FieldDescriptor[] {
                        PayloadDocumentation.fieldWithPath("responseCode").description("Response Code"),
                        PayloadDocumentation.fieldWithPath("responseMessage").optional().type("String")
                                .description("Response Debug Message (shows if error occured)")
                };
    }
}
