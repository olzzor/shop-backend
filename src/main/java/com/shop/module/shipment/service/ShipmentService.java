package com.shop.module.shipment.service;

import com.shop.common.exception.NotFoundException;
import com.shop.common.exception.ValidationException;
import com.shop.common.service.SendMailService;
import com.shop.module.order.dto.OrderDetailDto;
import com.shop.module.order.dto.OrderDto;
import com.shop.module.order.dto.OrderPaymentRequest;
import com.shop.module.order.mapper.OrderDetailMapper;
import com.shop.module.order.mapper.OrderMapper;
import com.shop.module.product.dto.ProductDto;
import com.shop.module.product.dto.ProductImageDto;
import com.shop.module.product.dto.ProductSizeDto;
import com.shop.module.product.mapper.ProductImageMapper;
import com.shop.module.product.mapper.ProductMapper;
import com.shop.module.product.mapper.ProductSizeMapper;
import com.shop.module.shipment.dto.ShipmentDto;
import com.shop.module.shipment.dto.ShipmentListSearchRequest;
import com.shop.module.shipment.entity.Shipment;
import com.shop.module.shipment.entity.ShipmentStatus;
import com.shop.module.shipment.mapper.ShipmentMapper;
import com.shop.module.shipment.repository.ShipmentRepository;
import com.siot.IamportRestClient.response.Payment;
import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ShipmentService {

    private final SendMailService sendMailService;

    private final ShipmentRepository shipmentRepository;

    private final ShipmentMapper shipmentMapper;
    private final OrderMapper orderMapper;
    private final OrderDetailMapper orderDetailMapper;
    private final ProductMapper productMapper;
    private final ProductSizeMapper productSizeMapper;
    private final ProductImageMapper productImageMapper;

    public ShipmentDto convertToDto(Shipment shipment) {
        return shipmentMapper.mapToDto(shipment);
    }

    public List<ShipmentDto> convertToDtoList(List<Shipment> shipmentList) {
        return shipmentMapper.mapToDtoList(shipmentList);
    }

    public Shipment retrieveById(Long id) {
        return shipmentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("shipmentNotFound", "배송 정보를 찾을 수 없습니다."));
    }

    public ShipmentDto getDto(Shipment shipment) {
        ShipmentDto shipmentDto = shipmentMapper.mapToDto(shipment);

        List<OrderDetailDto> orderDetailDtos = shipment.getOrderDetails().stream()
                .map(orderDetail -> {
                    OrderDetailDto orderDetailDto = orderDetailMapper.mapToDto(orderDetail);

                    OrderDto orderDto = orderMapper.mapToDto(orderDetail.getOrder());
                    orderDetailDto.setOrder(orderDto);

                    ProductSizeDto productSizeDto = productSizeMapper.mapToDto(orderDetail.getProductSize());
                    orderDetailDto.setProductSize(productSizeDto);

                    ProductDto productDto = productMapper.mapToDto(orderDetail.getProduct());
                    List<ProductImageDto> productImageDtoList = productImageMapper.mapToDtoList(orderDetail.getProduct().getProductImages());
                    productDto.setProductImages(productImageDtoList);
                    orderDetailDto.setProduct(productDto);

                    return orderDetailDto;
                })
                .collect(Collectors.toList());

        shipmentDto.setOrderDetails(orderDetailDtos);
        return shipmentDto;
    }

    public List<ShipmentDto> getDtoList(List<Shipment> shipmentList) {
        return shipmentList.stream()
                .map(this::getDto)
                .collect(Collectors.toList());
    }

    public Page<Shipment> getShipmentList(Pageable pageable) {
        return shipmentRepository.findDistinctShipmentByOrderDetailsOrderOrderNumberDescIdAsc(pageable);
    }

    public Page<Shipment> searchAllPaginated(ShipmentListSearchRequest shipmentListSearchRequest, Pageable pageable) {
        return shipmentRepository.findByCondition(shipmentListSearchRequest, pageable);
    }

    /**
     * 주어진 배송 정보에 대해 업데이트를 수행
     * 변경이 감지된 경우에만 데이터베이스에 저장
     *
     * @param shipment    업데이트할 배송 정보 엔티티
     * @param shipmentDto 업데이트에 사용될 DTO
     */
    @Transactional
    public void updateShipment(Shipment shipment, ShipmentDto shipmentDto) {

        ShipmentStatus ssBefore = shipment.getStatus(); // 변경 전 배송 상태
        ShipmentStatus ssAfter = shipmentDto.getStatus(); // 변경 후 배송 상태

        // 변경 사항을 감지하여 엔티티를 저장
        if (updateShipmentDetails(shipment, shipmentDto)) {
            shipmentRepository.save(shipment);

            // '발송 준비 중' 혹은 '발송 처리 중' -> '발송 완료' 업데이트의 경우, 발송 안내 메일 전송
            if ((ssBefore == ShipmentStatus.PREPARING || ssBefore == ShipmentStatus.PROCESSING)
                    && ssAfter == ShipmentStatus.SHIPPED) {
                sendMailService.sendShipmentMail(shipment);
            }
        }
    }

    /**
     * 주어진 배송 정보 목록에 대해 업데이트를 수행
     * 변경이 감지된 경우에만 데이터베이스에 저장
     *
     * @param shipmentDtoList 업데이트할 배송 정보의 DTO 목록
     */
    @Transactional
    public void updateShipments(List<ShipmentDto> shipmentDtoList) {

        for (ShipmentDto shipmentDto : shipmentDtoList) {
            // ID를 이용해 배송 정보 엔티티를 조회하여, 없을 경우 NotFoundException을 발생
            Shipment shipment = shipmentRepository.findById(shipmentDto.getId())
                    .orElseThrow(() -> new NotFoundException("shipmentNotFound", "배송 정보를 찾을 수 없습니다."));

            ShipmentStatus ssBefore = shipment.getStatus(); // 변경 전 배송 상태
            ShipmentStatus ssAfter = shipmentDto.getStatus(); // 변경 후 배송 상태

            // 변경 사항을 감지하여 엔티티를 저장
            if (updateShipmentDetails(shipment, shipmentDto)) {
                shipmentRepository.save(shipment);

                // '발송 준비 중' 혹은 '발송 처리 중' -> '발송 완료' 업데이트의 경우, 배송 안내 메일 전송
                if ((ssBefore == ShipmentStatus.PREPARING || ssBefore == ShipmentStatus.PROCESSING)
                        && ssAfter == ShipmentStatus.SHIPPED) {
                    sendMailService.sendShipmentMail(shipment);
                }
            }
        }
    }

    /**
     * 개별 배송 정보 엔티티에 대한 상세 업데이트를 수행
     * 변경된 필드가 있을 경우에만 업데이트를 진행
     *
     * @param shipment    업데이트할 배송 정보 엔티티
     * @param shipmentDto 업데이트에 사용될 DTO
     * @return 변경 사항이 있었으면 true를, 아니면 false를 반환
     */
    private boolean updateShipmentDetails(Shipment shipment, ShipmentDto shipmentDto) {
        boolean isModified = false;

        // 수령인, 연락처, 주소, 배송 업체, 송장 번호의 변경 사항을 검사하고 업데이트
        isModified |= updateIfDifferent(shipment.getRecipientName(), shipmentDto.getRecipientName(), shipment::setRecipientName);
        isModified |= updateIfDifferent(shipment.getRecipientPhone(), shipmentDto.getRecipientPhone(), shipment::setRecipientPhone);
        isModified |= updateIfDifferent(shipment.getShippingAddress(), shipmentDto.getShippingAddress(), shipment::setShippingAddress);
        isModified |= updateIfDifferent(shipment.getTrackingNumber(), shipmentDto.getTrackingNumber(), shipment::setTrackingNumber);

        // 택배사 변경이 있을 경우 업데이트
        if (shipmentDto.getCourierCompany() != null && shipment.getCourierCompany() != shipmentDto.getCourierCompany()) {
            shipment.setCourierCompany(shipmentDto.getCourierCompany());
            isModified = true; // 상태가 변경되었다면 수정됨으로 표시
        }

        ShipmentStatus ssBefore = shipment.getStatus(); // 변경 전 배송 상태
        ShipmentStatus ssAfter = shipmentDto.getStatus(); // 변경 후 배송 상태

        // 배송 상태 변경이 있을 경우 업데이트
        if (ssBefore != ssAfter) {
            shipment.setStatus(ssAfter);
            isModified = true; // 상태가 변경되었다면 수정됨으로 표시

            // '발송 준비 중' 혹은 '발송 처리 중' -> '발송 완료' 으로 변경되는 경우 필수 요건 검사
            // '발송 준비 중' 혹은 '발송 처리 중' -> '발송 완료' 업데이트의 경우, 택배사와 송장번호는 필수 입력 체크
            if ((ssBefore == ShipmentStatus.PREPARING || ssBefore == ShipmentStatus.PROCESSING)
                    && ssAfter == ShipmentStatus.SHIPPED) {

                // 택배사와 송장번호가 제공되지 않았다면 예외 발생
                if (shipmentDto.getCourierCompany() == null || shipmentDto.getTrackingNumber() == null) {
                    throw new ValidationException("courierCompanyOrTrackingNumberMissing",
                            "배송 상태를 '배송 중'으로 변경할 때는 택배사와 송장번호 입력이 필수입니다.");
                }
            }
        }

        // 변경된 사항이 있으면 true, 아니면 false를 반환
        return isModified;
    }

    /**
     * 현재 값과 새로운 값을 비교하여 다를 경우, 제공된 setter 함수를 사용해 값을 업데이트
     *
     * @param currentValue 현재 객체의 필드 값
     * @param newValue     업데이트 할 새로운 값
     * @param setter       현재 값을 업데이트할 setter 메소드 참조
     * @return 값이 변경되었으면 true를, 그렇지 않으면 false를 반환
     */
    private boolean updateIfDifferent(String currentValue, String newValue, Consumer<String> setter) {
        if (StringUtils.isNotBlank(newValue) && !newValue.trim().equals(currentValue)) {
            // 새로운 값이 다르다면 setter 메소드를 사용하여 현재 객체의 값을 업데이트
            setter.accept(newValue.trim());
            return true; // 변경이 있었으므로 true를 반환
        }
        return false; // 값이 변경되지 않았으므로 false를 반환
    }

    @Transactional
    public Shipment insertShipment(Payment payment) {

        Shipment shipment = Shipment.builder()
                .recipientName(payment.getBuyerName())
                .recipientPhone(payment.getBuyerTel())
                .shippingAddress(payment.getBuyerAddr())
                .courierCompany(null)  // TODO: 추후 변경 할 것
                .trackingNumber("")
                .status(ShipmentStatus.PREPARING)
                .build();

        return shipmentRepository.save(shipment);
    }

    @Transactional
    public Shipment insertShipmentForDirectDeposit(OrderPaymentRequest opReq) {

        Shipment shipment = Shipment.builder()
                .recipientName(opReq.getBuyerName())
                .recipientPhone(opReq.getBuyerTel())
                .shippingAddress(opReq.getBuyerAddr())
                .courierCompany(null)  // TODO: 추후 변경 할 것
                .trackingNumber("")
                .status(ShipmentStatus.PREPARING)
                .build();

        return shipmentRepository.save(shipment);
    }
}