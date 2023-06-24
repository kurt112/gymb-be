package com.kurt.gym.gym.membership.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kurt.gym.gym.membership.model.Membership;
import com.kurt.gym.gym.membership.service.MembershipService;
import com.kurt.gym.helper.service.ApiMessage;

import lombok.RequiredArgsConstructor;

@RequestMapping("gym/memberships")
@RestController
@RequiredArgsConstructor
public class MembershipController {

    private final MembershipService membershipService;

    @GetMapping
    public ResponseEntity<?> getMembership(
            @RequestParam("page") int page,
            @RequestParam("size") int size,
            @RequestParam("search") String search) {

        return membershipService.data(search, size, page - 1);
    }

    @GetMapping("/{id}/members")
    public ResponseEntity<?> getMembershipMembers(
            @PathVariable long id,
            @RequestParam("page") int page,
            @RequestParam("size") int size,
            @RequestParam("search") String search) {

        return membershipService.getMembershipMembers(id, search, size, page - 1);
    }

    @PostMapping
    public ResponseEntity<?> createMembershipPromo(@RequestBody Membership membership) {
        return membershipService.save(membership);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getMembership(@PathVariable Long id) {

        return membershipService.findOne(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateMembership(@PathVariable Long id, @RequestBody Membership membership) {
        if (membership.getId() != id)
            ApiMessage.errorResponse("Id Not Match To Body");

        if (membershipService.referencedById(id) == null)
            return ApiMessage.errorResponse("Can't find membership Data");

        return membershipService.save(membership);
    }

    @PostMapping("/{membershipId}/enroll-customer/{rfId}")
    public ResponseEntity<?> enrollCustomer(@PathVariable Long membershipId, @PathVariable String rfId) {
        return membershipService.enrollCustomer(rfId, membershipId);
    }
}
