package com.blog.repository.mybatis.mappers;

import com.blog.domain.Member;
import org.apache.ibatis.annotations.Mapper;

import java.util.Optional;

@Mapper
public interface MemberMapper {
    Optional<Member> findByEmail(String email);
}
