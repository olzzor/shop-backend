package com.shop.module.cart.entity;

import com.shop.common.entity.BaseTimeEntity;
import com.shop.module.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "carts")
public class Cart extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "user_id", referencedColumnName = "id")
    private User user;

    @Column(nullable = false)
    private boolean activateFlag;

    @OneToMany(mappedBy = "cart")
    private List<CartProduct> cartProducts = new ArrayList<>();

    // 빌더 패턴을 사용하는 생성자
    @Builder
    public Cart(User user, Boolean activateFlag) {
        this.user = user;
        this.activateFlag = (activateFlag == null) ? true : activateFlag; // 기본값 설정
    }

    // 설정자 메서드들
    public void setUser(User user) {
        this.user = user;
    }

    public void setActivateFlag(boolean activateFlag) {
        this.activateFlag = activateFlag;
    }
}
