package com.bridgeshop.module.review.repository;

import com.bridgeshop.module.review.entity.QReview;
import com.bridgeshop.module.review.entity.Review;
import com.bridgeshop.module.review.dto.ReviewListSearchRequest;
import com.bridgeshop.module.user.entity.QUser;
import com.bridgeshop.common.util.QueryDslUtils;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

public class ReviewRepositoryCustomImpl implements ReviewRepositoryCustom {
    @PersistenceContext
    private EntityManager em;

    @Override
    public Page<Review> findByCondition(ReviewListSearchRequest reviewListSearchRequest, Pageable pageable) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        QReview qReview = QReview.review;
        QUser qUser = QUser.user;

        JPAQuery<Review> query = queryFactory
                .selectFrom(qReview).distinct()
                .leftJoin(qReview.user, qUser)
                .where(
                        QueryDslUtils.eqByte(qReview.rating, reviewListSearchRequest.getRating()),
                        QueryDslUtils.likeString(qReview.title, reviewListSearchRequest.getTitle()),
                        QueryDslUtils.likeString(qReview.content, reviewListSearchRequest.getContent()),
                        QueryDslUtils.likeString(qUser.email, reviewListSearchRequest.getUserEmail()),
                        QueryDslUtils.eqBoolean(qReview.activateFlag, reviewListSearchRequest.getActivateFlag()),
                        QueryDslUtils.betweenDate(qReview.regDate, reviewListSearchRequest.getStartRegDate(), reviewListSearchRequest.getEndRegDate()),
                        QueryDslUtils.betweenDate(qReview.modDate, reviewListSearchRequest.getStartModDate(), reviewListSearchRequest.getEndModDate())
                )
                .orderBy(qReview.regDate.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());

        List<Review> reviewList = query.fetch();
        long totalCount = query.fetchCount();

        return new PageImpl<>(reviewList, pageable, totalCount);
    }
}