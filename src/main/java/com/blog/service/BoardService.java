package com.blog.service;

import com.blog.common.exception.BadRequestException;
import com.blog.domain.Board;
import com.blog.domain.BoardFile;
import com.blog.domain.Member;
import com.blog.model.Pagination;
import com.blog.model.PagingResponseDto;
import com.blog.model.board.BoardDto;
import com.blog.model.board.BoardSaveDto;
import com.blog.model.board.BoardSearchDto;
import com.blog.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;

    // 게시글 셍성
    @Transactional
    public void createBoard(BoardSaveDto boardSaveDto, Member member) {
        Board board = Board.builder()
                .memberId(member.getMemberId())
                .boardWriter(member.getMemberNm())
                .boardTitle(boardSaveDto.getBoardTitle())
                .boardContent(boardSaveDto.getBoardContent())
                .build();
        boardRepository.save(board);
    }

    // 게시글 모두 조회 (페이징 처리)
    @Transactional(readOnly = true)
    public PagingResponseDto<BoardDto> findAllBoard(final BoardSearchDto params) {
        Integer count = boardRepository.getPageMaxCount(params);

        if (count < 1) {
            // 조건에 해당하는 데이터가 없는 경우, 응답 데이터에 비어있는 리스트와 null 을 담아 반환
            return new PagingResponseDto<>(Collections.emptyList(), null);
        }

        // Pagination 객체를 생성해서 페이지 정보 계산 후 SearchDto 타입의 객체인 params 에 계산된 페이지 정보 저장
        Pagination pagination = new Pagination(count, params);
        params.setPagination(pagination);

        // 계산된 페이지 정보의 일부(limitStart, recordSize)를 기준으로 리스트 데이터 조회 후 응답 데이터 반환
        List<Board> findAll = boardRepository.findAll(params);

        List<BoardDto> boardDtoList = findAll
                .stream()
                .map(Board::toDto)
                .collect(Collectors.toList());

        return new PagingResponseDto<>(boardDtoList, pagination);
    }

    /**
     * 게시글 조회,
     * 존재하지 않은 게시글, 삭제된 게시글 언체크 예외 throw
     * @return BoardDto
     */
    @Transactional(readOnly = true)
    public BoardDto findBoard(Long boardId) {
        // boardId 로 된 File 이 있는지 조회
        Optional<BoardFile> BoardFileOptional = boardRepository.findBoard(boardId);

        // 조회된 데이터가 존재 하지 않을 경우 예외 발생
        BoardFile boardFile = BoardFileOptional.orElseThrow(() -> new BadRequestException("존재하지 않은 게시글입니다."));

        // 게시글이 삭제되어 있는 경우 예외 발생 (boardDeleteDate 가 null 이 아닐 경우)
        if(!ObjectUtils.isEmpty(boardFile.getBoardDeleteDate())) {
            throw new BadRequestException("삭제된 게시글입니다.");
        }

        // boardFile 이 null 이 아닐 경우
        if(!ObjectUtils.isEmpty(boardFile.getFileList())) {
            return BoardDto.builder()
                    .boardId(boardFile.getBoardId())
                    .memberId(boardFile.getMemberId())
                    .boardWriter(boardFile.getBoardWriter())
                    .boardTitle(boardFile.getBoardTitle())
                    .boardContent(boardFile.getBoardContent())
                    .boardRegdate(boardFile.getBoardRegdate())
                    .fileList(boardFile.getFileList())
                    .build();
        }

        // * File 이 존재하지 않은 경우 *
        return BoardDto.builder()
                .boardId(boardFile.getBoardId())
                .memberId(boardFile.getMemberId())
                .boardWriter(boardFile.getBoardWriter())
                .boardTitle(boardFile.getBoardTitle())
                .boardContent(boardFile.getBoardContent())
                .boardRegdate(boardFile.getBoardRegdate())
                .build();
    }
}
