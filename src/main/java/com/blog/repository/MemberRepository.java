package com.blog.repository;

import com.blog.domain.Member;

import java.util.Optional;

public interface MemberRepository {
    Member save(Member user);
    Optional<Member> findByEmail(String email);
}
