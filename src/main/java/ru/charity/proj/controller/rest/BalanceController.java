package ru.charity.proj.controller.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.charity.proj.restclient.RestClient;

@RestController
@RequestMapping("${charity.route.balance}")
public class BalanceController {

    @Autowired
    private RestClient restClient;

    @RequestMapping(value = "/rub", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity getService() {
        return ResponseEntity.ok(restClient.getBalance());
    }


    @RequestMapping(value = "/ping", method = RequestMethod.GET)
    public ResponseEntity getPing() {
        return ResponseEntity.ok("PONG!!");
    }
}
