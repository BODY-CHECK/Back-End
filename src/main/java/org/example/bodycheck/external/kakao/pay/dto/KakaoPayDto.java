package org.example.bodycheck.external.kakao.pay.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

public class KakaoPayDto {

	@Getter
	@Setter
	@ToString
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class Amount {
		@JsonProperty("total")
		private int total;
		@JsonProperty("tax_free")
		private int taxFree;
		@JsonProperty("tax")
		private int tax;
		@JsonProperty("point")
		private int point;
		@JsonProperty("discount")
		private int discount;
		@JsonProperty("green_deposit")
		private int greenDeposit;
	}

	@Data
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class KakaoReadyResponse {
		@JsonProperty("tid")
		private String tid;
		@JsonProperty("next_redirect_app_url")
		private String nextRedirectAppUrl;
		@JsonProperty("next_redirect_mobile_url")
		private String nextRedirectMobileUrl;
		@JsonProperty("next_redirect_pc_url")
		private String nextRedirectPcUrl;
		@JsonProperty("android_app_scheme")
		private String androidAppScheme;
		@JsonProperty("ios_app_scheme")
		private String iosAppScheme;
		@JsonProperty("created_at")
		private String createdAt;
	}

	@Getter
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class KakaoApproveRequest {
		@JsonProperty("tid")
		private String tid;
		@JsonProperty("pgToken")
		private String pgToken;
	}

	@Getter
	@Setter
	@ToString
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class KakaoApproveResponse {
		@JsonProperty("tid")
		private String aid;
		@JsonProperty("tid")
		private String tid;
		@JsonProperty("tid")
		private String cid;
		@JsonProperty("tid")
		private String sid;
		@JsonProperty("partner_order_id")
		private String partnerOrderId;
		@JsonProperty("partner_user_id")
		private String partnerUserId;
		@JsonProperty("payment_method_type")
		private String paymentMethodType;
		@JsonProperty("amount")
		private Amount amount;
		@JsonProperty("item_name")
		private String itemName;
		@JsonProperty("item_code")
		private String itemCode;
		@JsonProperty("quantity")
		private int quantity;
		@JsonProperty("created_at")
		private String createdAt;
		@JsonProperty("approved_at")
		private String approvedAt;
		@JsonProperty("payload")
		private String payload;
	}

	@Getter
	@Setter
	@ToString
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class KakaoCancelResponse {
		@JsonProperty("aid")
		private String aid;
		@JsonProperty("tid")
		private String tid;
		@JsonProperty("cid")
		private String cid;
		@JsonProperty("status")
		private String status;
		@JsonProperty("partner_order_id")
		private String partnerOrderId;
		@JsonProperty("partner_user_id")
		private String partnerUserId;
		@JsonProperty("payment_method_type")
		private String paymentMethodType;
		@JsonProperty("amount")
		private Amount amount;
		@JsonProperty("approved_cancel_amount")
		private ApprovedCancelAmount approvedCancelAmount;
		@JsonProperty("canceled_amount")
		private CanceledAmount canceledAmount;
		@JsonProperty("cancel_available_amount")
		private CancelAvailableAmount cancelAvailableAmount;
		@JsonProperty("item_name")
		private String itemName;
		@JsonProperty("item_code")
		private String itemCode;
		@JsonProperty("quantity")
		private int quantity;
		@JsonProperty("created_at")
		private String createdAt;
		@JsonProperty("approved_at")
		private String approvedAt;
		@JsonProperty("canceled_at")
		private String canceledAt;
		@JsonProperty("payload")
		private String payload;
	}

	@Getter
	@Setter
	@ToString
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class KakaoSubscribeCancelResponse {
		@JsonProperty("cid")
		private String cid;
		@JsonProperty("sid")
		private String sid;
		@JsonProperty("status")
		private String status;
		@JsonProperty("created_at")
		private String createdAt;
		@JsonProperty("last_approved_at")
		private String lastApprovedAt;
		@JsonProperty("inactivated_at")
		private String inactivatedAt;
	}

	@Getter
	@Setter
	@ToString
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class KakaoSubscribeStatusResponse {
		@JsonProperty("available")
		private boolean available;
		@JsonProperty("cid")
		private String cid;
		@JsonProperty("sid")
		private String sid;
		@JsonProperty("status")
		private String status;
		@JsonProperty("item_name")
		private String itemName;
		@JsonProperty("payment_method_type")
		private String paymentMethodType;
		@JsonProperty("created_at")
		private String createdAt;
		@JsonProperty("last_approved_at")
		private String lastApprovedAt;
		@JsonProperty("use_point_status")
		private String usePointStatus;
	}

	@Getter
	@Setter
	@ToString
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class ApprovedCancelAmount {
		@JsonProperty("total")
		private int total;
		@JsonProperty("tax_free")
		private int taxFree;
		@JsonProperty("vat")
		private int vat;
		@JsonProperty("point")
		private int point;
		@JsonProperty("discount")
		private int discount;
		@JsonProperty("green_deposit")
		private int greenDeposit;
	}

	@Getter
	@Setter
	@ToString
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class CanceledAmount {
		@JsonProperty("total")
		private int total;
		@JsonProperty("tax_free")
		private int taxFree;
		@JsonProperty("vat")
		private int vat;
		@JsonProperty("point")
		private int point;
		@JsonProperty("discount")
		private int discount;
		@JsonProperty("green_deposit")
		private int greenDeposit;
	}

	@Getter
	@Setter
	@ToString
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class CancelAvailableAmount {
		@JsonProperty("total")
		private int total;
		@JsonProperty("tax_free")
		private int taxFree;
		@JsonProperty("vat")
		private int vat;
		@JsonProperty("point")
		private int point;
		@JsonProperty("discount")
		private int discount;
		@JsonProperty("green_deposit")
		private int greenDeposit;
	}

	@Builder
	@Getter
	@Setter
	@ToString
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class KakaoPayStatus {
		@JsonProperty("isLogExist")
		private boolean isLogExist;
		@JsonProperty("status")
		private String status;
		@JsonProperty("last_approved_at")
		private String lastApprovedAt;
	}
}
