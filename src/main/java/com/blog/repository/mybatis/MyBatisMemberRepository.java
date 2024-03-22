package com.blog.repository.mybatis;

import com.blog.domain.Member;
import com.blog.repository.MemberRepository;
import com.blog.repository.mybatis.mappers.MemberMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MyBatisMemberRepository implements MemberRepository {

    private final MemberMapper memberMapper;

    @Override
    public Optional<Member> findByEmail(String email) {
        return Optional.empty();
    }
}
