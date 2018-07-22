package com.kyulix.RGTestApp.controllers;

import com.kyulix.RGTestApp.entities.Employee;
import com.kyulix.RGTestApp.entities.Office;
import com.kyulix.RGTestApp.repositories.EmployeeRepository;
import com.kyulix.RGTestApp.repositories.OfficeRepository;
import com.kyulix.RGTestApp.resources.EmployeeResource;
import com.kyulix.RGTestApp.resources.ResponseMessageResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/employees")
public class EmployeeController {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private OfficeRepository officeRepository;

    @RequestMapping
    public HttpEntity<EmployeeResource> showAll() {

        EmployeeResource employeeResource = new EmployeeResource(employeeRepository.findAll());

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

        Employee employeeToAppend = new Employee(firstName, lastName, position);

        if (eMail != null)
            employeeToAppend.setEMail(eMail);

        if (phoneNumber != null)
            employeeToAppend.setPhoneNumber(phoneNumber);

        employeeRepository.save(employeeToAppend);

        responseMessage = new ResponseMessageResource(1,
                String.format("added employee: %s", employeeToAppend.toString()));

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

            Employee employeeToChange = employeeRepository.findById(id).get();
            String oldEmployeeString = employeeToChange.toString();

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

            responseMessage = new ResponseMessageResource(1,
                    String.format("%s -> %s", oldEmployeeString, employeeToChange.toString()));
        } else
            responseMessage = new ResponseMessageResource(4, String.format("employee with id %d not exists", id));

        return new ResponseEntity(responseMessage, HttpStatus.OK);
    }

    @RequestMapping("/bindToOffice")
    public HttpEntity<ResponseMessageResource> bindEmployeeToOffice(
            @RequestParam(value = "id", required = true) int id,
            @RequestParam(value = "officeId", required = true) int officeId) {

        ResponseMessageResource responseMessage;

        if ((employeeRepository.existsById(id)) && (officeRepository.existsById(officeId))) {

            Employee employeeToChange = employeeRepository.findById(id).get();
            Office officeToBind = officeRepository.findById(officeId).get();

            employeeToChange.setWorkingOffice(officeToBind);

            employeeRepository.save(employeeToChange);

            responseMessage = new ResponseMessageResource(1,
                    String.format("%s -> %s", employeeToChange.toString(), officeToBind.toString()));
        } else
            responseMessage = new ResponseMessageResource(4, "employee or office not exists");

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

        if (employeeRepository.existsById(employeeId)) {

            Employee employeeToDismiss = employeeRepository.findById(employeeId).get();
            employeeToDismiss.setActive(activeState);

            String actionName = activeState ? "returned to work" : "is fired";

            employeeRepository.save(employeeToDismiss);

            return new ResponseMessageResource(1, String.format("Employee with id %d %s", employeeId, actionName));
        } else
            return new ResponseMessageResource(4, String.format("Employee with id %d not exists", employeeId));
    }

}
