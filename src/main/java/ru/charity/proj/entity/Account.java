package ru.charity.proj.entity;

import com.google.gson.internal.LinkedTreeMap;
import lombok.Data;

@Data
public class Account {
    Double balance;

    public Account(LinkedTreeMap map){
        LinkedTreeMap temp = (LinkedTreeMap)map.get("balance");
        balance = (Double) temp.get("amount");
    }
}
