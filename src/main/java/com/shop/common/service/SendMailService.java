package com.shop.common.service;

import com.shop.common.dto.MailDto;
import com.shop.common.exception.SendMailException;
import com.shop.module.contact.dto.ContactDto;
import com.shop.module.contact.repository.ContactRepository;
import com.shop.module.order.entity.Order;
import com.shop.module.order.entity.OrderDetail;
import com.shop.module.shipment.entity.CourierCompany;
import com.shop.module.shipment.entity.Shipment;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class SendMailService {

    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;

    private final ContactRepository contactRepository;

    @Value("${app.url.password-reset}")
    private String passwordResetBaseUrl;

//    /**
//     * 메일 전송
//     *
//     * @param mailDto 메일 정보를 담고 있는 DTO 객체
//     * @throws MessagingException           메시징 예외 처리
//     * @throws UnsupportedEncodingException 지원되지 않는 인코딩 예외 처리
//     */
//    public void sendHtmlMail(MailDto mailDto) {
//        try {
//            MimeMessage message = mailSender.createMimeMessage();
//
//            message.addRecipients(Message.RecipientType.TO, mailDto.getAddress());
//            message.setFrom(new InternetAddress("leesahhhhhhh@gmail.com", "BRIDGE MALL"));
//            message.setSubject(mailDto.getTitle());
//            message.setText(mailDto.getContent(), "utf-8", "html");
//
//            mailSender.send(message);
//
//        } catch (MessagingException | UnsupportedEncodingException e) {
//            throw new SendMailException("sendMailFailed", "메일 전송에 실패하였습니다.");
//        }
//    }

    /**
     * 주문 완료 안내 메일 전송
     *
     * @param order 주문 정보 엔티티
     * @param shipment 배송 정보 엔티티
     * @param orderDetailList 주문 상세 정보 리스트 엔티티
     * @throws MessagingException 메일 전송 과정에서 발생하는 예외 처리
     */
    public void sendOrderMail(Order order, Shipment shipment, List<OrderDetail> orderDetailList) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            MailDto mailDto = setOrderMailDto(order.getUser().getEmail());
            helper.setTo(mailDto.getAddress()); //수신자 설정
            helper.setSubject(mailDto.getTitle()); //메일 제목 설정

            // 템플릿에 전달할 데이터 설정
            HashMap<String, Object> mailValues = setOrderMailValues(order, shipment, orderDetailList);
            Context context = new Context();
            mailValues.forEach((key, value) -> {
                context.setVariable(key, value);
            });

            // 메일 내용 설정 : 템플릿 프로세스
            String html = templateEngine.process(mailDto.getTemplate(), context);
            helper.setText(html, true);

            // 템플릿에 들어가는 이미지 cid로 삽입
            helper.addInline("icon_instagram", new ClassPathResource("static/images/icon_instagram.png"));
            helper.addInline("logo", new ClassPathResource("static/images/logo.png"));

            // 메일 전송
            mailSender.send(message);

        } catch (MessagingException e) {
            throw new SendMailException("sendMailFailed", "메일 전송에 실패하였습니다.");
        }
    }

    /**
     * 주문 완료 안내 메일의 내용을 설정
     *
     * @param email 비밀번호 재설정 링크 전송 메일 주소
     * @return 생성된 메일 DTO
     */
    private MailDto setOrderMailDto(String email) {

        MailDto maildto = MailDto.builder()
                .address(email)
                .title("주문이 완료되었습니다.")
                .template("order")
                .build();

        return maildto;
    }

    /**
     * 주문 완료 안내 메일에 포함될 값을 설정
     *
     * @param order 주문 정보 엔티티
     * @param shipment 배송 정보 엔티티
     * @param orderDetailList 주문 상세 정보 리스트 엔티티
     * @return 메일 템플릿에 사용될 값들이 담긴 HashMap
     */
    private HashMap<String, Object> setOrderMailValues(Order order, Shipment shipment, List<OrderDetail> orderDetailList) {
        HashMap<String, Object> mailValues = new HashMap<>();

        mailValues.put("paymentMethod", order.getPaymentMethod());
        mailValues.put("cardNumber", order.getCardNumber());
        mailValues.put("recipientName", shipment.getRecipientName());
        mailValues.put("recipientPhone", shipment.getRecipientPhone());
        mailValues.put("shippingAddress", shipment.getShippingAddress());

        int orderTotalPrice = calculateTotalPrice(orderDetailList);
        int shipmentPrice = calculateShipmentPrice(order, orderDetailList);
        int paymentAmount = orderTotalPrice + shipmentPrice;

        mailValues.put("orderTotalPrice", formatPrice(orderTotalPrice));
        mailValues.put("shipmentPrice", formatPrice(shipmentPrice));
        mailValues.put("paymentAmount", formatPrice(paymentAmount));

        List<HashMap<String, String>> orderDetailsList = new ArrayList<>();
        for (OrderDetail od : orderDetailList) {

            HashMap<String, String> detailMap = new HashMap<>();

            detailMap.put("productImageFileUrl", od.getProduct().getProductImages().get(0).getFileUrl());
            detailMap.put("productName", od.getProduct().getName());
            detailMap.put("productSize", od.getProductSize().getSize());
            detailMap.put("totalPrice", formatPrice(calculateItemTotalPrice(od)));
            detailMap.put("orderQuantity", String.valueOf(od.getQuantity()));

            orderDetailsList.add(detailMap);
        }
        mailValues.put("orderDetails", orderDetailsList);

        return mailValues;
    }

    /**
     * 메일 전송
     *
     * @param shipment 배송 정보 엔티티
     * @throws MessagingException 메일 전송 과정에서 발생하는 예외 처리
     */
    public void sendShipmentMail(Shipment shipment) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            MailDto mailDto = setShipmentMailDto(shipment.getOrderDetails().get(0).getOrder().getUser().getEmail());
            helper.setTo(mailDto.getAddress()); //수신자 설정
            helper.setSubject(mailDto.getTitle()); //메일 제목 설정

            // 템플릿에 전달할 데이터 설정
            HashMap<String, Object> mailValues = setShipmentMailValues(shipment);
            Context context = new Context();
            mailValues.forEach((key, value) -> {
                context.setVariable(key, value);
            });

            // 메일 내용 설정 : 템플릿 프로세스
            String html = templateEngine.process(mailDto.getTemplate(), context);
            helper.setText(html, true);

            // 템플릿에 들어가는 이미지 cid로 삽입
            helper.addInline("icon_instagram", new ClassPathResource("static/images/icon_instagram.png"));
            helper.addInline("logo", new ClassPathResource("static/images/logo.png"));

            // 메일 전송
            mailSender.send(message);

        } catch (MessagingException e) {
            throw new SendMailException("sendMailFailed", "메일 전송에 실패하였습니다.");
        }
    }

    /**
     * 주문 발송 안내 메일의 내용을 설정
     *
     * @param email 비밀번호 재설정 링크 전송 메일 주소
     * @return 생성된 메일 DTO
     */
    private MailDto setShipmentMailDto(String email) {

        MailDto maildto = MailDto.builder()
                .address(email)
                .title("상품이 발송되었습니다.")
                .template("shipment")
                .build();

        return maildto;
    }

    /**
     * 주문 발송 안내 메일에 포함될 값을 설정
     *
     * @param shipment 배송 정보 엔티티
     * @return 메일 템플릿에 사용될 값들이 담긴 HashMap
     */
    private HashMap<String, Object> setShipmentMailValues(Shipment shipment) {
        HashMap<String, Object> mailValues = new HashMap<>();

        List<OrderDetail> orderDetailList = shipment.getOrderDetails();
        Order order = orderDetailList.get(0).getOrder();

        mailValues.put("paymentMethod", order.getPaymentMethod());
        mailValues.put("cardNumber", order.getCardNumber());
        mailValues.put("recipientName", shipment.getRecipientName());
        mailValues.put("recipientPhone", shipment.getRecipientPhone());
        mailValues.put("shippingAddress", shipment.getShippingAddress());

        int orderTotalPrice = calculateTotalPrice(orderDetailList);
        int shipmentPrice = calculateShipmentPrice(order, orderDetailList);
        int paymentAmount = orderTotalPrice + shipmentPrice;

        mailValues.put("orderTotalPrice", formatPrice(orderTotalPrice));
        mailValues.put("shipmentPrice", formatPrice(shipmentPrice));
        mailValues.put("paymentAmount", formatPrice(paymentAmount));

        List<HashMap<String, String>> orderDetailsList = new ArrayList<>();
        for (OrderDetail od : orderDetailList) {

            HashMap<String, String> detailMap = new HashMap<>();

            detailMap.put("productImageFileUrl", od.getProduct().getProductImages().get(0).getFileUrl());
            detailMap.put("productName", od.getProduct().getName());
            detailMap.put("productSize", od.getProductSize().getSize());
            detailMap.put("totalPrice", formatPrice(calculateItemTotalPrice(od)));
            detailMap.put("orderQuantity", String.valueOf(od.getQuantity()));

            // 배송 조회 링크 취득
            CourierCompany courierCompany = od.getShipment().getCourierCompany();
            String trackingNumber = od.getShipment().getTrackingNumber();
            String trackingLink = courierCompany.getTrackingUrl(trackingNumber);

            detailMap.put("shipmentStatus", od.getShipment().getStatus().getDescription());
            detailMap.put("courierCompany", courierCompany.getDescription());
            detailMap.put("trackingLink", trackingLink);
            detailMap.put("trackingNumber", trackingNumber);

            orderDetailsList.add(detailMap);
        }
        mailValues.put("orderDetails", orderDetailsList);

        return mailValues;
    }

    /**
     * 비밀번호 재설정 안내 메일 전송
     *
     * @param email 비밀번호 재설정 링크 전송 메일 주소
     * @param passwordResetToken 비밀번호 재설정 토큰
     * @throws MessagingException 메일 전송 과정에서 발생하는 예외 처리
     */
    public void sendPasswordResetMail(String email, String passwordResetToken) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            MailDto mailDto = setPasswordResetMailDto(email);
            helper.setTo(mailDto.getAddress()); //수신자 설정
            helper.setSubject(mailDto.getTitle()); //메일 제목 설정

            // 템플릿에 전달할 데이터 설정
            HashMap<String, String> mailValues = setPasswordResetMailValues(passwordResetToken);
            Context context = new Context();
            mailValues.forEach((key, value) -> {
                context.setVariable(key, value);
            });

            // 메일 내용 설정 : 템플릿 프로세스
            String html = templateEngine.process(mailDto.getTemplate(), context);
            helper.setText(html, true);

            // 템플릿에 들어가는 이미지 cid로 삽입
            helper.addInline("icon_instagram", new ClassPathResource("static/images/icon_instagram.png"));
            helper.addInline("logo", new ClassPathResource("static/images/logo.png"));

            // 메일 전송
            mailSender.send(message);

        } catch (MessagingException e) {
            throw new SendMailException("sendMailFailed", "메일 전송에 실패하였습니다.");
        }
    }

    /**
     * 비밀번호 재설정 안내 메일의 내용을 설정
     *
     * @param email 비밀번호 재설정 링크 전송 메일 주소
     * @return 생성된 메일 DTO
     */
    private MailDto setPasswordResetMailDto(String email) {

        MailDto maildto = MailDto.builder()
                .address(email)
                .title("비밀번호 재설정 안내")
                .template("password_reset")
                .build();

        return maildto;
    }

    /**
     * 비밀번호 재설정 안내 메일에 포함될 값을 설정
     *
     * @param passwordResetToken 비밀번호 재설정 토큰
     * @return 메일 템플릿에 사용될 값들이 담긴 HashMap
     */
    private HashMap<String, String> setPasswordResetMailValues(String passwordResetToken) {
        // 비밀번호 재설정 페이지의 URL 작성
        String passwordResetLink = passwordResetBaseUrl + passwordResetToken;

        HashMap<String, String> mailValues = new HashMap<>();
        mailValues.put("passwordResetLink", passwordResetLink);
        return mailValues;
    }

    /**
     * 문의 사항 등록 안내 메일 전송
     *
     * @param contactDto 문의 정보가 담긴 DTO 객체
     * @throws MessagingException 메일 전송 과정에서 발생하는 예외 처리
     */
    public void sendContactInquiryMail(ContactDto contactDto) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            MailDto mailDto = setContactInquiryMailDto(contactDto);
            helper.setTo(mailDto.getAddress()); //수신자 설정
            helper.setSubject(mailDto.getTitle()); //메일 제목 설정

            // 템플릿에 전달할 데이터 설정
            HashMap<String, String> mailValues = setContactInquiryMailValues(contactDto);
            Context context = new Context();
            mailValues.forEach((key, value) -> {
                context.setVariable(key, value);
            });

            // 메일 내용 설정 : 템플릿 프로세스
            String html = templateEngine.process(mailDto.getTemplate(), context);
            helper.setText(html, true);

            // 템플릿에 들어가는 이미지 cid로 삽입
            helper.addInline("icon_instagram", new ClassPathResource("static/images/icon_instagram.png"));
            helper.addInline("logo", new ClassPathResource("static/images/logo.png"));

            // 메일 전송
            mailSender.send(message);

        } catch (MessagingException e) {
            throw new SendMailException("sendMailFailed", "파일 업로드에 실패하였습니다.");
        }
    }

    /**
     * 문의 사항 등록 안내 메일의 내용을 설정
     *
     * @param contactDto 문의 정보가 담긴 DTO
     * @return 생성된 메일 DTO
     */
    private MailDto setContactInquiryMailDto(ContactDto contactDto) {

        MailDto maildto = MailDto.builder()
                .address(contactDto.getInquirerEmail())
                .title("문의 사항 등록 안내")
                .template("contact_inquiry_registation_notice")
                .build();

        return maildto;
    }

    /**
     * 문의 사항 등록 안내 메일에 포함될 값을 설정
     *
     * @param contactDto 문의 정보가 담긴 DTO
     * @return 메일 템플릿에 사용될 값들이 담긴 HashMap
     */
    private HashMap<String, String> setContactInquiryMailValues(ContactDto contactDto) {
        HashMap<String, String> mailValues = new HashMap<>();

        mailValues.put("inquirerName", contactDto.getInquirerName());
        mailValues.put("inquiryTitle", contactDto.getTitle());
        mailValues.put("inquiryContent", contactDto.getContent().replace("\n", "<br>"));

        return mailValues;
    }

    /**
     * 문의 사항 답변 등록 안내 메일 전송
     *
     * @param contactDto 문의 정보가 담긴 DTO 객체
     * @throws MessagingException 메일 전송 과정에서 발생하는 예외 처리
     */
    public void sendContactAnswerMail(ContactDto contactDto) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            MailDto mailDto = setContactAnswerMailDto(contactDto);
            helper.setTo(mailDto.getAddress()); // 수신자 설정
            helper.setSubject(mailDto.getTitle()); // 메일 제목 설정

            // 템플릿에 전달할 데이터 설정
            HashMap<String, String> mailValues = setContactAnswerMailValues(contactDto);
            Context context = new Context();
            mailValues.forEach((key, value) -> {
                context.setVariable(key, value);
            });

            // 메일 내용 설정 : 템플릿 프로세스
            String html = templateEngine.process(mailDto.getTemplate(), context);
            helper.setText(html, true);

            // 템플릿에 들어가는 이미지 cid로 삽입
            helper.addInline("icon_instagram", new ClassPathResource("static/images/icon_instagram.png"));
            helper.addInline("logo", new ClassPathResource("static/images/logo.png"));

            // 메일 전송
            mailSender.send(message);

        } catch (MessagingException e) {
            throw new SendMailException("sendMailFailed", "파일 업로드에 실패하였습니다.");
        }
    }

    /**
     * 문의 사항 답변 등록 안내 메일 작성
     *
     * @param contactDto 문의 정보
     * @return 생성된 메일 DTO
     */
    public MailDto setContactAnswerMailDto(ContactDto contactDto) {
        Long ref = contactRepository.findRefById(contactDto.getId());
        String inquirerEmail = contactRepository.findInquirerEmailById(ref);

        MailDto maildto = MailDto.builder()
                .address(inquirerEmail)
                .title("문의 사항 답변 등록 안내")
                .template("contact_answer_registation_notice")
                .build();

        return maildto;
    }

    /**
     * 문의 사항 등록 안내 메일에 포함될 값을 설정
     *
     * @param contactDto 문의 정보가 담긴 DTO
     * @return 메일 템플릿에 사용될 값들이 담긴 HashMap
     */
    private HashMap<String, String> setContactAnswerMailValues(ContactDto contactDto) {
        HashMap<String, String> mailValues = new HashMap<>();

        Long ref = contactRepository.findRefById(contactDto.getId());
        String inquirerName = contactRepository.findInquirerNameById(ref);

        mailValues.put("inquirerName", inquirerName);
        mailValues.put("answerContent", contactDto.getContent().replace("\n", "<br>"));

        return mailValues;
    }

    private int calculateItemTotalPrice(OrderDetail orderDetail) {
        return orderDetail.getFinalPrice() * orderDetail.getQuantity();
    }

    private int calculateTotalPrice(List<OrderDetail> orderDetailList) {
        int totalPrice = 0;
        for (OrderDetail od : orderDetailList) {
            totalPrice += calculateItemTotalPrice(od);
        }
        return totalPrice;
    }

    private int calculateShipmentPrice(Order order, List<OrderDetail> orderDetailList) {
        return Math.max(order.getPaymentAmount() - calculateTotalPrice(orderDetailList), 0);
    }

    private String formatPrice(int price) {
        return NumberFormat.getNumberInstance(Locale.KOREA).format(price);
    }

