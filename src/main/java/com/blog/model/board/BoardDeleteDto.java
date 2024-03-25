package com.blog.model.board;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BoardDeleteDto {
    private Long boardId;

    @Builder
    public BoardDeleteDto(Long boardId) {
        this.boardId = boardId;
    }
}
