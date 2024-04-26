// Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
// SPDX-License-Identifier: MIT-0

package com.example.product.handler;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.example.product.entity.Product;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import com.example.product.dao.ProductDao;

import java.util.function.Function;

@Component
public class CreateProductFunction implements Function<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {
  @Autowired
  private ProductDao productDao;

  private ObjectMapper objectMapper = new ObjectMapper();

  @Override
  public APIGatewayProxyResponseEvent apply(APIGatewayProxyRequestEvent requestEvent) {
    if (!requestEvent.getHttpMethod().equals(HttpMethod.PUT.name())) {
      return new APIGatewayProxyResponseEvent()
        .withStatusCode(401)
        .withBody("Only PUT method is supported");
    }
    try {
      String id = requestEvent.getPathParameters().get("id");
      String jsonPayload = requestEvent.getBody();
      Product product = objectMapper.readValue(jsonPayload, Product.class);
      if (!product.getProductId().equals(id)) {
        return new APIGatewayProxyResponseEvent()
          .withStatusCode(400)
          .withBody("Product ID in the body does not match path parameter");
      }
      productDao.putProduct(product);
      return new APIGatewayProxyResponseEvent()
        .withStatusCode(202)
        .withBody("Product with id = " + id + " created");
    } catch (Exception e) {
      e.printStackTrace();
      return new APIGatewayProxyResponseEvent()
        .withStatusCode(500)
        .withBody("Internal Server Error :: " + e.getMessage());
    }
  }
}