//    /**
//     * 임시 비밀번호 안내 메일 작성
//     *
//     * @param email    이메일 주소
//     * @param userName 사용자 이름
//     * @return 생성된 메일 DTO
//     */
//    public MailDto writeTempPasswordMail(String email, String userName) {
//        MailDto maildto = new MailDto();
//        String tempPwd = createTempPassword();
//
//        maildto.setAddress(email);
//        maildto.setTitle("고객 계정 임시 비밀번호 발급 안내");
//        maildto.setContent("안녕하세요 " + userName + "님,"
//                + "<br>고객님의 임시 비밀번호를 발급하였습니다."
//                + "<br>아래 비밀번호로 로그인하여 비밀번호를 재설정해주세요."
//                + "<br><b>" + tempPwd + "</b>");
//
//        updatePassword(email, tempPwd);
//
//        return maildto;
//    }

//    /**
//     * 비밀번호를 데이터베이스에 갱신
//     *
//     * @param email    이메일 주소
//     * @param password 새로운 비밀번호
//     */
//    @Transactional
//    public void updatePassword(String email, String password) {
//        String encodedPwd = passwordEncoder.encode(password);
//
//        User user = userRepository.findByEmailAndAuthProvider(email, AuthProvider.LOCAL)
//                .orElseThrow(() -> new NotFoundException("userNotFound", "사용자 정보를 찾을 수 없습니다."));
//
//        user.setPassword(encodedPwd);
//
//        userRepository.save(user);
//    }

