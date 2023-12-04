package com.bridgeshop.common.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@MappedSuperclass
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "base_time_entity")
public class BaseTimeEntity {
    @CreatedDate
    @Column(name = "reg_date")
    private LocalDateTime regDate;

    //    @LastModifiedDate
    @UpdateTimestamp
    @Column(name = "mod_date")
    private LocalDateTime modDate;
}