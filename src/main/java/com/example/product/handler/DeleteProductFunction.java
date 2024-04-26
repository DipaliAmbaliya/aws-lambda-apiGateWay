// Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
// SPDX-License-Identifier: MIT-0

package com.example.product.handler;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import com.example.product.dao.ProductDao;
import com.example.product.entity.Product;

import java.util.Optional;
import java.util.function.Function;

@Component
public class DeleteProductFunction implements Function<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {
  @Autowired
  private ProductDao productDao;

  @Override
  public APIGatewayProxyResponseEvent apply(APIGatewayProxyRequestEvent requestEvent) {
    if (!requestEvent.getHttpMethod().equals(HttpMethod.DELETE.name())) {
      return new APIGatewayProxyResponseEvent()
        .withStatusCode(401)
        .withBody("Only DELETE method is supported");
    }
    try {
      String id = requestEvent.getPathParameters().get("id");
      Optional<Product> product = productDao.getProduct(id);
      if (product.isEmpty()) {
        return new APIGatewayProxyResponseEvent()
          .withStatusCode(404)
          .withBody("Product with id = " + id + " not found");
      }
      productDao.deleteProduct(id);
      return new APIGatewayProxyResponseEvent()
        .withStatusCode(200)
        .withBody("Product with id = " + id + " deleted");
    } catch (Exception je) {
      je.printStackTrace();
      return new APIGatewayProxyResponseEvent()
        .withStatusCode(500)
        .withBody("Internal Server Error :: " + je.getMessage());
    }
  }
}
