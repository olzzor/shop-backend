package com.bridgeshop.module.notice.entity;

import com.bridgeshop.common.entity.BaseTimeEntity;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "notice_images")
public class NoticeImage extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JsonManagedReference
    @JoinColumn(nullable = false, name = "notice_id", referencedColumnName = "id")
    private Notice notice;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private NoticeImageType type;

    @Column(nullable = false, length = 100)
    private String filePath;

    @Column(nullable = false, length = 100)
    private String fileName;
}
