package com.shop.module.coupon.service;

import com.shop.common.exception.NotFoundException;
import com.shop.common.exception.ValidationException;
import com.shop.common.util.DateUtils;
import com.shop.module.category.dto.CategoryDto;
import com.shop.module.category.entity.Category;
import com.shop.module.category.mapper.CategoryMapper;
import com.shop.module.coupon.dto.CouponDto;
import com.shop.module.coupon.dto.CouponEligibilityRequest;
import com.shop.module.coupon.dto.CouponListSearchRequest;
import com.shop.module.coupon.dto.CouponUpdateRequest;
import com.shop.module.coupon.entity.Coupon;
import com.shop.module.coupon.mapper.CouponMapper;
import com.shop.module.coupon.repository.CouponRepository;
import com.shop.module.product.dto.ProductDto;
import com.shop.module.product.entity.Product;
import com.shop.module.product.mapper.ProductMapper;
import com.shop.module.user.dto.UserDto;
import com.shop.module.user.entity.User;
import com.shop.module.user.mapper.UserMapper;
import io.micrometer.common.util.StringUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CouponService {

    private final CouponRepository couponRepository;
    private final CouponMapper couponMapper;
    private final CategoryMapper categoryMapper;
    private final ProductMapper productMapper;
    private final UserMapper userMapper;

    private static final int MAX_CODE_LENGTH = 50;
    private static final int MAX_NAME_LENGTH = 50;
    private static final int MAX_DETAIL_LENGTH = 2000;
    private static final String CODE_PATTERN = "^[A-Za-z0-9_]+$";


    public Coupon retrieveById(Long id) {
        return couponRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Coupon not found"));
    }

    public Page<Coupon> retrieveAllPaged(Pageable pageable) {
        return couponRepository.findAll(pageable);
    }

    public List<Coupon> retrieveAllForUser(Long userId) {
        return couponRepository.findAllAvailableCouponsByUser(userId);
    }

    public Page<Coupon> searchAllPaginated(CouponListSearchRequest couponListSearchRequest, Pageable pageable) {
        return couponRepository.findByCondition(couponListSearchRequest, pageable);
    }

    public List<Coupon> getCouponListAvailable(Long userId, CouponEligibilityRequest couponEligibilityRequest) {
        Long categoryId = couponEligibilityRequest.getCategoryId();
        Long productId = couponEligibilityRequest.getProductId();

        return couponRepository.retrieveApplicableCoupons(userId, categoryId, productId);
    }

    public CouponDto convertToDto(Coupon coupon) {
        return couponMapper.mapToDto(coupon);
    }

    public List<CouponDto> convertToDtoList(List<Coupon> couponList) {
        return couponList.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    public CouponDto getCouponDtoDetail(Coupon coupon) {
        CouponDto couponDto = couponMapper.mapToDto(coupon);

        List<CategoryDto> categoryDtoList = coupon.getCouponCategories().stream()
                .map(couponCategory -> {
                    Category category = couponCategory.getCategory();
                    return categoryMapper.mapToDto(category);
                }).collect(Collectors.toList());

        List<ProductDto> productDtoList = coupon.getCouponProducts().stream()
                .map(couponProduct -> {
                    Product product = couponProduct.getProduct();
                    return productMapper.mapToDto(product);
                }).collect(Collectors.toList());

        List<UserDto> userDtoList = coupon.getCouponUsers().stream()
                .map(couponUser -> {
                    User user = couponUser.getUser();
                    return userMapper.mapToDto(user);
                }).collect(Collectors.toList());

        couponDto.setCategories(categoryDtoList);
        couponDto.setProducts(productDtoList);
        couponDto.setUsers(userDtoList);

        return couponDto;
    }

    public List<CouponDto> getCouponDtoListDetail(List<Coupon> couponList) {
        return couponList.stream().map(this::getCouponDtoDetail).collect(Collectors.toList());
    }

    public void checkInput(CouponUpdateRequest cuReq) {

        // type 체크
        if (cuReq.getType() == null) {
            throw new ValidationException("typeMissing", "유형을 선택해주세요.");
        }

        // code 체크
        if (StringUtils.isBlank(cuReq.getCode())) {
            throw new ValidationException("codeMissing", "코드를 입력해주세요.");
        } else if (cuReq.getCode().trim().length() > MAX_CODE_LENGTH) {
            throw new ValidationException("codeTooLong", "코드는 50자 이하로 입력해주세요.");
        } else if (!cuReq.getCode().matches(CODE_PATTERN)) {
            throw new ValidationException("codeInvalidFormat", "코드가 유효하지 않습니다.");
        }

        // name 체크
        if (StringUtils.isBlank(cuReq.getName())) {
            throw new ValidationException("nameMissing", "이름을 입력해주세요.");
        } else if (cuReq.getName().trim().length() > MAX_NAME_LENGTH) {
            throw new ValidationException("nameTooLong", "이름은 50자 이하로 입력해주세요.");
        }

        // detail 체크
        if (StringUtils.isBlank(cuReq.getDetail())) {
            throw new ValidationException("detailMissing", "설명을 입력해주세요.");
        } else if (cuReq.getDetail().trim().length() > MAX_DETAIL_LENGTH) {
            throw new ValidationException("nameTooLong", "설명은 2000자 이하로 입력해주세요.");
        }

        // minAmount 체크
        if (cuReq.getMinAmount() < 0) {
            throw new ValidationException("minAmountInvalid", "최소 금액을 올바르게 입력해주세요.");
        }

        // discountType 체크
        if (cuReq.getDiscountType() == null) {
            throw new ValidationException("discountTypeMissing", "할인 유형을 선택해주세요.");
        }

        // discountValue 체크
        if (cuReq.getDiscountValue() < 0) {
            throw new ValidationException("discountValueInvalid", "할인 금액 및 할인율을 올바르게 입력해주세요.");
        }

        // validDate 체크
        if (StringUtils.isBlank(cuReq.getStartValidDate())) {
            throw new ValidationException("validDateMissing", "유효 기간의 시작 날짜를 입력해주세요.");
        } else if (StringUtils.isBlank(cuReq.getEndValidDate())) {
            throw new ValidationException("validDateMissing", "유효 기간의 종료 날짜를 입력해주세요.");
        } else if (LocalDate.parse(cuReq.getStartValidDate()).isAfter(LocalDate.parse(cuReq.getEndValidDate()))) {
            throw new ValidationException("validDateInvalid", "유효 기간의 종료 날짜는 시작 날짜보다 이후여야 합니다.");
        }

        // status 체크
        if (cuReq.getStatus() == null) {
            throw new ValidationException("statusMissing", "쿠폰 상태를 선택해주세요.");
        }
    }

    @Transactional
    public Coupon insertCoupon(CouponUpdateRequest couponUpdateRequest) {
        // 1. 입력된 유효 기간을 문자열에서 LocalDateTime 객체로 변환
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        String strStartValidDate = couponUpdateRequest.getStartValidDate();
        String strEndValidDate = couponUpdateRequest.getEndValidDate();

        LocalDateTime ldtStartValidDate = DateUtils.convertToLocalDateTime(strStartValidDate, formatter, false);
        LocalDateTime ldtEndValidDate = DateUtils.convertToLocalDateTime(strEndValidDate, formatter, true);

        // 2. Request 정보로 쿠폰 필드 업데이트
        Coupon coupon = Coupon.builder()
                .type(couponUpdateRequest.getType())
                .code(couponUpdateRequest.getCode())
                .name(couponUpdateRequest.getName())
                .detail(couponUpdateRequest.getDetail())
                .minAmount(couponUpdateRequest.getMinAmount())
                .discountType(couponUpdateRequest.getDiscountType())
                .discountValue(couponUpdateRequest.getDiscountValue())
                .startValidDate(ldtStartValidDate)
                .endValidDate(ldtEndValidDate)
                .status(couponUpdateRequest.getStatus())
                .build();

        // Save the Coupon entity
        return couponRepository.save(coupon);
    }

    @Transactional
    public Coupon updateCoupon(CouponUpdateRequest couponUpdateRequest) {
        // 1. ID로 기존 쿠폰을 데이터베이스에서 조회
        Coupon coupon = couponRepository.findById(couponUpdateRequest.getId())
                .orElseThrow(() -> new RuntimeException("Coupon not found"));

        // 2. 입력된 유효 기간을 문자열에서 LocalDateTime 객체로 변환
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        String strStartValidDate = couponUpdateRequest.getStartValidDate();
        String strEndValidDate = couponUpdateRequest.getEndValidDate();

        LocalDateTime ldtStartValidDate = DateUtils.convertToLocalDateTime(strStartValidDate, formatter, false);
        LocalDateTime ldtEndValidDate = DateUtils.convertToLocalDateTime(strEndValidDate, formatter, true);

        // 3. Request 정보로 쿠폰 필드 업데이트
        coupon.setType(couponUpdateRequest.getType());
        coupon.setCode(couponUpdateRequest.getCode());
        coupon.setName(couponUpdateRequest.getName());
        coupon.setDetail(couponUpdateRequest.getDetail());
        coupon.setMinAmount(couponUpdateRequest.getMinAmount());
        coupon.setDiscountType(couponUpdateRequest.getDiscountType());
        coupon.setDiscountValue(couponUpdateRequest.getDiscountValue());
        coupon.setStartValidDate(ldtStartValidDate);
        coupon.setEndValidDate(ldtEndValidDate);
        coupon.setStatus(couponUpdateRequest.getStatus());

        return couponRepository.save(coupon);
    }

    /**
     * 주어진 쿠폰 정보 목록에 대해 업데이트를 수행
     * 변경이 감지된 경우에만 데이터베이스에 저장
     *
     * @param couponDtoList 업데이트할 쿠폰 정보의 DTO 목록
     */
    @Transactional
    public void updateCoupons(List<CouponDto> couponDtoList) {

        for (CouponDto couponDto : couponDtoList) {
            // ID를 이용해 쿠폰 정보 엔티티를 조회하여, 없을 경우 NotFoundException을 발생
            Coupon coupon = couponRepository.findById(couponDto.getId())
                    .orElseThrow(() -> new NotFoundException("couponNotFound", "쿠폰 정보를 찾을 수 없습니다."));

            // 변경 사항을 감지하여 엔티티를 저장
            if (updateCouponDetails(coupon, couponDto)) {
                couponRepository.save(coupon);
            }
        }
    }

    /**
     * 개별 쿠폰 정보 엔티티에 대한 상세 업데이트를 수행
     * 변경된 필드가 있을 경우에만 업데이트를 진행
     *
     * @param coupon    업데이트할 쿠폰 정보 엔티티
     * @param couponDto 업데이트에 사용될 DTO
     * @return 변경 사항이 있었으면 true를, 아니면 false를 반환
     */
    private boolean updateCouponDetails(Coupon coupon, CouponDto couponDto) {
        boolean isModified = false;

        // 쿠폰 코드, 이름의 변경 사항을 검사하고 업데이트
        isModified |= updateIfDifferent(coupon.getCode(), couponDto.getCode(), coupon::setCode);
        isModified |= updateIfDifferent(coupon.getName(), couponDto.getName(), coupon::setName);

        // 쿠폰 구분 변경이 있을 경우 업데이트
        if (couponDto.getType() != null && coupon.getType() != couponDto.getType()) {
            coupon.setType(couponDto.getType());
            isModified = true; // 상태가 변경되었다면 수정됨으로 표시
        }

        // 쿠폰 상태 변경이 있을 경우 업데이트
        if (couponDto.getStatus() != null && coupon.getStatus() != couponDto.getStatus()) {
            coupon.setStatus(couponDto.getStatus());
            isModified = true; // 상태가 변경되었다면 수정됨으로 표시
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

}