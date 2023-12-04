package com.bridgeshop.module.notice.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QNoticeImage is a Querydsl query type for NoticeImage
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QNoticeImage extends EntityPathBase<NoticeImage> {

    private static final long serialVersionUID = -2034821458L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QNoticeImage noticeImage = new QNoticeImage("noticeImage");

    public final com.bridgeshop.module.entity.QBaseTimeEntity _super = new com.bridgeshop.module.entity.QBaseTimeEntity(this);

    public final StringPath fileName = createString("fileName");

    public final StringPath filePath = createString("filePath");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modDate = _super.modDate;

    public final QNotice notice;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> regDate = _super.regDate;

    public final EnumPath<NoticeImageType> type = createEnum("type", NoticeImageType.class);

    public QNoticeImage(String variable) {
        this(NoticeImage.class, forVariable(variable), INITS);
    }

    public QNoticeImage(Path<? extends NoticeImage> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QNoticeImage(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QNoticeImage(PathMetadata metadata, PathInits inits) {
        this(NoticeImage.class, metadata, inits);
    }

    public QNoticeImage(Class<? extends NoticeImage> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.notice = inits.isInitialized("notice") ? new QNotice(forProperty("notice")) : null;
    }

}

