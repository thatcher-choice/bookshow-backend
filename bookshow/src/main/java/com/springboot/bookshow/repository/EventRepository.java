package com.springboot.bookshow.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.springboot.bookshow.model.Event;

@RepositoryRestResource
public interface EventRepository extends JpaRepository<Event, Long> {

}
