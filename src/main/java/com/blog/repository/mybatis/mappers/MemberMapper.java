package com.blog.repository.mybatis.mappers;

import com.blog.domain.Authority;
import com.blog.domain.Member;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Optional;

@Mapper
public interface MemberMapper {
    void save(Member member);
    void insertAuthority(List<Authority> authorities);
    Optional<Member> findByEmail(String email);
    Optional<Member> findByName(String name);
}
