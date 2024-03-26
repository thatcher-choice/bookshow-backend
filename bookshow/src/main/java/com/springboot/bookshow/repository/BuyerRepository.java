package com.springboot.bookshow.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.springboot.bookshow.model.Buyer;

@RepositoryRestResource
public interface BuyerRepository extends JpaRepository<Buyer, Long> {

}
