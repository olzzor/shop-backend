package com.bridgeshop.module.notice.dto;

import com.bridgeshop.module.notice.entity.NoticeImageType;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NoticeImageDto {
    private Long id;
    private NoticeImageType type;
    private NoticeDto notice;
    private String filePath;
    private String fileName;
}
