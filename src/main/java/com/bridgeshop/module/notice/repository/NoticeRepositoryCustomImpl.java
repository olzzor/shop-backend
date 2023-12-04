package com.bridgeshop.module.notice.repository;

import com.bridgeshop.module.notice.dto.NoticeListSearchRequest;
import com.bridgeshop.module.notice.entity.Notice;
import com.bridgeshop.module.notice.entity.QNotice;
import com.bridgeshop.common.util.QueryDslUtils;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

public class NoticeRepositoryCustomImpl implements NoticeRepositoryCustom {
    @PersistenceContext
    private EntityManager em;

    @Override
    public Page<Notice> findByCondition(NoticeListSearchRequest noticeListSearchRequest, Pageable pageable) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        QNotice qNotice = QNotice.notice;

        JPAQuery<Notice> query = queryFactory
                .selectFrom(qNotice)
                .where(
                        QueryDslUtils.eqNoticeType(qNotice.type, noticeListSearchRequest.getType()),
                        QueryDslUtils.likeString(qNotice.title, noticeListSearchRequest.getTitle()),
                        QueryDslUtils.eqBoolean(qNotice.isModalImage, noticeListSearchRequest.getIsModalImage()),
                        QueryDslUtils.eqBoolean(qNotice.isSliderImage, noticeListSearchRequest.getIsSliderImage()),
                        QueryDslUtils.eqNoticeStatus(qNotice.status, noticeListSearchRequest.getStatus()),
                        QueryDslUtils.betweenDate(qNotice.regDate, noticeListSearchRequest.getStartRegDate(), noticeListSearchRequest.getEndRegDate()),
                        QueryDslUtils.betweenDate(qNotice.modDate, noticeListSearchRequest.getStartModDate(), noticeListSearchRequest.getEndModDate())
                )
                .orderBy(qNotice.regDate.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());

        List<Notice> NoticeList = query.fetch();
        long totalCount = query.fetchCount();

        return new PageImpl<>(NoticeList, pageable, totalCount);
    }
}