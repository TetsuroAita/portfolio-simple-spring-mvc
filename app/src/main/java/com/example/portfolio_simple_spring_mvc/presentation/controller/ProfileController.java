package com.example.portfolio_simple_spring_mvc.presentation.controller;

import java.util.UUID;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.portfolio_simple_spring_mvc.application.command.Command;
import com.example.portfolio_simple_spring_mvc.application.command.ProfilesCommand;
import com.example.portfolio_simple_spring_mvc.application.command.ProfileCommand;
import com.example.portfolio_simple_spring_mvc.application.dispatcher.CommandHandlerDispatcher;
import com.example.portfolio_simple_spring_mvc.application.dto.pagination.PaginationDto;
import com.example.portfolio_simple_spring_mvc.application.dto.profile.Birthplace;
import com.example.portfolio_simple_spring_mvc.application.dto.profile.Gender;
import com.example.portfolio_simple_spring_mvc.application.dto.profile.ProfileColumn;
import com.example.portfolio_simple_spring_mvc.application.dto.profile.RequestDto;
import com.example.portfolio_simple_spring_mvc.application.dto.profile.ResponseDto;
import com.example.portfolio_simple_spring_mvc.application.handledResult.HandledResult;
import com.example.portfolio_simple_spring_mvc.infrastructure.framework.util.MessageUtil;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/")
@SessionAttributes({"page", "size", "profileColumn", "asc", "activity", "requestDto"})
public class ProfileController {
    private final CommandHandlerDispatcher dispatcher;
    private final MessageUtil messageUtil;

    public ProfileController(
        CommandHandlerDispatcher dispatcher,
        MessageUtil messageUtil
    ) {
        this.dispatcher = dispatcher;
        this.messageUtil = messageUtil;
    }

    @ModelAttribute("page")
    public int page() {
        return 0;
    }

    @ModelAttribute("size")
    public int size() {
        return 10;
    }

    @ModelAttribute("profileColumn")
    public ProfileColumn profileColumn() {
        return ProfileColumn.PERSONAL_NUMBER;
    }

    @ModelAttribute("asc")
    public boolean asc() {
        return true;
    }

    @ModelAttribute("activity")
    public boolean activity() {
        return true;
    }

    @ModelAttribute("requestDto")
    public RequestDto requestDto() {
        return new RequestDto();
    }

    @ModelAttribute("genders")
    public Gender[] genders() {
        return Gender.values();
    }

    @ModelAttribute("birthplaces")
    public Birthplace[] birthplaces() {
        return Birthplace.values();
    }

    @ModelAttribute("profileColumns")
    public ProfileColumn[] profileColumns() {
        return ProfileColumn.values();
    }

    // 一覧表示
    @GetMapping
    public ModelAndView selectProfiles(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size,
        @RequestParam(defaultValue = "PERSONAL_NUMBER") ProfileColumn profileColumn, 
        @RequestParam(defaultValue = "true") boolean asc,
        @RequestParam(defaultValue = "true") boolean activity,
        Model model
    ) { 
        Command command = new ProfilesCommand.OrderBy(page, size, profileColumn, asc, activity);
        HandledResult<PaginationDto<ResponseDto>> result = dispatcher.dispatch(command);
        
        ModelAndView mav = new ModelAndView("profile/list");
        // 初期化
        model.addAttribute("requestDto", requestDto());
        
        model.addAttribute("page", result.data().currentPage());
        model.addAttribute("size", size);
        model.addAttribute("profileColumn", profileColumn);
        model.addAttribute("asc", asc);
        model.addAttribute("activity", activity);

        mav.addObject("profiles", result.data().content());
        mav.addObject("empty", result.data() == null || !result.data().hasContent());
        mav.addObject("totalPages", result.data().totalPages());
        mav.addObject("totalElements", result.data().totalElements());
        mav.addObject("hasNext", result.data().hasNext());
        mav.addObject("hasPrevious", result.data().hasPrevious());

        return mav;
    }

    //詳細データ表示
    @GetMapping("profile/{id}")
    public ModelAndView selectProfile(
        @PathVariable UUID id
    ) {
        Command command = new ProfileCommand.Select(id);
        HandledResult<RequestDto> result = dispatcher.dispatch(command);

        ModelAndView mav = new ModelAndView("profile/details");
        mav.addObject("profile", result.data());
        return mav;
    }

