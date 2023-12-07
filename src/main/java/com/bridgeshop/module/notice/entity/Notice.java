package com.bridgeshop.module.notice.entity;

import com.bridgeshop.common.entity.BaseTimeEntity;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "notices")
public class Notice extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private NoticeType type;

    @Column(nullable = false, length = 100)
    private String title;

    @Column(nullable = false, length = 10000)
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private NoticeStatus status;

//    @Column(nullable = false, columnDefinition = "boolean default true")
//    private boolean isMainImage;

    @Column(nullable = false, columnDefinition = "boolean default false")
    private boolean isSliderImage = false;

    @Column(nullable = false, columnDefinition = "boolean default false")
    private boolean isModalImage = false;

    @OneToMany(mappedBy = "notice")
    @JsonBackReference
    private List<NoticeImage> noticeImages = new ArrayList<>();
}
