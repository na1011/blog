package com.blog.model.board;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
public class BoardSaveDto {
    private String boardTitle;
    private String boardContent;
    private List<MultipartFile> imageFiles; // 이미지를 다중 업로드 하기 위해 MultipartFile 사용
    private MultipartFile attachFile; // 첨부 파일

    @Builder
    public BoardSaveDto(String boardTitle, String boardContent) {
        this.boardTitle = boardTitle;
        this.boardContent = boardContent;
    }
}