    //新規登録画面表示
    @GetMapping("profile/new")
    public ModelAndView getNewForm() {
        ModelAndView mav = new ModelAndView("profile/form");
        mav.addObject("requestDto", requestDto());
        mav.addObject("title", "新規登録");
        return mav;
    }

    //新規登録確認画面表示
    @PostMapping("profile/new/confirm")
    public ModelAndView postConfirm(
        @RequestParam(defaultValue = "false") boolean agree, 
        @Valid @ModelAttribute("requestDto") RequestDto requestDto,
        BindingResult bindingResult
    ) {
        ModelAndView mav = new ModelAndView("profile/form");

        if(!agree) {
            mav.addObject("agreeError", messageUtil.getMessage(ControllerMessage.AGREE.getKey()));
            mav.addObject("title", "新規登録");
            return mav;
        }

        if(bindingResult.hasErrors()) {
            mav.addObject("title", "新規登録");
            return mav;
        }

        return new ModelAndView("redirect:/profile/new/confirm");
    }

    //新規登録確認画面をリダイレクトで表示(再送信防止)RPGパターン
    @GetMapping("profile/new/confirm")
    public ModelAndView getConfirm() {
        ModelAndView mav = new ModelAndView("profile/confirm");
        mav.addObject("title", "新規登録確認");
        return mav;
    }

    //新規登録確認画面から新規登録フォームに戻る
    @GetMapping("profile/new/back")
    public ModelAndView backToNewForm() {
        ModelAndView mav = new ModelAndView("profile/form");
        mav.addObject("title", "新規登録");
        return mav;
    }

    //新規登録処理
    @PostMapping("profile/insert")
    public ModelAndView insertProfile(
        @Valid @ModelAttribute("requestDto") RequestDto requestDto,
        BindingResult bindingResult,
        @ModelAttribute("page") int page,
        @ModelAttribute("size") int size,
        @ModelAttribute("profileColumn") ProfileColumn profileColumn,
        @ModelAttribute("asc") boolean asc,
        @ModelAttribute("activity") boolean activity,
        Model model,
        RedirectAttributes redirectAttributes
    ) {
        if(bindingResult.hasErrors()) {
            // 初期化
            model.addAttribute("requestDto", requestDto());

            return new ModelAndView("error")
                    .addObject("title", "400: Bad Request")
                    .addObject("message", "誤った操作です。初めからやり直してください。");
        }

        Command command = new ProfileCommand.Insert(requestDto);
        HandledResult<Void> result = dispatcher.dispatch(command);
        
        // 初期化
        model.addAttribute("requestDto", requestDto());

        redirectAttributes.addFlashAttribute("flashMessage", result.message());
        redirectAttributes.addAttribute("page", page);
        redirectAttributes.addAttribute("size", size);
        redirectAttributes.addAttribute("profileColumn", profileColumn);
        redirectAttributes.addAttribute("asc", asc);
        redirectAttributes.addAttribute("activity", activity);
        return new ModelAndView("redirect:/");
    }

    //編集画表表示
    @GetMapping("profile/{id}/edit")
    public ModelAndView getEditForm(
        @PathVariable UUID id
    ) {
        Command command = new ProfileCommand.Select_ForEdit(id);
        HandledResult<RequestDto> result = dispatcher.dispatch(command);

        ModelAndView mav = new ModelAndView("profile/form");
        mav.addObject("requestDto", result.data());
        mav.addObject("id", id);
        mav.addObject("title", "編集");
        return mav;
    }

    //更新確認画面表示
    @PostMapping("profile/{id}/edit/confirm")
    public ModelAndView postEditConfirm(
        @PathVariable UUID id,
        @RequestParam(defaultValue = "false") boolean agree,
        @Valid @ModelAttribute("requestDto") RequestDto requestDto,
        BindingResult bindingResult,
        RedirectAttributes redirectAttributes
    ) {
        ModelAndView mav = new ModelAndView("profile/form");

        if(!agree) {
            mav.addObject(
                "agreeError",
                messageUtil.getMessage(ControllerMessage.AGREE.getKey())
            );
            mav.addObject("id", id);
            mav.addObject("title", "編集");
            return mav;
        }

        if(bindingResult.hasErrors()) {
            mav.addObject("id", id);
            mav.addObject("title", "編集");
            return mav;
        }

        redirectAttributes.addAttribute("id", id);
        return new ModelAndView("redirect:/profile/{id}/edit/confirm");
    }

