package com.mockservice.domain;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import org.springframework.context.annotation.Configuration;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Configuration
public class RequestFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletResponse response = (HttpServletResponse) servletResponse;
        HttpServletRequest request = (HttpServletRequest) servletRequest;

        List<ServiceDefinition> servicesDefinitions = getServicesDefinitions();

        ServiceDefinition serviceDefinition = servicesDefinitions.stream()
                .filter(sd -> sd.getPath().equals(request.getRequestURI()) && sd.getMethod().equals(request.getMethod()))
                .findFirst().orElseThrow(() -> new ServletException("No valid mock config found for your request"));

        response.setStatus(serviceDefinition.getStatusCode());
        response.getWriter().write(serviceDefinition.getResponseBody());
    }

    private List<ServiceDefinition> getServicesDefinitions() throws FileNotFoundException {

        String file = "src/main/resources/json/services-mock-config.json";

        JsonReader reader = new JsonReader(new FileReader(file));
        return Arrays.asList(new Gson().fromJson(reader, ServiceDefinition[].class));
    }
}
