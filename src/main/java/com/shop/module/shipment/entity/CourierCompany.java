package com.shop.module.shipment.entity;

public enum CourierCompany {

    CJ_LOGISTICS("CJ 대한통운", "https://trace.cjlogistics.com/next/tracking.html?wblNo="), // 확인 완료
    LOTTE("롯데 택배", "https://www.lotteglogis.com/home/reservation/tracking/linkView?InvNo="),
    KOREA_POST("우체국 택배", "https://service.epost.go.kr/trace.RetrieveDomRigiTraceList.comm?displayHeader=N&sid1="),
    LOGEN("로젠 택배", "https://www.ilogen.com/m/personal/trace.pop/"),
    HANJIN("한진 택배", "https://www.hanjin.com/kor/CMS/DeliveryMgr/WaybillResult.do?mCode=MN038&schLang=KR&wblnumText2="),
    CU("CU 편의점 택배", "https://www.cupost.co.kr/postbox/delivery/localResult.cupost?invoice_no="),
    KYUNG_DONG("경동 택배", "https://kdexp.com/service/delivery/etc/delivery.do?barcode="),
    GS_POSTBOX("GS 편의점 택배", "https://www.cvsnet.co.kr/invoice/tracking.do?invoice_no="),
    OTHER("기타", "");

    private final String description;
    private final String trackingUrl;

    CourierCompany(String description, String trackingUrl) {
        this.description = description;
        this.trackingUrl = trackingUrl;
    }

    public String getDescription() {
        return description;
    }

    public String getTrackingUrl(String trackingNumber) {
        if (this == OTHER || trackingNumber == null || trackingNumber.isEmpty()) {
            return "#"; // 기타 또는 송장번호가 없는 경우에는 링크 없음
        }
        return trackingUrl + trackingNumber;
    }
}
