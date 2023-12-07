package com.bridgeshop.common.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@MappedSuperclass // 이 클래스는 테이블로 생성되지 않음
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@Table(name = "base_time_entity")
public class BaseTimeEntity {
    @CreatedDate
    @Column(name = "reg_date")
    private LocalDateTime regDate;

    @UpdateTimestamp
    @Column(name = "mod_date")
    private LocalDateTime modDate;
}