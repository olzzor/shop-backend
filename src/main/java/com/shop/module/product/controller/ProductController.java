package com.shop.module.product.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.shop.common.exception.UnauthorizedException;
import com.shop.common.service.FileUploadService;
import com.shop.common.util.JsonUtils;
import com.shop.module.category.entity.Category;
import com.shop.module.category.service.CategoryService;
import com.shop.module.product.service.*;
import com.shop.module.wishlist.dto.WishlistDto;
import com.shop.module.wishlist.entity.Wishlist;
import com.shop.module.wishlist.repository.WishlistRepository;
import com.shop.module.wishlist.service.WishlistService;
import com.shop.module.product.dto.*;
import com.shop.module.product.entity.Product;
import com.shop.module.user.service.JwtService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/product")
public class ProductController {

    private final JwtService jwtService;
    private final ProductService productService;
    private final ProductImageService productImageService;
    private final ProductSizeService productSizeService;
    private final ProductDetailService productDetailService;
    private final LogProductViewService logProductViewService;
    private final CategoryService categoryService;
    private final WishlistService wishlistService;
    private final FileUploadService fileUploadService;

    private final WishlistRepository wishlistRepository;

    /**
     * 상품 단일 취득
     */
    @GetMapping("/{id}")
    public ProductDto getProduct(@PathVariable("id") Long productId) {

        Product product = productService.retrieveById(productId);
        return productService.getDtoWithCategoryAndSizeAndImageAndDetail(product);
    }

    /**
     * 판매 상품 전체 취득
     */
//    @GetMapping("/all")
//    public List<ProductDto> getProductAll() {
//
//        List<Product> productList = productService.getProductsOnSale();
//        return productService.getDtoListWithMainImage(productList);
//    }
    @GetMapping("/all")
    public ResponseEntity getProductAll(Pageable pageable) {

        Page<Product> productPage = productService.getProducts(pageable);

        ProductListResponse productListResponse = ProductListResponse.builder()
                .products(productService.getDtoListWithMainImage(productPage.getContent()))
                .totalPages(productPage.getTotalPages())
                .build();

        return new ResponseEntity<>(productListResponse, HttpStatus.OK);
    }

    /**
     * 카테고리별 판매 상품 전체 취득
     */
    @GetMapping("/category/{slug}")
    public ResponseEntity getProductListCategory(@PathVariable("slug") String categorySlug,
                                                 Pageable pageable) {

        Optional<Category> categoryOpt = categoryService.retrieveBySlug(categorySlug);
        Page<Product> productPage;

        if (categoryOpt.isPresent()) { // 입력된 카테고리 슬러그의 카테고리가 존재하는 경우
            Category category = categoryOpt.get();

            if (category.getCodeRef() == null) { // 카테고리가 메인 카테고리인 경우
                productPage = productService.getProductsByMainCategory(category.getCode(), pageable);

            } else { // 카테고리가 서브 카테고리인 경우
                productPage = productService.getProductsBySubCategory(category.getCode(), pageable);
            }

        } else { // 카테고리가 존재하지 않을 경우, 기본 상품 페이지를 반환
            productPage = productService.getProducts(pageable);
        }

        ProductListResponse productListResponse = ProductListResponse.builder()
                .products(productService.getDtoListWithMainImage(productPage.getContent()))
                .totalPages(productPage.getTotalPages())
                .build();

        return new ResponseEntity<>(productListResponse, HttpStatus.OK);
    }

    /**
     * 상품 검색
     */
    @GetMapping("/search/{query}")
    public ResponseEntity searchProductQuery(@PathVariable("query") String query,
                                             Pageable pageable) {

        Page<Product> productPage = productService.searchProducts(query, pageable);

        ProductListResponse productListResponse = ProductListResponse.builder()
                .products(productService.getDtoListWithMainImage(productPage.getContent()))
                .totalPages(productPage.getTotalPages())
                .build();

        return new ResponseEntity<>(productListResponse, HttpStatus.OK);
    }

//    @GetMapping("/search/{query}")
//    public List<ProductDto> searchProductQuery(@PathVariable("query") String query) {
//
//        List<Product> productList = productService.searchOnSaleProducts(query);
//        return productService.getDtoListWithMainImage(productList);
//    }

