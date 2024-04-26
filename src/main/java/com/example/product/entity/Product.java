// Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
// SPDX-License-Identifier: MIT-0

package com.example.product.entity;

import org.springframework.stereotype.Component;

public class Product {
  String ProductId;
  String name;
  Float price;

  public Product(String productId, String name, Float price) {
    ProductId = productId;
    this.name = name;
    this.price = price;
  }

  public String getProductId() {
    return ProductId;
  }

  public void setProductId(String productId) {
    ProductId = productId;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Float getPrice() {
    return price;
  }

  public void setPrice(Float price) {
    this.price = price;
  }
}
