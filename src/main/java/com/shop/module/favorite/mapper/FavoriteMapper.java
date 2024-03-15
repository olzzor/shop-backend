package com.shop.module.favorite.mapper;

import com.shop.module.favorite.entity.Favorite;
import com.shop.module.favorite.dto.FavoriteDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class FavoriteMapper {

    public FavoriteDto mapToDto(Favorite favorite) {
        return FavoriteDto.builder()
                .id(favorite.getId())
                .build();
    }

    public List<FavoriteDto> mapToDtoList(List<Favorite> favoriteList) {
        return favoriteList.stream().map(this::mapToDto).collect(Collectors.toList());
    }
}