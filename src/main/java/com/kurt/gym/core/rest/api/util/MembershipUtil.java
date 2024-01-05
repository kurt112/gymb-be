package com.kurt.gym.core.rest.api.util;

import com.kurt.gym.core.persistence.entity.Membership;
import com.kurt.gym.core.persistence.entity.MembershipWithUser;
import com.kurt.gym.core.persistence.repository.MembershipRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
public class MembershipUtil {

    private static MembershipRepository membershipRepository;

    public static void initRepositories(MembershipRepository membershipRepository){
            MembershipUtil.membershipRepository = membershipRepository;
    }

    private MembershipUtil(){}

    public static void save(Membership membership) {
        membershipRepository.saveAndFlush(membership);
    }

    public static Membership findById(Long id) {
        return membershipRepository.findById(id).orElse(null);
    }

    public static void deleteById(Long id) {
        membershipRepository.deleteById(id);
    }

    public static void delete(Membership membership){
        membershipRepository.delete(membership);
    }

    public static Page<Membership> findAllByOrderByCreatedAtDesc(String search, Pageable pageable){
        return membershipRepository.findAllByOrderByCreatedAtDesc(search,pageable);
    }

    public static Page<MembershipWithUser> getMembershipMembers(Long membershipId, Pageable pageable){
        return membershipRepository.getMembershipMembers(membershipId, pageable);
    }

    public static Membership getReferenceById(Long id){
        return membershipRepository.getReferenceById(id);
    }

}
