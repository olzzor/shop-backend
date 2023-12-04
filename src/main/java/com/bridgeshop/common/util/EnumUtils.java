package com.bridgeshop.common.util;

public class EnumUtils {

    /**
     * 주어진 Enum 클래스와 이름에 대해 해당 이름의 Enum 값이 존재하는지 확인합니다.
     *
     * @param <E>       Enum 타입
     * @param enumClass 확인하려는 Enum의 클래스
     * @param enumName  확인하려는 Enum의 이름
     * @return 해당 이름의 Enum 값이 있으면 true, 그렇지 않으면 false
     */
    public static <E extends Enum<E>> boolean isValueOf(Class<E> enumClass, String enumName) {
        for (E e : enumClass.getEnumConstants()) {
            if (e.name().equals(enumName.toUpperCase())) {
                return true;
            }
        }
        return false;
    }
}
