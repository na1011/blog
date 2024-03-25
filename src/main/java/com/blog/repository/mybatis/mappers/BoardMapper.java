package com.blog.repository.mybatis.mappers;

import com.blog.domain.Board;
import com.blog.model.board.BoardSearchDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

@Mapper
public interface BoardMapper {
    void save(Board board);
    List<Board> findByMemberId(Long memberId);
    Optional<Board> findById(Long id);
    List<Board> findAll(BoardSearchDto search);
    void updateByBoard(Board board);
    void deleteSetupByBoardIdAndMemberId(@Param("boardId") Long boardId, @Param("memberId") Long memberId);
    void remove(Long boardId);
    Integer getPageMaxCount(BoardSearchDto search);
}
