package com.udacity.vehicles.service;

import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Service
public class EndpointDiscoveryService {


    private  DiscoveryClient discoveryClient;

    public EndpointDiscoveryService(DiscoveryClient discoveryClient) {
        this.discoveryClient = discoveryClient;
    }

    //return Webclientendpoints for Reueests


    public WebClient getPriceServiceWebsClient (){
        List<ServiceInstance> instances = discoveryClient.getInstances("PRICE-SERVICE");
        if (instances.isEmpty()) {
            throw new IllegalStateException("Keine Instanzen von PRICE-SERVICE gefunden");
        }
        ServiceInstance instance = instances.get(0); // Nehmen Sie die erste verfügbare Instanz
        System.out.println(String.format("http://%s:%s", instance.getHost(), instance.getPort()));
        String endpoint =String.format("http://%s:%s", instance.getHost(), instance.getPort());
        return WebClient.create(endpoint);
    }


}
