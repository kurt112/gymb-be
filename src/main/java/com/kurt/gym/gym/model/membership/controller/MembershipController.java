package com.kurt.gym.gym.model.membership.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kurt.gym.gym.model.membership.service.MembershipService;

import lombok.RequiredArgsConstructor;

@RequestMapping("gym/memberships")
@RestController
@RequiredArgsConstructor
public class MembershipController {

    private final MembershipService membershipService;

    @GetMapping
    public ResponseEntity<?> getClasses(
            @RequestParam("page") int page,
            @RequestParam("size") int size,
            @RequestParam("search") String search) {

        return membershipService.data(search, size, page - 1);
    }

}
