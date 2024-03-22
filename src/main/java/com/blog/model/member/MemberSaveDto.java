package com.blog.model.member;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter
@Setter
@NoArgsConstructor
public class MemberSaveDto {
    @Pattern(regexp = "^[ㄱ-ㅎ가-힣a-z0-9-_]{2,10}$", message = "닉네임은 특수문자를 제외한 2~10자리여야 합니다.")
    private String memberNm;
    @NotBlank(message = "이메일은 필수 입력사항 입니다.")
    @Email(message = "이메일 형식이 올바르지 않습니다.")
    private String memberEmail;
    @Pattern(regexp = "(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,16}", message = "비밀번호는 8~16자 영문 대 소문자, 숫자, 특수문자를 사용하세요.")
    private String memberPwd;

    @Builder
    public MemberSaveDto(String memberNm, String memberEmail, String memberPwd) {
        this.memberNm = memberNm;
        this.memberEmail = memberEmail;
        this.memberPwd = memberPwd;
    }
}