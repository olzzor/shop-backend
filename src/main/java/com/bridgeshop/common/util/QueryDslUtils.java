package com.bridgeshop.common.util;

import com.bridgeshop.module.contact.entity.ContactStatus;
import com.bridgeshop.module.contact.entity.ContactType;
import com.bridgeshop.module.coupon.entity.CouponDiscountType;
import com.bridgeshop.module.coupon.entity.CouponStatus;
import com.bridgeshop.module.coupon.entity.CouponType;
import com.bridgeshop.module.notice.entity.NoticeStatus;
import com.bridgeshop.module.notice.entity.NoticeType;
import com.bridgeshop.module.order.entity.OrderStatus;
import com.bridgeshop.module.product.entity.ProductStatus;
import com.bridgeshop.module.shipment.entity.CourierCompany;
import com.bridgeshop.module.shipment.entity.ShipmentStatus;
import com.bridgeshop.module.user.entity.AuthProvider;
import com.querydsl.core.types.dsl.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class QueryDslUtils {

    /**
     * 주어진 값이 null이 아니고 비어있지 않으면 like 표현식을 반환.
     *
     * @param path  엔티티의 필드에 대한 경로.
     * @param value 비교할 값.
     * @return like 쿼리를 위한 BooleanExpression 혹은 null.
     */
    public static BooleanExpression likeString(StringPath path, String value) {
        return (value != null && !value.isEmpty()) ? path.contains(value) : null;
    }

    /**
     * 주어진 값이 null이 아니고 비어있지 않으면 String 필드에 대한 equal 표현식을 반환.
     *
     * @param path  엔티티의 필드에 대한 경로.
     * @param value 비교할 값.
     * @return equal 쿼리를 위한 BooleanExpression 혹은 null.
     */

    public static BooleanExpression eqString(StringExpression path, String value) {
        return (value != null && !value.isEmpty()) ? path.eq(value) : null;
    }

    /**
     * 주어진 값이 null이 아니고 비어있지 않으면 Integer 필드에 대한 equal 표현식을 반환.
     *
     * @param path  엔티티의 필드에 대한 경로.
     * @param value 비교할 값.
     * @return equal 쿼리를 위한 BooleanExpression 혹은 null.
     */
    public static BooleanExpression eqInteger(NumberPath<Integer> path, String value) {
        return (value != null && !value.equals("")) ? path.eq(Integer.valueOf(value)) : null;
    }

    /**
     * 주어진 값이 null이 아니고 비어있지 않으면 Byte 필드에 대한 equal 표현식을 반환.
     *
     * @param path  엔티티의 필드에 대한 경로.
     * @param value 비교할 값.
     * @return equal 쿼리를 위한 BooleanExpression 혹은 null.
     */
    public static BooleanExpression eqByte(NumberPath<Byte> path, String value) {
        return (value != null && !value.isEmpty()) ? path.eq(Byte.parseByte(value)) : null;
    }

    /**
     * 주어진 값이 null이 아니고 비어있지 않으면 Boolean 필드에 대한 equal 표현식을 반환.
     *
     * @param path  엔티티의 필드에 대한 경로.
     * @param value 비교할 값.
     * @return equal 쿼리를 위한 BooleanExpression 혹은 null.
     */
    public static BooleanExpression eqBoolean(BooleanPath path, String value) {
        return (value != null && !value.equals("")) ? path.eq(Boolean.parseBoolean(value)) : null;
    }

    /**
     * 주어진 값이 null이 아니고 비어있지 않으면 AuthProvider 필드에 대한 equal 표현식을 반환.
     *
     * @param path  엔티티의 필드에 대한 경로.
     * @param value 비교할 값.
     * @return equal 쿼리를 위한 BooleanExpression 혹은 null.
     */
    public static BooleanExpression eqAuthProvider(EnumPath<AuthProvider> path, String value) {
        return (value != null && !value.equals("")) ? path.eq(AuthProvider.valueOf(value)) : null;
    }

    /**
     * 주어진 값이 null이 아니고 비어있지 않으면 CourierCompany 필드에 대한 equal 표현식을 반환.
     *
     * @param path  엔티티의 필드에 대한 경로.
     * @param value 비교할 값.
     * @return equal 쿼리를 위한 BooleanExpression 혹은 null.
     */
    public static BooleanExpression eqCourierCompany(EnumPath<CourierCompany> path, String value) {
        return (value != null && !value.equals("")) ? path.eq(CourierCompany.valueOf(value)) : null;
    }

    /**
     * 주어진 값이 null이 아니고 비어있지 않으면 ShipmentStatus 필드에 대한 equal 표현식을 반환.
     *
     * @param path  엔티티의 필드에 대한 경로.
     * @param value 비교할 값.
     * @return equal 쿼리를 위한 BooleanExpression 혹은 null.
     */
    public static BooleanExpression eqShipmentStatus(EnumPath<ShipmentStatus> path, String value) {
        return (value != null && !value.equals("")) ? path.eq(ShipmentStatus.valueOf(value)) : null;
    }

    /**
     * 주어진 값이 null이 아니고 비어있지 않으면 NoticeType 필드에 대한 equal 표현식을 반환.
     *
     * @param path  엔티티의 필드에 대한 경로.
     * @param value 비교할 값.
     * @return equal 쿼리를 위한 BooleanExpression 혹은 null.
     */
    public static BooleanExpression eqNoticeType(EnumPath<NoticeType> path, String value) {
        return (value != null && !value.isEmpty()) ? path.eq(NoticeType.valueOf(value)) : null;
    }

    /**
     * 주어진 값이 null이 아니고 비어있지 않으면 NoticeStatus 필드에 대한 equal 표현식을 반환.
     *
     * @param path  엔티티의 필드에 대한 경로.
     * @param value 비교할 값.
     * @return equal 쿼리를 위한 BooleanExpression 혹은 null.
     */
    public static BooleanExpression eqNoticeStatus(EnumPath<NoticeStatus> path, String value) {
        return (value != null && !value.isEmpty()) ? path.eq(NoticeStatus.valueOf(value)) : null;
    }

    /**
     * 주어진 값이 null이 아니고 비어있지 않으면 ContactType 필드에 대한 equal 표현식을 반환.
     *
     * @param path  엔티티의 필드에 대한 경로.
     * @param value 비교할 값.
     * @return equal 쿼리를 위한 BooleanExpression 혹은 null.
     */
    public static BooleanExpression eqContactType(EnumPath<ContactType> path, String value) {
        return (value != null && !value.equals("")) ? path.eq(ContactType.valueOf(value)) : null;
    }

    /**
     * 주어진 값이 null이 아니고 비어있지 않으면 ContactStatus 필드에 대한 equal 표현식을 반환.
     *
     * @param path  엔티티의 필드에 대한 경로.
     * @param value 비교할 값.
     * @return equal 쿼리를 위한 BooleanExpression 혹은 null.
     */
    public static BooleanExpression eqContactStatus(EnumPath<ContactStatus> path, String value) {
        return (value != null && !value.equals("")) ? path.eq(ContactStatus.valueOf(value)) : null;
    }

    /**
     * 주어진 값이 null이 아니고 비어있지 않으면 ProductStatus 필드에 대한 equal 표현식을 반환.
     *
     * @param path  엔티티의 필드에 대한 경로.
     * @param value 비교할 값.
     * @return equal 쿼리를 위한 BooleanExpression 혹은 null.
     */
    public static BooleanExpression eqProductStatus(EnumPath<ProductStatus> path, String value) {
        return (value != null && !value.isEmpty()) ? path.eq(ProductStatus.valueOf(value)) : null;
    }

    /**
     * 주어진 값이 null이 아니고 비어있지 않으면 OrderStatus 필드에 대한 equal 표현식을 반환.
     *
     * @param path  엔티티의 필드에 대한 경로.
     * @param value 비교할 값.
     * @return equal 쿼리를 위한 BooleanExpression 혹은 null.
     */
    public static BooleanExpression eqOrderStatus(EnumPath<OrderStatus> path, String value) {
        return (value != null && !value.isEmpty()) ? path.eq(OrderStatus.valueOf(value)) : null;
    }

    /**
     * 주어진 값이 null이 아니고 비어있지 않으면 CouponType 필드에 대한 equal 표현식을 반환.
     *
     * @param path  엔티티의 필드에 대한 경로.
     * @param value 비교할 값.
     * @return equal 쿼리를 위한 BooleanExpression 혹은 null.
     */
    public static BooleanExpression eqCouponType(EnumPath<CouponType> path, String value) {
        return (value != null && !value.isEmpty()) ? path.eq(CouponType.valueOf(value)) : null;
    }

    /**
     * 주어진 값이 null이 아니고 비어있지 않으면 CouponDiscountType 필드에 대한 equal 표현식을 반환.
     *
     * @param path  엔티티의 필드에 대한 경로.
     * @param value 비교할 값.
     * @return equal 쿼리를 위한 BooleanExpression 혹은 null.
     */
    public static BooleanExpression eqCouponDiscountType(EnumPath<CouponDiscountType> path, String value) {
        return (value != null && !value.isEmpty()) ? path.eq(CouponDiscountType.valueOf(value)) : null;
    }

    /**
     * 주어진 값이 null이 아니고 비어있지 않으면 CouponStatus 필드에 대한 equal 표현식을 반환.
     *
     * @param path  엔티티의 필드에 대한 경로.
     * @param value 비교할 값.
     * @return equal 쿼리를 위한 BooleanExpression 혹은 null.
     */
    public static BooleanExpression eqCouponStatus(EnumPath<CouponStatus> path, String value) {
        return (value != null && !value.isEmpty()) ? path.eq(CouponStatus.valueOf(value)) : null;
    }

    /**
     * 시작 및 종료 날짜가 null이 아닐 때 날짜 범위에 대한 between 표현식을 반환.
     *
     * @param path      엔티티의 필드에 대한 경로.
     * @param startDate 시작 날짜.
     * @param endDate   종료 날짜.
     * @return 날짜 범위 쿼리를 위한 BooleanExpression 혹은 null.
     */
    public static BooleanExpression betweenDate(DateTimePath<LocalDateTime> path, String startDate, String endDate) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDateTime ldtStartDate = (startDate != null && !startDate.isEmpty()) ? LocalDate.parse(startDate, formatter).atStartOfDay() : null;
        LocalDateTime ldtEndDate = (endDate != null && !endDate.isEmpty()) ? LocalDate.parse(endDate, formatter).atTime(23, 59, 59) : null;

        return (ldtStartDate != null || ldtEndDate != null) ? path.between(ldtStartDate, ldtEndDate) : null;
    }
}
