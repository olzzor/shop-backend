package com.shop.module.notice.mapper;

import com.shop.module.notice.dto.NoticeDto;
import com.shop.module.notice.entity.Notice;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class NoticeMapper {

    public NoticeDto mapToDto(Notice notice) {
        return NoticeDto.builder()
                .id(notice.getId())
                .type(notice.getType())
                .title(notice.getTitle())
                .content(notice.getContent())
                .status(notice.getStatus())
//                .isMainImage(notice.isMainImage())
                .isSliderImage(notice.isSliderImage())
                .isModalImage(notice.isModalImage())
                .regDate(notice.getRegDate())
                .modDate(notice.getModDate())
                .build();
    }

    public List<NoticeDto> mapToDtoList(List<Notice> noticeList) {
        return noticeList.stream().map(this::mapToDto).collect(Collectors.toList());
    }
}