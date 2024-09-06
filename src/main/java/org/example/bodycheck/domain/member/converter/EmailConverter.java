package org.example.bodycheck.domain.member.converter;

import org.example.bodycheck.domain.member.entity.Email;

public class EmailConverter {

    public static Email toMail(String email, String code) {
        return Email.builder()
                .email(email)
                .code(code)
                .build();
    }
}
