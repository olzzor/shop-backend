package com.bridgeshop.module.review.dto;

import com.bridgeshop.common.entity.BaseTimeEntity;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "review_images")
public class ReviewImage extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JsonManagedReference
    @JoinColumn(name = "review_id", referencedColumnName = "id")
    private Review review;

    @Column(nullable = false, length = 100)
    private String filePath;

    @Column(nullable = false, length = 100)
    private String fileName;

    @Column(nullable = false, length = 50)
    private int displayOrder;
}
