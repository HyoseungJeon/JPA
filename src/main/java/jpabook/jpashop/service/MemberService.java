package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.dto.MemberDto;
import jpabook.jpashop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    /**
     * 회원 가입
     * */
    @Transactional
    public Long join(Member member) {
        // 1. 유효성 검증
        validateDuplicateMember(member);

        // 2. 저장
        memberRepository.save(member);

        return member.getId();
    }

    private void validateDuplicateMember(Member member) {
        List<Member> members = memberRepository.findByName(member.getName());
        if (!members.isEmpty()) {
            throw new IllegalStateException("이미 존재하는 이름 입니다.");
        }
    }

    // 회원 전체 조회
    /**
     * 회원 전체 조회
     * */
    public List<MemberDto> findMembers() {
        List<Member> memberList = memberRepository.findAll();
        List<MemberDto> memberDtoList = new ArrayList<>();

        memberList.forEach(el -> {
            MemberDto memberDto = new MemberDto();
            BeanUtils.copyProperties(el, memberDto);
            memberDtoList.add(memberDto);
        });

        return memberDtoList;
    }

    public Member findOne(Long memberId) {
        return memberRepository.findOne(memberId);
    }
}
