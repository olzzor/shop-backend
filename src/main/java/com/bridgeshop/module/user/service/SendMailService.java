package com.bridgeshop.module.user.service;

import com.bridgeshop.common.dto.MailDto;
import com.bridgeshop.module.user.entity.AuthProvider;
import com.bridgeshop.module.user.repository.UserRepository;
import com.bridgeshop.module.user.entity.User;
import com.bridgeshop.common.exception.NotFoundException;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.Random;

@Service
@AllArgsConstructor
public class SendMailService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private JavaMailSender mailSender;

    /**
     * 메일 전송
     **/
    public void sendMail(MailDto mailDto) throws MessagingException, UnsupportedEncodingException {
        MimeMessage message = mailSender.createMimeMessage();

        message.addRecipients(Message.RecipientType.TO, mailDto.getAddress());
        message.setFrom(new InternetAddress("leesahhhhhhh@gmail.com", "BRIDGE MALL"));
        message.setSubject(mailDto.getTitle());
        message.setText(mailDto.getMessage(), "utf-8", "html");
//        SimpleMailMessage message = new SimpleMailMessage();
//        message.setTo(mailDto.getAddress());
//        message.setFrom("leesahhhhhhh@gmail.com");
//        message.setSubject(mailDto.getTitle());
//        message.setText(mailDto.getMessage());

        mailSender.send(message);
    }

    /**
     * 임시 비밀번호 안내 메일 작성
     **/
    public MailDto writeTempPasswordMail(String email, String userName) {
        MailDto maildto = new MailDto();
        String tempPwd = createTempPassword();

        maildto.setAddress(email);
        maildto.setTitle("고객 계정 임시 비밀번호");
        maildto.setMessage("안녕하세요 " + userName + "님,"
                + "<br>고객님의 임시 비밀번호를 발급하였습니다."
                + "<br>아래 비밀번호로 로그인하여 비밀번호를 재설정해주세요."
                + "<br><b>" + tempPwd + "</b>");
//        maildto.setMessage("안녕하세요." + userName + "님"
//                + "\n" + "고객님의 임시 비밀번호를 발급하였습니다."
//                + "\n" + "아래 비밀번호로 로그인하여 비밀번호를 재설정해주세요."
//                + "\n" + tempPwd);

        updatePassword(email, tempPwd);

        return maildto;
    }

    /**
     * 비밀번호 DB갱신
     **/
    @Transactional
    public void updatePassword(String email, String password) {
        String encodedPwd = passwordEncoder.encode(password);

        User user = userRepository.findByEmailAndAuthProvider(email, AuthProvider.LOCAL)
                .orElseThrow(() -> new NotFoundException("userNotFound", "사용자 정보를 찾을 수 없습니다."));

        user.setPassword(encodedPwd);

        userRepository.save(user);
    }

    /**
     * 임시 비밀번호 생성
     **/
    public String createTempPassword() {
//        char[] charSet = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F',
//                'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
//
//        String str = "";
//        int idx = 0;
//
//        for (int i = 0; i < 10; i++) {
//            idx = (int) (charSet.length * Math.random());
//            str += charSet[idx];
//        }
//        return str;

        Random random = new Random();
        StringBuilder password = new StringBuilder(10);

        char[] UPPERCASE_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
        char[] LOWERCASE_CHARS = "abcdefghijklmnopqrstuvwxyz".toCharArray();
        char[] NUMBER_CHARS = "0123456789".toCharArray();
        char[] SPECIAL_CHARS = "@$!%*?&".toCharArray();
        char[] ALL_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789@$!%*?&".toCharArray();

        // 각 요구 사항을 만족시키기 위해 문자를 추가
        password.append(UPPERCASE_CHARS[random.nextInt(UPPERCASE_CHARS.length)]);
        password.append(LOWERCASE_CHARS[random.nextInt(LOWERCASE_CHARS.length)]);
        password.append(NUMBER_CHARS[random.nextInt(NUMBER_CHARS.length)]);
        password.append(SPECIAL_CHARS[random.nextInt(SPECIAL_CHARS.length)]);

        // 나머지 길이를 랜덤하게 채움
        for (int i = password.length(); i < 10; i++) {
            password.append(ALL_CHARS[random.nextInt(ALL_CHARS.length)]);
        }

        // 섞기 위해 문자열을 셔플
        char[] shuffledArray = password.toString().toCharArray();
        for (int i = 0; i < shuffledArray.length; i++) {
            int index = random.nextInt(shuffledArray.length);
            char temp = shuffledArray[i];
            shuffledArray[i] = shuffledArray[index];
            shuffledArray[index] = temp;
        }

        return password.toString();
    }
}