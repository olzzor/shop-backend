package com.shop.module.product.dto;

import lombok.Getter;

@Getter
public class ProductSizeUpsertRequest {
    private Long productId; // 추가 (상품 목록 페이지에서도 사이즈 변경이 가능하게 하기 위함)
    private Long id;
    private String size;
    private int quantity; // 페이지 로드 시점의 상품 사이즈 재고
    /** adjustmentQuantity
     * 서버의 재고 업데이트 처리 시, 프론트에서 입력된 수정 전후의 수량차를 이용하여 갱신하기 위함.
     * 업데이트 시 실시간으로 상품이 판매되어 수량이 변경된 경우에 대비하기 위함. */
    private int adjustmentQuantity; // 페이지 로드 이후 관리자에 의해 입력된 상품 사이즈 재고
}
