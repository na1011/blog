package com.blog.service;

import com.blog.common.exception.DuplicateException;
import com.blog.domain.Authority;
import com.blog.domain.Member;
import com.blog.model.Role;
import com.blog.model.member.MemberSaveDto;
import com.blog.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Transactional
    public void save(MemberSaveDto memberDto) {
        // 이메일이 DB 에 중복되는지 확인
        Optional<Member> emailCheckMember = memberRepository.findByEmail(memberDto.getMemberEmail());
        if(emailCheckMember.isPresent()) {
            throw new DuplicateException("이미 존재하는 이메일입니다.");
        }

        // 닉네임이 DB 에 중복되는지 확인
        Optional<Member> nameCheckMember = memberRepository.findByName(memberDto.getMemberNm());
        if(nameCheckMember.isPresent()) {
            throw new DuplicateException("이미 존재하는 닉네임입니다.");
        }

        // 저장
        Member member = Member.builder()
                .memberNm(memberDto.getMemberNm())
                .memberEmail(memberDto.getMemberEmail())
                .memberPwd(passwordEncoder.encode(memberDto.getMemberPwd())) // 암호화 처리
                .build();

        memberRepository.save(member);

        List<Authority> authList = new ArrayList<>();
        // 유저 권한 등록
        Authority auth = Authority.builder()
                .memberId(member.getMemberId())
                .authEmail(member.getMemberEmail())
                .authRole(Role.USER.getAuth()).build();

        authList.add(auth);

        memberRepository.insertAuthority(authList);
    }

    @Transactional(readOnly = true)
    public Optional<Member> login(String email) {
        return memberRepository.findByEmail(email);
    }
}
