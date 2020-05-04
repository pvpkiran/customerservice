package com.example.customerservice.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.SequenceGenerator;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
public class Tag {

    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator = "tag_seq_gen")
    @SequenceGenerator(sequenceName = "tag_seq", allocationSize = 1, name = "tag_seq_gen")
    private Long id;

    @ManyToMany(mappedBy = "tags", fetch = FetchType.LAZY)
    @JsonBackReference
    private Set<Customer> customers;

    private String label;
}
