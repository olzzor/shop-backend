package com.shop.module.notice.repository;

import com.shop.module.notice.entity.NoticeImage;
import com.shop.module.notice.entity.NoticeImageType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface NoticeImageRepository extends JpaRepository<NoticeImage, Long> {

    int countByNotice_Id(Long noticeId);

    Optional<NoticeImage> findByTypeAndNotice_Id(NoticeImageType noticeImageType, Long noticeId);

    List<NoticeImage> findAllByNotice_Id(Long noticeId);

    @Modifying
    @Query("DELETE FROM NoticeImage nif WHERE nif.notice.id = :noticeId")
    void deleteAllByNotice_Id(@Param("noticeId") Long noticeId);
}
