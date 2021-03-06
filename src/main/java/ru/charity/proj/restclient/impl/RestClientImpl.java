package ru.charity.proj.restclient.impl;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import ru.charity.proj.config.RestConfig;
import ru.charity.proj.restclient.RestClient;

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
        Double balance  = null;
        HttpEntity<String> entity = new HttpEntity<String>(getRestHeaders());
        ResponseEntity<String> response = restConfig.getRestTemplate()
                .exchange(getBalanceUrl(), HttpMethod.GET, entity, String.class);

        try {
            JSONObject obj = new JSONObject(response.getBody());
            JSONObject account = (JSONObject) obj.getJSONArray("accounts").get(0);
            balance = (Double) account.getJSONObject("balance").get("amount");
            LOG.info("Баланс: " + balance);
        } catch (Exception e) {
            LOG.error("Ошибка обработки ответа от Qiwi. Не парсится баланс");
        }
        return balance;
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
