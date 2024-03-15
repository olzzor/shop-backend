package com.shop.module.product.repository;

import com.shop.module.category.entity.QCategory;
import com.shop.module.product.dto.ProductListSearchRequest;
import com.shop.module.product.entity.*;
import com.shop.common.util.QueryDslUtils;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

public class ProductRepositoryCustomImpl implements ProductRepositoryCustom {
    @PersistenceContext
    private EntityManager em;

    public Page<Product> searchByKeywords(String query, Pageable pageable) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        QProduct qProduct = QProduct.product;

        String[] keywords = query.split("\\s+");
        BooleanBuilder booleanBuilder = new BooleanBuilder();

        for (String keyword : keywords) {
            booleanBuilder.or(qProduct.name.containsIgnoreCase(keyword));
        }
        booleanBuilder.and(qProduct.status.eq(ProductStatus.ON_SALE));

        List<Product> results = queryFactory
                .selectFrom(qProduct)
                .where(booleanBuilder)
                .groupBy(qProduct.id) // 중복 제거
                .orderBy(qProduct.regDate.desc()) // regDate 기준으로 내림차순 정렬
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long totalCount = queryFactory
                .select(qProduct.countDistinct())
                .from(qProduct)
                .where(booleanBuilder)
                .fetchOne(); // 전체 카운트 취득

        return new PageImpl<>(results, pageable, totalCount);
    }

    @Override
    public Page<Product> findByCondition(ProductListSearchRequest productListSearchRequest, Pageable pageable) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        QProduct qProduct = QProduct.product;
        QCategory qCategory = QCategory.category;
        QProductSize qProductSize = QProductSize.productSize;
        QProductImage qProductImage = QProductImage.productImage;

        JPAQuery<Product> query = queryFactory
                .selectFrom(qProduct).distinct()
                .leftJoin(qProduct.category, qCategory)
                .leftJoin(qProduct.productSizes, qProductSize)
                .leftJoin(qProduct.productImages, qProductImage)
                .where(
                        qProductImage.displayOrder.eq(1),
                        QueryDslUtils.eqString(qCategory.code, productListSearchRequest.getCategoryCode()),
                        QueryDslUtils.likeString(qProduct.name, productListSearchRequest.getName()),
                        QueryDslUtils.likeString(qProductSize.size, productListSearchRequest.getProductSize()),
                        QueryDslUtils.eqInteger(qProduct.price, productListSearchRequest.getPrice()),
                        QueryDslUtils.eqInteger(qProduct.discountPer, productListSearchRequest.getDiscountPer()),
                        QueryDslUtils.eqProductStatus(qProduct.status, productListSearchRequest.getStatus()),
                        QueryDslUtils.betweenDate(qProduct.regDate, productListSearchRequest.getStartRegDate(), productListSearchRequest.getEndRegDate()),
                        QueryDslUtils.betweenDate(qProduct.modDate, productListSearchRequest.getStartModDate(), productListSearchRequest.getEndModDate())
                )
                .orderBy(qProduct.regDate.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());

        List<Product> productList = query.fetch();
        long totalCount = query.fetchCount(); // 전체 카운트 취득

        return new PageImpl<>(productList, pageable, totalCount);
    }
}