//    /**
//     * 임시 비밀번호를 생성
//     *
//     * @return 생성된 임시 비밀번호
//     */
//    public String createTempPassword() {
//
//        Random random = new Random();
//        StringBuilder password = new StringBuilder(10);
//
//        char[] UPPERCASE_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
//        char[] LOWERCASE_CHARS = "abcdefghijklmnopqrstuvwxyz".toCharArray();
//        char[] NUMBER_CHARS = "0123456789".toCharArray();
//        char[] SPECIAL_CHARS = "@$!%*?&".toCharArray();
//        char[] ALL_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789@$!%*?&".toCharArray();
//
//        // 각 요구 사항을 만족시키기 위해 문자를 추가
//        password.append(UPPERCASE_CHARS[random.nextInt(UPPERCASE_CHARS.length)]);
//        password.append(LOWERCASE_CHARS[random.nextInt(LOWERCASE_CHARS.length)]);
//        password.append(NUMBER_CHARS[random.nextInt(NUMBER_CHARS.length)]);
//        password.append(SPECIAL_CHARS[random.nextInt(SPECIAL_CHARS.length)]);
//
//        // 나머지 길이를 랜덤하게 채움
//        for (int i = password.length(); i < 10; i++) {
//            password.append(ALL_CHARS[random.nextInt(ALL_CHARS.length)]);
//        }
//
//        // 섞기 위해 문자열을 셔플
//        char[] shuffledArray = password.toString().toCharArray();
//        for (int i = 0; i < shuffledArray.length; i++) {
//            int index = random.nextInt(shuffledArray.length);
//            char temp = shuffledArray[i];
//            shuffledArray[i] = shuffledArray[index];
//            shuffledArray[index] = temp;
//        }
//
//        return password.toString();
//    }
}