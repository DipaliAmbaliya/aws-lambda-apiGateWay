// Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
// SPDX-License-Identifier: MIT-0

package com.example.product.dao;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.DeleteItemRequest;
import com.amazonaws.services.dynamodbv2.model.DescribeTableRequest;
import com.amazonaws.services.dynamodbv2.model.DescribeTableResult;
import com.amazonaws.services.dynamodbv2.model.GetItemRequest;
import com.amazonaws.services.dynamodbv2.model.GetItemResult;
import com.amazonaws.services.dynamodbv2.model.PutItemRequest;
import com.amazonaws.services.dynamodbv2.model.ScanRequest;
import com.amazonaws.services.dynamodbv2.model.ScanResult;
import com.example.product.entity.Product;
import com.example.product.entity.Products;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Component
public class DynamoProductDao implements ProductDao {
  private static final Logger logger = LoggerFactory.getLogger(DynamoProductDao.class);
  private static final String PRODUCT_TABLE_NAME = "Product";
  private final AmazonDynamoDB dynamoDbClient = AmazonDynamoDBClientBuilder.standard().build();

  @Override
  public Optional<Product> getProduct(String ProductId) {
    GetItemResult getItemResponse = dynamoDbClient.getItem(new GetItemRequest().
      withKey(Map.of("PK", new AttributeValue().withS(ProductId)))
      .withTableName(PRODUCT_TABLE_NAME));

    if (Objects.nonNull(getItemResponse)) {
      return Optional.of(ProductMapper.productFromDynamoDB(getItemResponse.getItem()));
    } else {
      return Optional.empty();
    }
  }

  @Override
  public void putProduct(Product product) {
    dynamoDbClient.putItem(new PutItemRequest()
      .withTableName(PRODUCT_TABLE_NAME)
      .withItem(ProductMapper.productToDynamoDb(product)));
  }

  @Override
  public void deleteProduct(String id) {
    dynamoDbClient.deleteItem(new DeleteItemRequest()
      .withTableName(PRODUCT_TABLE_NAME)
      .withKey(Map.of("PK", new AttributeValue().withS(id))));
  }

  @Override
  public Products getAllProduct() {
    ScanResult scanResponse = dynamoDbClient.scan(new ScanRequest()
      .withTableName(PRODUCT_TABLE_NAME)
      .withLimit(20));
    logger.info("Scan returned: {} item(s)", scanResponse.getCount());

    List<Product> productList = new ArrayList<>();

    for (Map<String, AttributeValue> item : scanResponse.getItems()) {
      productList.add(ProductMapper.productFromDynamoDB(item));
    }

    return new Products(productList);
  }

  public void describeTable() {
    DescribeTableResult response = dynamoDbClient.describeTable(new DescribeTableRequest()
      .withTableName(PRODUCT_TABLE_NAME));
  }

}
