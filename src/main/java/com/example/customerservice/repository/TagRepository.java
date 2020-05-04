package com.example.customerservice.repository;

import com.example.customerservice.model.entity.Tag;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface TagRepository extends PagingAndSortingRepository<Tag, Long> {
    Optional<Tag> findByLabel(String label);
}
