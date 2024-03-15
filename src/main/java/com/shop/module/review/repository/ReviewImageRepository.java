package com.shop.module.review.repository;

import com.shop.module.review.entity.ReviewImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReviewImageRepository extends JpaRepository<ReviewImage, Long> {

    int countByReview_Id(Long reviewId);

    List<ReviewImage> findAllByReview_Id(Long reviewId);

    @Modifying
    @Query("DELETE FROM ReviewImage ri WHERE ri.review.id = :reviewId")
    void deleteAllByReview_Id(@Param("reviewId") Long reviewId);
}
