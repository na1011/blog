package com.blog.repository.mybatis;

import com.blog.domain.Board;
import com.blog.domain.BoardFile;
import com.blog.model.board.BoardSearchDto;
import com.blog.repository.BoardRepository;
import com.blog.repository.mybatis.mappers.BoardMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MybatisBoardRepository implements BoardRepository {

    private final BoardMapper boardMapper;

    @Override
    public Board save(Board board) {
        boardMapper.save(board);
        return board;
    }

    @Override
    public Optional<Board> findById(Long id) {
        return Optional.empty();
    }

    @Override
    public List<Board> findByMemberId(Long memberId) {
        return null;
    }

    @Override
    public List<Board> findAll(BoardSearchDto search) {
        return boardMapper.findAll(search);
    }

    @Override
    public Integer getPageMaxCount(BoardSearchDto search) {
        return boardMapper.getPageMaxCount(search);
    }

    @Override
    public void deleteSetupByBoardIdAndMemberId(Long boardId, Long memberId) {

    }

    @Override
    public void updateByBoard(Board board) {

    }

    @Override
    public void remove(Long boardId) {

    }

    @Override
    public Optional<BoardFile> findBoardFileById(Long id) {
        return boardMapper.findBoardFileById(id);
    }
}
