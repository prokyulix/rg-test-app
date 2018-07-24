package com.kyulix.RGTestApp.controllers;

import com.kyulix.RGTestApp.constants.OfficeResponseCodes;
import com.kyulix.RGTestApp.entities.Employee;
import com.kyulix.RGTestApp.entities.Office;
import com.kyulix.RGTestApp.repositories.EmployeeRepository;
import com.kyulix.RGTestApp.repositories.OfficeRepository;
import com.kyulix.RGTestApp.resources.OfficeResource;
import com.kyulix.RGTestApp.resources.ResponseMessageResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/offices")
public class OfficeController {

    @Autowired
    private OfficeRepository officeRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @RequestMapping
    public HttpEntity<OfficeResource> showAll() {

        OfficeResource officeResource = new OfficeResource(officeRepository.findAll());

        return new ResponseEntity(officeResource, HttpStatus.OK);
    }

    @RequestMapping("/add")
    public HttpEntity<ResponseMessageResource> addOffice(
            @RequestParam(value = "name", required = true) String name,
            @RequestParam(value = "address", required = true) String address) {

        ResponseMessageResource responseMessage;

        if ((officeRepository.existsByName(name)) || (officeRepository.existsByAddress(address)))
            responseMessage = new ResponseMessageResource(OfficeResponseCodes.ALREADY_EXISTS);
        else {
            Office officeToAppend = new Office(name, address);
            officeRepository.save(officeToAppend);

            responseMessage = new ResponseMessageResource(OfficeResponseCodes.SUCCESSFUL);
        }

        return new ResponseEntity(responseMessage, HttpStatus.OK);
    }

    @RequestMapping("/change")
    public HttpEntity<ResponseMessageResource> changeOffice(@RequestParam(value = "id", required = true) int id,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "address", required = false) String address) {

        ResponseMessageResource responseMessage;

        if (officeRepository.existsById(id)) {

            Office officeToChange = officeRepository.findById(id).get();
            String oldOfficeString = officeToChange.toString();

            if (name != null)
                officeToChange.setName(name);

            if (address != null)
                officeToChange.setAddress(address);

            officeRepository.save(officeToChange);

            responseMessage = new ResponseMessageResource(OfficeResponseCodes.SUCCESSFUL);
        } else
            responseMessage = new ResponseMessageResource(OfficeResponseCodes.NOT_EXISTS);

        return new ResponseEntity(responseMessage, HttpStatus.OK);
    }

    @RequestMapping("/acceptEmployees")
    public HttpEntity<ResponseMessageResource> acceptEmployees(
            @RequestParam(value = "id", required = true) int id,
            @RequestParam(value = "employeesId", required = true) String employeesId) {

        ResponseMessageResource responseMessage;

        if (officeRepository.existsById(id)) {

            Office office = officeRepository.findById(id).get();

            String[] employeesToAcceptArray = employeesId.split(",");

            for (String employeeId : employeesToAcceptArray) {

                if (employeeRepository.existsById(Integer.parseInt(employeeId))) {

                    Employee employeeToAccept = employeeRepository.findById(Integer.parseInt(employeeId)).get();
                    employeeToAccept.setWorkingOffice(office);
                    employeeRepository.save(employeeToAccept);
                }
            }

            responseMessage = new ResponseMessageResource(OfficeResponseCodes.SUCCESSFUL);
        } else
            responseMessage = new ResponseMessageResource(OfficeResponseCodes.NOT_EXISTS);

        return new ResponseEntity(responseMessage, HttpStatus.OK);
    }

    @RequestMapping("/close")
    public HttpEntity<ResponseMessageResource> closeOffice(@RequestParam(value = "id", required = true) int id) {

        ResponseMessageResource responseMessage;

        if (officeRepository.existsById(id)) {

            Office officeToClose = officeRepository.findById(id).get();
            officeToClose.setActive(false);
            officeRepository.save(officeToClose);

            for (Employee employee : employeeRepository.getByWorkingOffice(officeToClose)) {

                employee.setActive(false);
                employeeRepository.save(employee);
            }

            responseMessage = new ResponseMessageResource(OfficeResponseCodes.SUCCESSFUL);
        } else
            responseMessage = new ResponseMessageResource(OfficeResponseCodes.NOT_EXISTS);

        return new ResponseEntity(responseMessage, HttpStatus.OK);
    }
}
