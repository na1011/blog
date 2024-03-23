package com.blog.repository.mybatis;

import com.blog.domain.Authority;
import com.blog.domain.Member;
import com.blog.repository.MemberRepository;
import com.blog.repository.mybatis.mappers.MemberMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MyBatisMemberRepository implements MemberRepository {

    private final MemberMapper memberMapper;

    @Override
    public Member save(Member user) {
        memberMapper.save(user);
        return user;
    }

    @Override
    public void insertAuthority(List<Authority> authorities) {
        memberMapper.insertAuthority(authorities);
    }

    @Override
    public Optional<Member> findByEmail(String email) {
        return Optional.empty();
    }

    @Override
    public Optional<Member> findByName(String name) {
        return memberMapper.findByName(name);
    }
}
