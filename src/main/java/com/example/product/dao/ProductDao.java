// Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
// SPDX-License-Identifier: MIT-0

package com.example.product.dao;

import com.example.product.entity.Product;
import org.springframework.stereotype.Component;
import com.example.product.entity.Products;

import java.util.Optional;

@Component
public interface ProductDao {
  Optional<Product> getProduct(String ProductId);

  void putProduct(Product product);

  void deleteProduct(String ProductId);

  Products getAllProduct();
}
