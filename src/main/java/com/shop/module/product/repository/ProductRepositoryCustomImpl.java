package com.shop.module.product.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.shop.common.util.QueryDslUtils;
import com.shop.module.category.entity.QCategory;
import com.shop.module.product.dto.ProductListSearchRequest;
import com.shop.module.product.entity.Product;
import com.shop.module.product.entity.QProduct;
import com.shop.module.product.entity.QProductImage;
import com.shop.module.product.entity.QProductSize;
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
//        booleanBuilder.and(qProduct.status.eq(ProductStatus.ON_SALE)); // 20240318 품절 상품 포함
        booleanBuilder.and(qProduct.isDisplay.isTrue()); // 20240411 표시 대상 상품 필터

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
//                        QueryDslUtils.eqString(qCategory.code, productListSearchRequest.getCategoryCode()),
                        eqCategoryCode(qCategory, productListSearchRequest.getCategoryCode()),
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

    @Override
    public Page<Product> findByConditionExcludingRecommended(ProductListSearchRequest productListSearchRequest, List<Long> excludedProductIds, Pageable pageable) {
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
//                        QueryDslUtils.eqString(qCategory.code, productListSearchRequest.getCategoryCode()),
                        eqCategoryCode(qCategory, productListSearchRequest.getCategoryCode()),
                        QueryDslUtils.likeString(qProduct.name, productListSearchRequest.getName()),
                        QueryDslUtils.likeString(qProductSize.size, productListSearchRequest.getProductSize()),
                        QueryDslUtils.eqInteger(qProduct.price, productListSearchRequest.getPrice()),
                        QueryDslUtils.eqInteger(qProduct.discountPer, productListSearchRequest.getDiscountPer()),
                        QueryDslUtils.eqProductStatus(qProduct.status, productListSearchRequest.getStatus()),
                        QueryDslUtils.betweenDate(qProduct.regDate, productListSearchRequest.getStartRegDate(), productListSearchRequest.getEndRegDate()),
                        QueryDslUtils.betweenDate(qProduct.modDate, productListSearchRequest.getStartModDate(), productListSearchRequest.getEndModDate()),
                        qProduct.id.notIn(excludedProductIds) // 추천 상품 목록 제외
                )
                .orderBy(qProduct.regDate.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());

        List<Product> productList = query.fetch();
        long totalCount = query.fetchCount(); // 전체 카운트 취득

        return new PageImpl<>(productList, pageable, totalCount);
    }

    @Override
    public Page<Product> findAllExcludingRecommended(List<Long> excludedProductIds, Pageable pageable) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        QProduct qProduct = QProduct.product;

        JPAQuery<Product> query = queryFactory
                .selectFrom(qProduct)
                .where(qProduct.id.notIn(excludedProductIds)) // 추천 상품 목록 제외
                .orderBy(qProduct.regDate.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());

        List<Product> productList = query.fetch();
        long totalCount = query.fetchCount(); // 전체 카운트 취득

        return new PageImpl<>(productList, pageable, totalCount);
    }

    private BooleanExpression eqCategoryCode(QCategory qCategory, String categoryCode) {
        if (categoryCode != null && !categoryCode.isEmpty()) {
            if (categoryCode.endsWith("0")) { // 부모 카테고리 코드인 경우
                return qCategory.codeRef.eq(categoryCode);
            } else { // 특정 하위 카테고리 코드인 경우
                return qCategory.code.eq(categoryCode);
            }
        } else {
            return null;
        }
    }
}