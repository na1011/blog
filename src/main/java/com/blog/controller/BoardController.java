package com.blog.controller;

import com.blog.model.FileStore;
import com.blog.model.PagingResponseDto;
import com.blog.model.board.BoardDto;
import com.blog.model.board.BoardSaveDto;
import com.blog.model.board.BoardSearchDto;
import com.blog.model.board.SearchType;
import com.blog.model.member.MemberDetailsDto;
import com.blog.model.reply.ReplyDeleteDto;
import com.blog.model.reply.ReplyDto;
import com.blog.model.reply.ReplySaveDto;
import com.blog.model.reply.ReplySearchDto;
import com.blog.service.BoardService;
import com.blog.service.ReplyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/board")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;
    private final ReplyService replyService;
    private final FileStore fileStore;

    @Value("10")
    private Integer fileMaxSize;

    /**
     * 게시글 목록 뷰
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
     * 게시글 보기 뷰
     */
    @GetMapping("/{boardId}")
    public String boardDetailForm(@AuthenticationPrincipal MemberDetailsDto memberDetailsDto,
                                  @PathVariable("boardId") Long boardId,
                                  Model model) {

        BoardDto boardDto = boardService.findBoardAndFiles(boardId);

        model.addAttribute("authMemberId", memberDetailsDto.getMember().getMemberId()); // 파일 삭제 하기 위한 model 속성
        model.addAttribute("authMemberNm", memberDetailsDto.getMember().getMemberNm());
        model.addAttribute("boardDto", boardDto);

        return "boardDetail";
    }

    /**
     * 게시글 상세 보기 뷰 (게시글, 파일, 댓글 불러오기)
     */
    @GetMapping("/{boardId}")
    public String boardDetailForm(@AuthenticationPrincipal MemberDetailsDto memberDetailsDto,
                                  @PathVariable("boardId") Long boardId,
                                  @ModelAttribute("params") final ReplySearchDto params,
                                  @RequestParam(value = "replySaveFailure", defaultValue = "false", required = false) Boolean replySaveFailure,
                                  Model model) {
        if(replySaveFailure) {
            model.addAttribute("replySaveFailure", "댓글이 너무 길거나 짧습니다. (1자 이상 500자 이내)");
        }

        BoardDto boardDto = boardService.findBoardAndFiles(boardId);
        PagingResponseDto<ReplyDto> pagingResponseDto = replyService.findPageReply(params, boardId);

        model.addAttribute("replySaveDto", new ReplySaveDto());
        model.addAttribute("replyModifyDto", new ReplyDeleteDto());
        model.addAttribute("authMemberId", memberDetailsDto.getMember().getMemberId()); // 파일 삭제 하기 위한 model 속성
        model.addAttribute("authMemberNm", memberDetailsDto.getMember().getMemberNm());
        model.addAttribute("boardDto", boardDto);
        model.addAttribute("pagingResponseDto", pagingResponseDto);

        return "boardDetail";
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

    /**
     * 게시글 작성 및 파일 업로드
     */
    @PostMapping("/write")
    public String boardWrite(@AuthenticationPrincipal MemberDetailsDto memberDetailsDto,
                             @Valid @ModelAttribute BoardSaveDto boardSaveDto,
                             BindingResult bindingResult,
                             Model model) {

        model.addAttribute("fileMaxSize", fileMaxSize);

        if(bindingResult.hasErrors()) {
            return "boardWrite";
        }

        if(!fileStore.isImageFiles(boardSaveDto.getImageFiles())) {
            // 파일이 존재하면서 이미지 파일 확장자(jpg, png, gif)가 아닌 경우 글로벌 오류 메세지
            bindingResult.reject("isNotImage", "이미지 파일은 jpg, png, gif 만 가능합니다.");
            return "boardWrite";
        }

        if(boardSaveDto.getImageFiles().size() > 3) {
            // 이미지 파일이 3개 이상일 경우 글로벌 오류 메세지
            bindingResult.reject("isManyImage", "이미지 파일은 최대 3개까지 가능합니다.");
            return "boardWrite";
        }

        boardService.createBoard(boardSaveDto, memberDetailsDto.getMember());

        return "redirect:/board";
    }
}
