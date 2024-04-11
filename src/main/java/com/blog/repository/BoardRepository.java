package com.blog.repository;

import com.blog.domain.Board;
import com.blog.domain.BoardFile;
import com.blog.model.board.BoardSearchDto;

import java.util.List;
import java.util.Optional;

public interface BoardRepository {
    Board save(Board board);
    Optional<Board> findById(Long id);
    List<Board> findByMemberId(Long memberId);
    List<Board> findAll(BoardSearchDto params);
    Integer getPageMaxCount(BoardSearchDto search);
    void deleteSetupByBoardIdAndMemberId(Long boardId, Long memberId);
    void updateByBoard(Board board);
    void remove(Long boardId);
    Optional<BoardFile> findBoardFileById(Long id);
}
