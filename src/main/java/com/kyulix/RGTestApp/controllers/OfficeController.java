package com.kyulix.RGTestApp.controllers;

import com.kyulix.RGTestApp.entities.Office;
import com.kyulix.RGTestApp.repositories.OfficeRepository;
import com.kyulix.RGTestApp.resources.OfficeResource;
import com.kyulix.RGTestApp.resources.ResponseMessageResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/offices")
public class OfficeController {

    @Autowired
    private OfficeRepository officeRepository;

    @RequestMapping(method = RequestMethod.GET)
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
            responseMessage = new ResponseMessageResource(1, "office already exists");
        else {
            Office officeToAppend = new Office(name, address);
            officeRepository.save(officeToAppend);

            responseMessage = new ResponseMessageResource(1,
                    "Added office: " + officeToAppend.toString());
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

            responseMessage = new ResponseMessageResource(1,
                    String.format("%s -> %s", oldOfficeString, officeToChange.toString()));
        } else
            responseMessage = new ResponseMessageResource(4, String.format("office with id %d not exists", id));

        return new ResponseEntity(responseMessage, HttpStatus.OK);
    }

    @RequestMapping("/close")
    public HttpEntity<ResponseMessageResource> closeOffice(@RequestParam(value = "id", required = true) int id) {

        ResponseMessageResource responseMessage;

        if (officeRepository.existsById(id)) {

            Office officeToClose = officeRepository.findById(id).get();
            officeToClose.setIsClosed(true);

            officeRepository.save(officeToClose);

            responseMessage = new ResponseMessageResource(1, String.format("office with id %d closed", id));
        } else
            responseMessage = new ResponseMessageResource(4, String.format("office with id %d not exists", id));

        return new ResponseEntity(responseMessage, HttpStatus.OK);
    }
}