    /**
     * 상품 전체 취득
     */
    @GetMapping("/list")
    public ResponseEntity getProductList(@CookieValue(value = "token", required = false) String accessToken,
                                         @CookieValue(value = "refresh_token", required = false) String refreshToken,
                                         HttpServletResponse res,
                                         Pageable pageable) {

        String token = jwtService.getToken(accessToken, refreshToken, res);

        if (token != null) {

            // 등록 날짜를 기준으로 내림차순 정렬을 적용한 PageRequest 객체를 생성
            Pageable sortedPageable = PageRequest.of(
                    pageable.getPageNumber(),
                    pageable.getPageSize(),
                    Sort.by("regDate").descending()
            );

            Page<Product> productPage = productService.retrieveAllPaginated(sortedPageable);
            List<ProductDto> productDtoList = productService.getDtoListWithCategoryAndSizeMainImage(productPage.getContent());

            ProductListResponse productListResponse = ProductListResponse.builder()
                    .products(productDtoList)
                    .totalPages(productPage.getTotalPages())
                    .build();

            return new ResponseEntity<>(productListResponse, HttpStatus.OK);

        } else { // 토큰이 유효하지 않은 경우
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
    }

    /**
     * 조건으로 상품 검색
     */
    @PostMapping("/search")
    public ResponseEntity searchProductList(@RequestBody ProductListSearchRequest productListSearchRequest,
                                            @CookieValue(value = "token", required = false) String accessToken,
                                            @CookieValue(value = "refresh_token", required = false) String refreshToken,
                                            HttpServletResponse res,
                                            Pageable pageable) {

        String token = jwtService.getToken(accessToken, refreshToken, res);

        if (token != null) {
            Page<Product> productPage = productService.searchProductList(productListSearchRequest, pageable);
            List<ProductDto> productDtoList = productService.getDtoListWithCategoryAndSizeMainImage(productPage.getContent());

            ProductListResponse productListResponse = ProductListResponse.builder()
                    .products(productDtoList)
                    .totalPages(productPage.getTotalPages())
                    .build();

            return new ResponseEntity<>(productListResponse, HttpStatus.OK);

        } else { // 토큰이 유효하지 않은 경우
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
    }

    /**
     * 조건으로 상품 검색
     */
    @PostMapping("/search-all/excluding-recommended")
    public ResponseEntity searchAllProductListExcludingRecommended(@CookieValue(value = "token", required = false) String accessToken,
                                                                   @CookieValue(value = "refresh_token", required = false) String refreshToken,
                                                                   HttpServletResponse res,
                                                                   Pageable pageable) {

        String token = jwtService.getToken(accessToken, refreshToken, res);

        if (token == null) {
            throw new UnauthorizedException("tokenInvalid", "유효하지 않은 토큰입니다.");
        }

        Page<Product> productPage = productService.searchAllProductListExcludingRecommended(pageable);
        List<ProductDto> productDtoList = productService.getDtoListWithCategoryAndSizeMainImage(productPage.getContent());

        ProductListResponse productListResponse = ProductListResponse.builder()
                .products(productDtoList)
                .totalPages(productPage.getTotalPages())
                .build();

        return new ResponseEntity<>(productListResponse, HttpStatus.OK);
    }

    /**
     * 조건으로 상품 검색
     */
    @PostMapping("/search/excluding-recommended")
    public ResponseEntity searchProductListExcludingRecommended(@RequestBody ProductListSearchRequest productListSearchRequest,
                                                                @CookieValue(value = "token", required = false) String accessToken,
                                                                @CookieValue(value = "refresh_token", required = false) String refreshToken,
                                                                HttpServletResponse res,
                                                                Pageable pageable) {

        String token = jwtService.getToken(accessToken, refreshToken, res);

        if (token == null) {
            throw new UnauthorizedException("tokenInvalid", "유효하지 않은 토큰입니다.");
        }

        Page<Product> productPage = productService.searchProductListExcludingRecommended(productListSearchRequest, pageable);
        List<ProductDto> productDtoList = productService.getDtoListWithCategoryAndSizeMainImage(productPage.getContent());

        ProductListResponse productListResponse = ProductListResponse.builder()
                .products(productDtoList)
                .totalPages(productPage.getTotalPages())
                .build();

        return new ResponseEntity<>(productListResponse, HttpStatus.OK);
    }

    /**
     * 상품 단일 취득
     */
    @GetMapping("/detail/{id}")
    public ResponseEntity getProductDetail(@PathVariable("id") Long productId,
                                           @CookieValue(value = "token", required = false) String accessToken,
                                           @CookieValue(value = "refresh_token", required = false) String refreshToken,
                                           HttpServletResponse res) {

        String token = jwtService.getToken(accessToken, refreshToken, res);

        if (token != null) {
            Product product = productService.retrieveById(productId);
            ProductDto productDto = productService.getDtoWithCategoryAndSizeAndImageAndDetail(product);

            return new ResponseEntity<>(productDto, HttpStatus.OK);

        } else { // 토큰이 유효하지 않은 경우
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
    }

    /**
     * 상품 단일 취득
     */
    @GetMapping("/with-user-wishlists/{productId}")
    public ProductDto getProductWithUserWishlists(@PathVariable("productId") Long productId,
                                                  @CookieValue(value = "token", required = false) String accessToken,
                                                  @CookieValue(value = "refresh_token", required = false) String refreshToken,
                                                  HttpServletResponse res) {

        String token = jwtService.getToken(accessToken, refreshToken, res);

        if (token != null) {
            Product product = productService.retrieveById(productId);
            ProductDto productDto = productService.convertToDto(product);
            List<ProductSizeDto> productSizeDtoList = productSizeService.convertToDtoList(product.getProductSizes());

            for (ProductSizeDto productSizeDto : productSizeDtoList) {
                Optional<Wishlist> wishlistOptional = wishlistRepository.findByUser_IdAndProduct_IdAndProductSize_Id(jwtService.getId(token), productId, productSizeDto.getId());

                if (wishlistOptional.isPresent()) {
                    List<WishlistDto> wishlistDtoList = new ArrayList<>();
                    wishlistDtoList.add(wishlistService.convertToDto(wishlistOptional.get()));
                    productSizeDto.setWishlists(wishlistDtoList);
                }
            }

            productDto.setProductSizes(productSizeDtoList);

            return productDto;
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
    }

    @Transactional
    @PostMapping("/register")
    public ResponseEntity registerProduct(@RequestParam("product") String productJson,
                                          @RequestParam("detail") String detailJson,
                                          @RequestParam("sizes") String sizesJson,
                                          @RequestPart("files") List<MultipartFile> files,
                                          @CookieValue(value = "token", required = false) String accessToken,
                                          @CookieValue(value = "refresh_token", required = false) String refreshToken,
                                          HttpServletResponse res) {

        String token = jwtService.getToken(accessToken, refreshToken, res);

        if (token == null) {
            throw new UnauthorizedException("tokenInvalid", "유효하지 않은 토큰입니다.");
        }

        ProductUpsertRequest productUpsertRequest = Optional.ofNullable(JsonUtils.fromJson(productJson, ProductUpsertRequest.class))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Failed to parse input data"));
        ProductDetailUpsertRequest productDetailUpsertRequest = Optional.ofNullable(JsonUtils.fromJson(detailJson, ProductDetailUpsertRequest.class))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Failed to parse input data"));

//            TypeReference<List<ProductSizeUpsertRequest>> typeRef = new TypeReference<List<ProductSizeUpsertRequest>>() {};
        TypeReference<List<ProductSizeUpsertRequest>> typeRef = new TypeReference<>() {
        };
        List<ProductSizeUpsertRequest> productSizeUpsertRequestList = JsonUtils.fromJson(sizesJson, typeRef);

        productService.checkInput(productUpsertRequest);
        productDetailService.checkInput(productDetailUpsertRequest);
        productSizeService.checkInputs(productSizeUpsertRequestList);
        fileUploadService.checkInputFiles(files);

        Long userId = jwtService.getId(token);
        // 입력 데이터 Product 테이블에 데이터 삽입
        Product product = productService.insertProduct(userId, productUpsertRequest);
        // 입력 데이터 ProductDetail 테이블에 데이터 삽입
        productDetailService.insertProductDetail(product, productDetailUpsertRequest);
        // 입력 데이터 ProductSize 테이블에 데이터 삽입
        productSizeService.insertProductSizes(product, productSizeUpsertRequestList);
        // 입력 데이터 ProductImage 테이블에 데이터 삽입 & File Image 업로드
        productImageService.saveProductImages(userId, product, files);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Transactional
    @PostMapping("/update/single")
    public ResponseEntity<?> updateProduct(@RequestParam("product") String productJson,
                                           @RequestParam("detail") String detailJson,
                                           @RequestParam("sizes") String sizesJson,
                                           @RequestPart(value = "files", required = false) List<MultipartFile> files,
                                           @CookieValue(value = "token", required = false) String accessToken,
                                           @CookieValue(value = "refresh_token", required = false) String refreshToken,
                                           HttpServletResponse res) throws IOException {

        try {
            String token = jwtService.getToken(accessToken, refreshToken, res);

            if (token != null) {

                ProductUpsertRequest productUpsertRequest = Optional.ofNullable(JsonUtils.fromJson(productJson, ProductUpsertRequest.class))
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Failed to parse input data"));
                ProductDetailUpsertRequest productDetailUpsertRequest = Optional.ofNullable(JsonUtils.fromJson(detailJson, ProductDetailUpsertRequest.class))
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Failed to parse input data"));

                TypeReference<List<ProductSizeUpsertRequest>> typeRef = new TypeReference<>() {
                };
                List<ProductSizeUpsertRequest> productSizeUpsertRequestList = JsonUtils.fromJson(sizesJson, typeRef);

                // 기존 상품 정보 취득
                Product product = productService.retrieveById(productUpsertRequest.getId());

                // 상품 상세 DB 갱신
                productDetailService.updateProductDetail(productDetailUpsertRequest);
                // 상품 사이즈 DB 갱신
                productSizeService.updateProductSizes(productSizeUpsertRequestList);
                // 상품 이미지 파일의 업로드 및 DB 갱신
                productImageService.updateProductImages(jwtService.getId(token), product, productUpsertRequest, files);
                // 상품 DB 갱신
                productService.updateProduct(product, productUpsertRequest);

                return ResponseEntity.ok().build();

            } else { // 토큰이 유효하지 않은 경우
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Update failed due to server error.");
        }
    }

    @Transactional
    @PostMapping("/update/multiple")
    public ResponseEntity<?> updateProducts(@RequestParam("productList") String productListJson,
                                            @CookieValue(value = "token", required = false) String accessToken,
                                            @CookieValue(value = "refresh_token", required = false) String refreshToken,
                                            HttpServletResponse res) throws IOException {

        try {
            String token = jwtService.getToken(accessToken, refreshToken, res);

            if (token != null) {

                TypeReference<List<ProductUpsertRequest>> typeRef = new TypeReference<>() {
                };
                List<ProductUpsertRequest> productUpdReqList = JsonUtils.fromJson(productListJson, typeRef);

                // 상품 DB 갱신
                productService.updateProducts(productUpdReqList);
                return ResponseEntity.ok().build();

            } else { // 토큰이 유효하지 않은 경우
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Update failed due to server error.");
        }
    }

    /**
     * 상품 조회 로그 등록
     */
    @PostMapping("/record/view-log/{productId}")
    public ResponseEntity<?> recordViewLog(@PathVariable("productId") Long productId,
                                           @CookieValue(value = "token", required = false) String accessToken,
                                           @CookieValue(value = "refresh_token", required = false) String refreshToken,
                                           HttpServletResponse res) {

        String token = jwtService.getToken(accessToken, refreshToken, res);
        logProductViewService.createProductViewLog(token != null ? jwtService.getId(token) : null, productId);

        return new ResponseEntity<>(HttpStatus.OK);
    }

//    @GetMapping("/download-images/{productId}")
//    public ResponseEntity<?> downloadImages(@PathVariable Long productId,
//                                            @CookieValue(value = "token", required = false) String accessToken,
//                                            @CookieValue(value = "refresh_token", required = false) String refreshToken,
//                                            HttpServletResponse res) {
//
//        String token = jwtService.getToken(accessToken, refreshToken, res);
//
//        if (token == null) {
//            throw new UnauthorizedException("tokenInvalid", "유효하지 않은 토큰입니다.");
//        }
//
//        // 해당 상품 ID에 대한 이미지 정보 조회
//        List<ProductImage> productImageList = productImageService.retrieveByProductId(productId);
//
//        // 각 이미지에 대해 다운로드 URL 생성
//        List<String> downloadUrls = productImageList.stream()
//                .map(image -> s3UploadService.downloadImage(image.getFileKey()).getBody().getURL().toString())
//                .collect(Collectors.toList());
//
//        // 생성된 다운로드 URL 목록을 클라이언트에 반환
//        return ResponseEntity.ok(downloadUrls);
//    }
}
