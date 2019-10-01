package ru.charity.proj.controller.rest;

import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import ru.charity.proj.restclient.RestClient;

@RestController
@RequestMapping("${charity.route.balance}")
public class BalanceController {

    @Autowired
    private RestClient restClient;

    private static final Gson gson = new Gson();

    @RequestMapping(value = "/rub", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getService() {
        return ResponseEntity.ok(gson.toJson("amount:" + restClient.getBalance().intValue()));
    }


    @RequestMapping(value = "/ping", method = RequestMethod.GET)
    public ResponseEntity getPing() {
        return ResponseEntity.ok("PONG!!");
    }
}
