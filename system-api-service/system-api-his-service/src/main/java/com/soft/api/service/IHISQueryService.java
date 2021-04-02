package com.soft.api.service;

import org.springframework.web.bind.annotation.RequestMapping;

public interface IHISQueryService {

    @RequestMapping(value="/outPatientRegisteredQuery")
    public String outPatientRegisteredQuery(String param);
}
