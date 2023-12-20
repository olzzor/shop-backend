package com.bridgeshop.module.notice.mapper;

import com.bridgeshop.module.notice.dto.NoticeImageDto;
import com.bridgeshop.module.notice.entity.NoticeImage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class NoticeImageMapper {

    public NoticeImageDto mapToDto(NoticeImage noticeImage) {
        return NoticeImageDto.builder()
                .id(noticeImage.getId())
                .type(noticeImage.getType())
                .fileUrl(noticeImage.getFileUrl())
                .fileKey(noticeImage.getFileKey())
                .build();
    }

    public List<NoticeImageDto> mapToDtoList(List<NoticeImage> noticeImageList) {
        return noticeImageList.stream().map(this::mapToDto).collect(Collectors.toList());
    }
}