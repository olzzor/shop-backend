package com.bridgeshop.module.review.repository;

import com.bridgeshop.module.review.dto.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long>, ReviewRepositoryCustom {

    Optional<Review> findByOrder_Id(Long orderId);

    Page<Review> findAllByActivateFlag(boolean activateFlag, Pageable pageable);
//    List<Review> findAllByStatusAndIsModalImage(NoticeStatus status, boolean isModalImage);
//    List<Review> findAllByStatusAndNoticeImageFiles_Type(NoticeStatus status, NoticeImageType imageType, Sort sort);
//    List<Review> findAllByStatusAndIsSliderImageAndNoticeImageFiles_Type(NoticeStatus status, boolean isSliderImage, NoticeImageType imageType, Sort sort);
//    @Query("SELECT n FROM Notice n JOIN FETCH n.noticeImageFiles nf WHERE n.status = :status AND n.isSliderImage = true AND nf.type = :type")
//    List<Review> findActiveNoticesWithSliderImage(@Param("status") NoticeStatus status, @Param("type") NoticeImageType type);
//    List<Review> findAllByStatusAndTypeAndNoticeImageFiles_Type(NoticeStatus status, NoticeType type, NoticeImageType imageType, Sort sort);
//    Page<Review> findAllByNoticeImageFiles_Type(NoticeImageType imageType, Pageable pageable);

}