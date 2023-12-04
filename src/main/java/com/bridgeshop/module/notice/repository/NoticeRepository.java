package com.bridgeshop.module.notice.repository;

import com.bridgeshop.module.notice.entity.Notice;
import com.bridgeshop.module.notice.entity.NoticeImageType;
import com.bridgeshop.module.notice.entity.NoticeStatus;
import com.bridgeshop.module.notice.entity.NoticeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface NoticeRepository extends JpaRepository<Notice, Long>, NoticeRepositoryCustom {
    List<Notice> findAllByStatusAndIsModalImage(NoticeStatus status, boolean isModalImage);

    List<Notice> findAllByStatusAndNoticeImages_Type(NoticeStatus status, NoticeImageType imageType, Sort sort);

    List<Notice> findAllByStatusAndIsSliderImageAndNoticeImages_Type(NoticeStatus status, boolean isSliderImage, NoticeImageType imageType, Sort sort);

    @Query("SELECT n FROM Notice n JOIN FETCH n.noticeImages ni WHERE n.status = :status AND n.isSliderImage = true AND ni.type = :type")
    List<Notice> findActiveNoticesWithSliderImage(@Param("status") NoticeStatus status, @Param("type") NoticeImageType type);

    List<Notice> findAllByStatusAndTypeAndNoticeImages_Type(NoticeStatus status, NoticeType type, NoticeImageType imageType, Sort sort);

    Page<Notice> findAllByNoticeImages_Type(NoticeImageType imageType, Pageable pageable);
}