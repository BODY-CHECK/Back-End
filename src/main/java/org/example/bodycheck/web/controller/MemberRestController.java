package org.example.bodycheck.web.controller;

import lombok.RequiredArgsConstructor;
import org.example.bodycheck.service.MemberService.MemberCommandService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/members")
public class MemberRestController {

    private final MemberCommandService memberCommandService;
}
