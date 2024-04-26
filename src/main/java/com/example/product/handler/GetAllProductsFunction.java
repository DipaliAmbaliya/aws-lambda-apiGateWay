// Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
// SPDX-License-Identifier: MIT-0

package com.example.product.handler;

import java.util.function.Function;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.example.product.dao.DynamoProductDao;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.product.dao.ProductDao;

import org.springframework.stereotype.Component;
/*
url : https://bl224w68vc.execute-api.us-east-1.amazonaws.com/lambda/products
 */
@Component
public class GetAllProductsFunction implements Function<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

  private ProductDao productDao = new DynamoProductDao();

  private ObjectMapper objectMapper = new ObjectMapper();

  @Override
  public APIGatewayProxyResponseEvent apply(APIGatewayProxyRequestEvent requestEvent) {
    try {
      return new APIGatewayProxyResponseEvent()
        .withStatusCode(200)
        .withBody(objectMapper.writeValueAsString(productDao.getAllProduct()));
    } catch (JsonProcessingException je) {
      je.printStackTrace();
      return new APIGatewayProxyResponseEvent()
        .withStatusCode(500)
        .withBody("Internal Server Error");
    }
  }
}
