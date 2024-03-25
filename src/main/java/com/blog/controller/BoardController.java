package com.blog.controller;

import com.blog.model.PagingResponseDto;
import com.blog.model.board.BoardDto;
import com.blog.model.board.BoardSaveDto;
import com.blog.model.board.BoardSearchDto;
import com.blog.model.board.SearchType;
import com.blog.model.member.MemberDetailsDto;
import com.blog.service.BoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/board")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    /**
     * 게시글 보기 뷰
     */
    @GetMapping()
    public String boardForm(@ModelAttribute("params") final BoardSearchDto params,
                            Model model) {
        PagingResponseDto<BoardDto> response = boardService.findAllBoard(params);
        model.addAttribute("response", response);
        List<SearchType> searchTypeList = new ArrayList<>(Arrays.asList(SearchType.writer, SearchType.content));

        model.addAttribute("searchTypeList", searchTypeList);
        return "board";
    }

    /**
     * 게시글 쓰기 뷰
     */
    @GetMapping("/writeView")
    public String boardWriteFrom(@ModelAttribute BoardSaveDto boardSaveDto,
                                 Model model) {
        return "boardWrite";
    }

    /**
     * 게시글 작성
     */
    @PostMapping("/write")
    public String boardWrite(@AuthenticationPrincipal MemberDetailsDto memberDetailsDto,
                             @ModelAttribute BoardSaveDto boardSaveDto) {
        boardService.createBoard(boardSaveDto, memberDetailsDto.getMember());

        return "redirect:/board";
    }
}
