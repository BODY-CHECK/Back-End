package org.example.bodycheck.external.fcm.service;

import java.util.List;

import org.example.bodycheck.common.apipayload.code.status.ErrorStatus;
import org.example.bodycheck.common.exception.handler.GeneralHandler;
import org.example.bodycheck.domain.member.entity.Device;
import org.example.bodycheck.domain.member.service.deviceservice.DeviceQueryService;
import org.example.bodycheck.external.fcm.dto.FcmRequestDto;
import org.springframework.stereotype.Service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FcmService {

	private final DeviceQueryService deviceQueryService;

	public String sendMessage(Long memberId, FcmRequestDto request) {
		List<Device> firebaseTokenList = deviceQueryService.getDeviceList(memberId);

		if (firebaseTokenList.isEmpty()) {
			throw new GeneralHandler(ErrorStatus.TOKEN_NOT_EXIST);
		}

		StringBuilder result = new StringBuilder();

		for (Device token : firebaseTokenList) {
			Message message = Message.builder()
				.setToken(token.getFcmToken())
				.setNotification(Notification.builder()
					.setTitle(request.getTitle())
					.setBody(request.getBody())
					.build())
				.build();

			try {
				String response = FirebaseMessaging.getInstance().send(message);
				result.append("Message sent to token ").append(token.getFcmToken())
					.append(": ").append(response).append("\n");
			} catch (FirebaseMessagingException e) {
				e.printStackTrace();
				result.append("Failed to send message to token ").append(token.getFcmToken()).append("\n");
			}
		}

		return result.toString();

	}
}