    //更新確認画面をリダイレクトで表示(再送信防止)
    @GetMapping("profile/{id}/edit/confirm")
    public ModelAndView getEditConfirm(
        @PathVariable UUID id
    ) {
        ModelAndView mav = new ModelAndView("profile/confirm");
        mav.addObject("id", id);
        mav.addObject("title", "更新確認");
        return mav;
    }

    //更新確認画面から編集フォームに戻る
    @GetMapping("profile/{id}/edit/back")
    public ModelAndView backToEditForm(
        @PathVariable UUID id
    ) {
        ModelAndView mav = new ModelAndView("profile/form");
        mav.addObject("id", id);
        mav.addObject("title", "編集");
        return mav;
    }

    //更新処理
    @PostMapping("profile/{id}/update")
    public ModelAndView updateProfile(
        @PathVariable UUID id,
        @Valid @ModelAttribute("requestDto") RequestDto requestDto,
        BindingResult bindingResult,
        @ModelAttribute("page") int page,
        @ModelAttribute("size") int size,
        @ModelAttribute("profileColumn") ProfileColumn profileColumn,
        @ModelAttribute("asc") boolean asc,
        @ModelAttribute("activity") boolean activity,
        Model model,
        RedirectAttributes redirectAttributes
    ) {
        if(bindingResult.hasErrors()) {
            // 初期化
            model.addAttribute("requestDto", requestDto());

            return new ModelAndView("error")
                    .addObject("title", "400: Bad Request")
                    .addObject("message", "誤った操作です。初めからやり直してください。");
        }

        Command command = new ProfileCommand.Update(id, requestDto);
        HandledResult<Void> result = dispatcher.dispatch(command);

        // 初期化
        model.addAttribute("requestDto", requestDto());

        redirectAttributes.addFlashAttribute("flashMessage", result.message());
        redirectAttributes.addAttribute("id", id);
        redirectAttributes.addAttribute("page", page);
        redirectAttributes.addAttribute("size", size);
        redirectAttributes.addAttribute("profileColumn", profileColumn);
        redirectAttributes.addAttribute("asc", asc);
        redirectAttributes.addAttribute("activity", activity);
        return new ModelAndView("redirect:/profile/{id}");
    }

    //削除処理
    @PostMapping("profile/{id}/delete")
    public ModelAndView deleteProfile(
        @PathVariable UUID id,
        @ModelAttribute("page") int page,
        @ModelAttribute("size") int size,
        @ModelAttribute("profileColumn") ProfileColumn profileColumn,
        @ModelAttribute("asc") boolean asc,
        @ModelAttribute("activity") boolean activity,
        Model model,
        RedirectAttributes redirectAttributes
    ) {
        Command command = new ProfileCommand.Delete(id);
        HandledResult<Void> result = dispatcher.dispatch(command);

        // 初期化
        model.addAttribute("requestDto", requestDto());

        redirectAttributes.addFlashAttribute("flashMessage", result.message());
        redirectAttributes.addAttribute("page", page);
        redirectAttributes.addAttribute("size", size);
        redirectAttributes.addAttribute("profileColumn", profileColumn);
        redirectAttributes.addAttribute("asc", asc);
        redirectAttributes.addAttribute("activity", activity);
        return new ModelAndView("redirect:/");
    }

    // 削除取消
    @PostMapping("profile/{id}/un-deleted")
    public ModelAndView unDeletedProfile(
        @PathVariable UUID id,
        @ModelAttribute("page") int page,
        @ModelAttribute("size") int size,
        @ModelAttribute("profileColumn") ProfileColumn profileColumn,
        @ModelAttribute("asc") boolean asc,
        @ModelAttribute("activity") boolean activity,
        Model model,
        RedirectAttributes redirectAttributes
    ) {
        Command command = new ProfileCommand.UnDeleted(id);
        HandledResult<Void> result = dispatcher.dispatch(command);

        // 初期化
        model.addAttribute("requestDto", requestDto());

        redirectAttributes.addFlashAttribute("flashMessage", result.message());
        redirectAttributes.addAttribute("page", page);
        redirectAttributes.addAttribute("size", size);
        redirectAttributes.addAttribute("profileColumn", profileColumn);
        redirectAttributes.addAttribute("asc", asc);
        redirectAttributes.addAttribute("activity", activity);
        return new ModelAndView("redirect:/");
    }
}