// Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
// SPDX-License-Identifier: MIT-0

package com.example.product.dao;

import com.example.product.entity.Product;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;

import java.util.Map;

public class ProductMapper {
  private static final String PK = "ProductId";
  private static final String NAME = "name";
  private static final String PRICE = "price";


  public static Product productFromDynamoDB(Map<String, AttributeValue> items) {
    return new Product(
      items.get(PK).getS(),
      items.get(NAME).getS(),
      new Float(items.get(PRICE).getN())
    );
  }

  public static Map<String, AttributeValue> productToDynamoDb(Product product) {
    return Map.of(
      PK, new AttributeValue().withS(product.getProductId()),
      NAME,  new AttributeValue().withS(product.getName()),
      PRICE,  new AttributeValue().withN(product.getPrice().toString())
    );
  }
}
