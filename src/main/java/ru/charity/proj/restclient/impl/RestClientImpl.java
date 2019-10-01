package ru.charity.proj.restclient.impl;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import ru.charity.proj.config.RestConfig;
import ru.charity.proj.entity.Account;
import ru.charity.proj.restclient.RestClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Component
public class RestClientImpl implements RestClient {

    @Autowired
    private RestConfig restConfig;

    @Value("${qiwi.login}")
    private String login;

    @Value("${qiwi.accessToken}")
    private String accessToken;

    private final static Logger LOG = LoggerFactory.getLogger(RestClientImpl.class.getName());

    @Override
    public Double getBalance() {
        Account ac = null;
        HttpEntity<String> entity = new HttpEntity<String>(getRestHeaders());
        ResponseEntity<String> response = restConfig.getRestTemplate()
                .exchange(getBalanceUrl(), HttpMethod.GET, entity, String.class);

        try {
            Gson gson = new Gson();
            HashMap<String, Object> jsonMap = gson.fromJson(response.getBody(), HashMap.class);
            ArrayList accounts = (ArrayList) jsonMap.get("accounts");
            ac = new Account((LinkedTreeMap) accounts.get(0));
            LOG.info("Баланс: " + ac.getBalance());
        } catch (Exception e) {
            LOG.error("Ошибка обработки ответа от Qiwi. Не парсится баланс");
        }
        return ac != null ? ac.getBalance() : null;
    }

    private String getBalanceUrl() {
        return "https://edge.qiwi.com/funding-sources/v2/persons/" + login + "/accounts";
    }

    private HttpHeaders getRestHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + accessToken);
        return headers;
    }
}
