package com.store.user.service;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


@FeignClient(name = "Order",url = "${server.orderUrl}")
public interface OrderClient {
    @RequestMapping(method = RequestMethod.GET, value = "api/orders/user/{id}")
    Long getOrders(@PathVariable(value = "id") Long id);
}