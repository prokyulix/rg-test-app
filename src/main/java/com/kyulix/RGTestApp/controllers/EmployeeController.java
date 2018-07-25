package com.kyulix.RGTestApp.controllers;

import com.kyulix.RGTestApp.constants.CommonResponseCodes;
import com.kyulix.RGTestApp.entities.Employee;
import com.kyulix.RGTestApp.entities.Office;
import com.kyulix.RGTestApp.repositories.EmployeeRepository;
import com.kyulix.RGTestApp.repositories.OfficeRepository;
import com.kyulix.RGTestApp.resources.EmployeeResource;
import com.kyulix.RGTestApp.resources.ResponseMessageResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RestController
@ExposesResourceFor(Employee.class)
@RequestMapping("/employees")
public class EmployeeController {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private OfficeRepository officeRepository;

    @RequestMapping("/showAll")
    public HttpEntity<EmployeeResource> showAll() {

        EmployeeResource employeeResource = new EmployeeResource(employeeRepository.findAll());
        employeeResource.add(linkTo(methodOn(EmployeeController.class).showAll()).withSelfRel());

        return new ResponseEntity(employeeResource, HttpStatus.OK);
    }

    @RequestMapping("/show")
    public HttpEntity<EmployeeResource> show(@RequestParam(value = "id", required = true) int id) {

        EmployeeResource employeeResource = new EmployeeResource(employeeRepository.findById(id).get());
        employeeResource.add(linkTo(methodOn(EmployeeController.class).show(id)).withSelfRel());

        return new ResponseEntity(employeeResource, HttpStatus.OK);
    }

    @RequestMapping("/add")
    public HttpEntity<ResponseMessageResource> addEmployee(
            @RequestParam(value = "firstName", required = true) String firstName,
            @RequestParam(value = "lastName", required = true) String lastName,
            @RequestParam(value = "email", required = false) String eMail,
            @RequestParam(value = "phoneNumber", required = false) String phoneNumber,
            @RequestParam(value = "position", required = true) String position) {

        ResponseMessageResource responseMessage;

        try {
            Employee employeeToAppend = new Employee(firstName, lastName, position);

            if (eMail != null)
                employeeToAppend.setEMail(eMail);

            if (phoneNumber != null)
                employeeToAppend.setPhoneNumber(phoneNumber);

            employeeRepository.save(employeeToAppend);

            responseMessage = new ResponseMessageResource(CommonResponseCodes.SUCCESSFUL);
            responseMessage.add(linkTo(methodOn(EmployeeController.class)
                    .show(employeeToAppend.getId())).withRel("result"));
        } catch (Exception e) {
            responseMessage = new ResponseMessageResource(CommonResponseCodes.FAILED, e.getMessage());
        }

        responseMessage.add(linkTo(methodOn(EmployeeController.class)
                .addEmployee(firstName, lastName, eMail, phoneNumber, position)).withSelfRel());

        return new ResponseEntity(responseMessage, HttpStatus.OK);
    }

    @RequestMapping("/change")
    public HttpEntity<ResponseMessageResource> changeEmployee(
            @RequestParam(value = "id", required = true) int id,
            @RequestParam(value = "firstName", required = false) String firstName,
            @RequestParam(value = "lastName", required = false) String lastName,
            @RequestParam(value = "email", required = false) String eMail,
            @RequestParam(value = "phoneNumber", required = false) String phoneNumber,
            @RequestParam(value = "position", required = false) String position) {

        ResponseMessageResource responseMessage;

        if (employeeRepository.existsById(id)) {
            try {
                Employee employeeToChange = employeeRepository.findById(id).get();

                if (firstName != null)
                    employeeToChange.setFirstName(firstName);

                if (lastName != null)
                    employeeToChange.setLastName(lastName);

                if (eMail != null)
                    employeeToChange.setEMail(eMail);

                if (phoneNumber != null)
                    employeeToChange.setPhoneNumber(phoneNumber);

                if (position != null)
                    employeeToChange.setPosition(position);

                employeeRepository.save(employeeToChange);

                responseMessage = new ResponseMessageResource(CommonResponseCodes.SUCCESSFUL);
                responseMessage.add(linkTo(methodOn(EmployeeController.class)
                        .show(id)).withRel("result"));
            } catch (Exception e) {
                responseMessage = new ResponseMessageResource(CommonResponseCodes.FAILED, e.getMessage());
            }
        } else
            responseMessage = new ResponseMessageResource(CommonResponseCodes.NOT_EXISTS);

        responseMessage.add(linkTo(methodOn(EmployeeController.class)
                .changeEmployee(id, firstName, lastName, eMail, phoneNumber, position)).withSelfRel());

        return new ResponseEntity(responseMessage, HttpStatus.OK);
    }

    @RequestMapping("/bindToOffice")
    public HttpEntity<ResponseMessageResource> bindEmployeeToOffice(
            @RequestParam(value = "id", required = true) int id,
            @RequestParam(value = "officeId", required = true) int officeId) {

        ResponseMessageResource responseMessage;

        if ((employeeRepository.existsById(id)) && (officeRepository.existsById(officeId))) {
            try {
                Employee employeeToChange = employeeRepository.findById(id).get();
                Office officeToBind = officeRepository.findById(officeId).get();

                employeeToChange.setWorkingOffice(officeToBind);
                employeeRepository.save(employeeToChange);

                responseMessage = new ResponseMessageResource(CommonResponseCodes.SUCCESSFUL);
                responseMessage.add(linkTo(methodOn(EmployeeController.class).show(id)).withRel("result"));
            } catch (Exception e) {
                responseMessage = new ResponseMessageResource(CommonResponseCodes.FAILED, e.getMessage());
            }
        } else
            responseMessage = new ResponseMessageResource(CommonResponseCodes.NOT_EXISTS);

        responseMessage.add(linkTo(methodOn(EmployeeController.class)
                .bindEmployeeToOffice(id, officeId)).withSelfRel());

        return new ResponseEntity(responseMessage, HttpStatus.OK);
    }

    @RequestMapping("/dismiss")
    public HttpEntity<ResponseMessageResource> dismissEmployee(@RequestParam(value = "id", required = true) int id) {

        return new ResponseEntity(this.changeEmployeeActiveState(id, false), HttpStatus.OK);
    }

    @RequestMapping("/return")
    public HttpEntity<ResponseMessageResource> returnEmployee(@RequestParam(value = "id", required = true) int id) {

        return new ResponseEntity(this.changeEmployeeActiveState(id, true), HttpStatus.OK);
    }

    protected ResponseMessageResource changeEmployeeActiveState(int employeeId, boolean activeState) {

        ResponseMessageResource responseMessage;

        if (employeeRepository.existsById(employeeId)) {
            try {
                Employee employee = employeeRepository.findById(employeeId).get();
                employee.setActive(activeState);
                employeeRepository.save(employee);

                responseMessage = new ResponseMessageResource(CommonResponseCodes.SUCCESSFUL);
                responseMessage.add(linkTo(methodOn(EmployeeController.class).show(employeeId)).withRel("result"));
            } catch (Exception e) {
                responseMessage = new ResponseMessageResource(CommonResponseCodes.FAILED, e.getMessage());
            }
        } else
            responseMessage = new ResponseMessageResource(CommonResponseCodes.NOT_EXISTS);

        if (activeState)
            responseMessage.add(linkTo(methodOn(EmployeeController.class).returnEmployee(employeeId)).withSelfRel());
        else
            responseMessage.add(linkTo(methodOn(EmployeeController.class).dismissEmployee(employeeId)).withSelfRel());

        return responseMessage;
    }
}
