package com.kyulix.RGTestApp.controllers;

import com.kyulix.RGTestApp.RgTestAppApplication;
import com.kyulix.RGTestApp.constants.OfficeResponseCodes;
import com.kyulix.RGTestApp.entities.Employee;
import com.kyulix.RGTestApp.entities.Office;
import com.kyulix.RGTestApp.repositories.EmployeeRepository;
import com.kyulix.RGTestApp.repositories.OfficeRepository;
import com.kyulix.RGTestApp.resources.OfficeResource;
import com.kyulix.RGTestApp.resources.ResponseMessageResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RestController
@RequestMapping("/offices")
public class OfficeController {

    private static final Logger logger = LoggerFactory.getLogger(RgTestAppApplication.class);

    @Autowired
    private OfficeRepository officeRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @RequestMapping("/showAll")
    public HttpEntity<OfficeResource> showAll() {

        OfficeResource officeResource = new OfficeResource(officeRepository.findAll());
        officeResource.add(linkTo(methodOn(OfficeController.class).showAll()).withSelfRel());

        return new ResponseEntity(officeResource, HttpStatus.OK);
    }

    @RequestMapping("/show")
    public HttpEntity<OfficeResource> show(@RequestParam(value = "id", required = true) int id) {

        OfficeResource officeResource = new OfficeResource(officeRepository.findById(id).get());
        officeResource.add(linkTo(methodOn(OfficeController.class).show(id)).withSelfRel());

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
            try {
                Office officeToAppend = new Office(name, address);
                officeRepository.save(officeToAppend);

                logger.info(String.format("New Office = %s", officeToAppend.toString()));

                responseMessage = new ResponseMessageResource(OfficeResponseCodes.SUCCESSFUL);
                responseMessage.add(linkTo(methodOn(OfficeController.class).show(officeToAppend.getId())).withRel("result"));
            } catch (Exception e) {
                responseMessage = new ResponseMessageResource(OfficeResponseCodes.FAILED, e.getMessage());
            }
        }

        responseMessage.add(linkTo(methodOn(OfficeController.class).addOffice(name, address)).withSelfRel());

        return new ResponseEntity(responseMessage, HttpStatus.OK);
    }

    @RequestMapping("/change")
    public HttpEntity<ResponseMessageResource> changeOffice(@RequestParam(value = "id", required = true) int id,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "address", required = false) String address) {

        ResponseMessageResource responseMessage;

        if (officeRepository.existsById(id)) {
            try {
                Office officeToChange = officeRepository.findById(id).get();

                if (name != null)
                    officeToChange.setName(name);

                if (address != null)
                    officeToChange.setAddress(address);

                officeRepository.save(officeToChange);

                logger.info(String.format("Changed Office = %s", officeToChange.toString()));

                responseMessage = new ResponseMessageResource(OfficeResponseCodes.SUCCESSFUL);
                responseMessage.add(linkTo(methodOn(OfficeController.class).show(id)).withRel("result"));
            } catch (Exception e) {
                responseMessage = new ResponseMessageResource(OfficeResponseCodes.FAILED, e.getMessage());
            }
        } else
            responseMessage = new ResponseMessageResource(OfficeResponseCodes.NOT_EXISTS);

        responseMessage.add(linkTo(methodOn(OfficeController.class).changeOffice(id, name, address)).withSelfRel());

        return new ResponseEntity(responseMessage, HttpStatus.OK);
    }

    @RequestMapping("/acceptEmployees")
    public HttpEntity<ResponseMessageResource> acceptEmployees(
            @RequestParam(value = "id", required = true) int id,
            @RequestParam(value = "employeesId", required = true) String employeesId) {

        ResponseMessageResource responseMessage;

        if (officeRepository.existsById(id)) {
            try {
                Office office = officeRepository.findById(id).get();

                responseMessage = new ResponseMessageResource(OfficeResponseCodes.SUCCESSFUL);
                responseMessage.add(linkTo(methodOn(OfficeController.class).show(id)).withRel("result"));

                for (String employeeId : employeesId.split(",")) {
                    if (employeeRepository.existsById(Integer.parseInt(employeeId))) {
                        Employee employeeToAccept = employeeRepository.findById(Integer.parseInt(employeeId)).get();
                        employeeToAccept.setWorkingOffice(office);
                        employeeRepository.save(employeeToAccept);

                        logger.info(String.format("Changed Employee = %s", employeeToAccept.toString()));
                    }
                }
            } catch (Exception e) {
                responseMessage = new ResponseMessageResource(OfficeResponseCodes.FAILED, e.getMessage());
            }
        } else
            responseMessage = new ResponseMessageResource(OfficeResponseCodes.NOT_EXISTS);

        responseMessage.add(linkTo(methodOn(OfficeController.class).acceptEmployees(id, employeesId)).withSelfRel());

        return new ResponseEntity(responseMessage, HttpStatus.OK);
    }

    @RequestMapping("/close")
    public HttpEntity<ResponseMessageResource> closeOffice(@RequestParam(value = "id", required = true) int id) {

        ResponseMessageResource responseMessage;

        if (officeRepository.existsById(id)) {
            try {
                Office officeToClose = officeRepository.findById(id).get();
                officeToClose.setActive(false);
                officeRepository.save(officeToClose);

                logger.info(String.format("Changed Office = %s", officeToClose.toString()));

                responseMessage = new ResponseMessageResource(OfficeResponseCodes.SUCCESSFUL);
                responseMessage.add(linkTo(methodOn(OfficeController.class).show(id)).withRel("result"));

                for (Employee employee : employeeRepository.getByWorkingOffice(officeToClose)) {
                    employee.setActive(false);
                    employeeRepository.save(employee);

                    logger.info(String.format("Changed Employee = %s", employee.toString()));
                }
            } catch (Exception e) {
                responseMessage = new ResponseMessageResource(OfficeResponseCodes.FAILED, e.getMessage());
            }
        } else
            responseMessage = new ResponseMessageResource(OfficeResponseCodes.NOT_EXISTS);

        responseMessage.add(linkTo(methodOn(OfficeController.class).closeOffice(id)).withSelfRel());

        return new ResponseEntity(responseMessage, HttpStatus.OK);
    }
}
