package com.bridgeshop.module.shipment.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QShipment is a Querydsl query type for Shipment
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QShipment extends EntityPathBase<Shipment> {

    private static final long serialVersionUID = 898998001L;

    public static final QShipment shipment = new QShipment("shipment");

    public final com.bridgeshop.module.entity.QBaseTimeEntity _super = new com.bridgeshop.module.entity.QBaseTimeEntity(this);

    public final EnumPath<CourierCompany> courierCompany = createEnum("courierCompany", CourierCompany.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modDate = _super.modDate;

    public final ListPath<com.bridgeshop.module.order.entity.OrderDetail, com.bridgeshop.module.order.entity.QOrderDetail> orderDetails = this.<com.bridgeshop.module.order.entity.OrderDetail, com.bridgeshop.module.order.entity.QOrderDetail>createList("orderDetails", com.bridgeshop.module.order.entity.OrderDetail.class, com.bridgeshop.module.order.entity.QOrderDetail.class, PathInits.DIRECT2);

    public final StringPath recipientName = createString("recipientName");

    public final StringPath recipientPhone = createString("recipientPhone");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> regDate = _super.regDate;

    public final StringPath shippingAddress = createString("shippingAddress");

    public final EnumPath<ShipmentStatus> status = createEnum("status", ShipmentStatus.class);

    public final StringPath trackingNumber = createString("trackingNumber");

    public QShipment(String variable) {
        super(Shipment.class, forVariable(variable));
    }

    public QShipment(Path<? extends Shipment> path) {
        super(path.getType(), path.getMetadata());
    }

    public QShipment(PathMetadata metadata) {
        super(Shipment.class, metadata);
    }

}

