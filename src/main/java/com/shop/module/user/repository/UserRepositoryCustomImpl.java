package com.shop.module.user.repository;

import com.shop.module.user.entity.QUser;
import com.shop.module.user.entity.User;
import com.shop.module.user.dto.UserListSearchRequest;
import com.shop.common.util.QueryDslUtils;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;


public class UserRepositoryCustomImpl implements UserRepositoryCustom {
    @PersistenceContext
    private EntityManager em;

    @Override
    public Page<User> findByCondition(UserListSearchRequest userListSearchRequest, Pageable pageable) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        QUser qUser = QUser.user;

        JPAQuery<User> query = queryFactory
                .selectFrom(qUser)
                .where(
                        QueryDslUtils.eqAuthProvider(qUser.authProvider, userListSearchRequest.getAuthProvider()),
                        QueryDslUtils.likeString(qUser.email, userListSearchRequest.getEmail()),
                        QueryDslUtils.likeString(qUser.name, userListSearchRequest.getName()),
                        QueryDslUtils.likeString(qUser.phoneNumber, userListSearchRequest.getPhoneNumber()),
                        QueryDslUtils.eqBoolean(qUser.isAdmin, userListSearchRequest.getIsAdmin()),
                        QueryDslUtils.eqBoolean(qUser.isActivate, userListSearchRequest.getIsActivate()),
                        QueryDslUtils.betweenDate(qUser.regDate, userListSearchRequest.getStartRegDate(), userListSearchRequest.getEndRegDate()),
                        QueryDslUtils.betweenDate(qUser.modDate, userListSearchRequest.getStartModDate(), userListSearchRequest.getEndModDate())
                )
                .orderBy(qUser.regDate.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());

        List<User> users = query.fetch();
        long totalCount = query.fetchCount();

        return new PageImpl<>(users, pageable, totalCount);
    }

//    private BooleanExpression likeIfNotNullAndNotEmpty(StringExpression path, String value) {
//        return (value != null && !value.equals("")) ? path.like("%" + value + "%") : null;
//    }

//    private BooleanExpression eqAuthProviderIfNotNullAndNotEmpty(EnumPath<AuthProvider> path, String value) {
//        return (value != null && !value.equals("") && !value.equals("ALL")) ? path.eq(AuthProvider.valueOf(value)) : null;
//    }
}