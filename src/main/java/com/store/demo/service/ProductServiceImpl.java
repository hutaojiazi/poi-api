package com.store.demo.service;

import com.store.demo.model.Product;
import com.store.demo.repository.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService
{

	private ProductRepository productRepository;

	public ProductServiceImpl(ProductRepository productRepository)
	{
		this.productRepository = productRepository;
	}

	@Override
	@Transactional(readOnly = true)
	public Page<Product> getAll(final Pageable pageable)
	{
		return productRepository.findAll(pageable);
	}

	@Override
	@Transactional(readOnly = true)
	public Optional<Product> get(String id)
	{
		return productRepository.findById(id);
	}

	@Override
	@Transactional
	public String create(Product product)
	{
		return productRepository.save(product).getId();
	}
}