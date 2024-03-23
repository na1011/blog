package com.blog.repository;

import com.blog.domain.Authority;
import com.blog.domain.Member;

import java.util.List;
import java.util.Optional;

public interface MemberRepository {
    Member save(Member user);
    void insertAuthority(List<Authority> authorities);
    Optional<Member> findByEmail(String email);
    Optional<Member> findByName(String name);
}
