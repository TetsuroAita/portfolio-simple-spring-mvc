package com.example.portfolio_simple_spring_mvc.presentation.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.example.portfolio_simple_spring_mvc.application.dispatcher.CommandHandlerDispatcher;
import com.example.portfolio_simple_spring_mvc.application.dto.pagination.PaginationDto;
import com.example.portfolio_simple_spring_mvc.application.dto.profile.ProfileColumn;
import com.example.portfolio_simple_spring_mvc.application.dto.profile.RequestDto;
import com.example.portfolio_simple_spring_mvc.application.dto.profile.ResponseDto;
import com.example.portfolio_simple_spring_mvc.application.handledResult.HandledResult;
import com.example.portfolio_simple_spring_mvc.domain.exception.ProfileNotFoundException;
import com.example.portfolio_simple_spring_mvc.infrastructure.framework.config.SecurityConfig;
import com.example.portfolio_simple_spring_mvc.infrastructure.framework.util.MessageUtil;

@WebMvcTest(ProfileController.class)
@Import(SecurityConfig.class)
public class ProfileControllerTest {
    @Autowired private MockMvc mockMvc;
    @MockitoBean private CommandHandlerDispatcher dispatcher;
    @MockitoBean private MessageUtil messageUtil;

    private static UUID PROFILE_ID = UUID.randomUUID();
    private int page;
    private int size;
    private ProfileColumn profileColumn;
    private boolean asc;
    private boolean activity;
    private RequestDto requestDto;
    private ResponseDto responseDto;
    private MockHttpSession session;

    @BeforeEach
    void setUp() {
        page = 0;
        size = 10;
        profileColumn = ProfileColumn.PERSONAL_NUMBER;
        asc = true;
        activity = true;
        requestDto = RequestDto.createObjectForTest();
        responseDto = ResponseDto.createObjectForTest();
        session = new MockHttpSession();
        session.setAttribute("page", page);
        session.setAttribute("size", size);
        session.setAttribute("profileColumn", profileColumn);
        session.setAttribute("asc", asc);
        session.setAttribute("activity", activity);
        session.setAttribute("requestDto", requestDto);
    }

    // =================================================
    // プロフィール一覧取得
    // =================================================
    @Test
    @DisplayName("プロフィール一覧取得時、RuntimeException 発生でエラーを返す")
    void test_selectProfiles_thenThrowRuntimeException_returnErrpr() throws Exception {
        when(dispatcher.dispatch(any())).thenThrow(RuntimeException.class);
        when(messageUtil.getMessage(any())).thenReturn("error");

        mockMvc.perform(
            get("/?page=" + page + "&size=" + size + "&profileColumn=" + profileColumn + "&asc=" + asc + "&activity=" + activity)
            .session(session)
        )
        .andExpect(request().sessionAttribute("requestDto", requestDto))
        .andExpect(request().sessionAttribute("page", 0))
        .andExpect(request().sessionAttribute("size", 10))
        .andExpect(request().sessionAttribute("profileColumn", ProfileColumn.PERSONAL_NUMBER))
        .andExpect(request().sessionAttribute("asc", true))
        .andExpect(request().sessionAttribute("activity", true))
        .andExpect(status().isInternalServerError())
        .andExpect(view().name("error"))
        .andExpect(model().attribute("title", "500: Internal Server Error"))
        .andExpect(model().attribute("message", "error"));

        verify(messageUtil).getMessage(any());
        assertThat(session.getAttribute("requestDto")).isEqualTo(requestDto);
    }

    @Test
    @DisplayName("プロフィール一覧取得で empty が空でない、かつ各 SessionAttribute は適切な値であるべき")
    void test_selectProfiles_thenEmptyIsFalse_returnList() throws Exception {
        profileColumn = ProfileColumn.LAST_NAME_KANA;
        asc = false;
        activity = true;
        List<ResponseDto> profiles = List.of(ResponseDto.createObjectForTest());
        PaginationDto<ResponseDto> dtos = new PaginationDto<>(profiles, 0, 1, 1, false, false);
        HandledResult<PaginationDto<ResponseDto>> result = new HandledResult<PaginationDto<ResponseDto>>("success", dtos);
        doReturn(result).when(dispatcher).dispatch(any());

        mockMvc.perform(
            get("/?page=" + page + "&size=" + size + "&profileColumn=" + profileColumn + "&asc=" + asc + "&activity=" + activity)
            .session(session)
        )
        .andExpect(request().sessionAttribute("requestDto", new RequestDto()))
        .andExpect(request().sessionAttribute("page", 0))
        .andExpect(request().sessionAttribute("size", 10))
        .andExpect(request().sessionAttribute("profileColumn", ProfileColumn.LAST_NAME_KANA))
        .andExpect(request().sessionAttribute("asc", false))
        .andExpect(request().sessionAttribute("activity", true))
        .andExpect(status().isOk())
        .andExpect(view().name("profile/list"))
        .andExpect(model().attribute("profiles", profiles))
        .andExpect(model().attribute("empty", false))
        .andExpect(model().attribute("totalPages", 1))
        .andExpect(model().attribute("totalElements", 1L))
        .andExpect(model().attribute("hasNext", false))
        .andExpect(model().attribute("hasPrevious", false));

        verifyNoInteractions(messageUtil);
        assertThat(session.getAttribute("requestDto")).isEqualTo(new RequestDto());
    }

    @Test
    @DisplayName("プロフィール一覧取得で empty が空、かつ各 SessionAttribute は適切な値であるべき")
    void test_selectProfiles_thenEmptyIsTrue_returnList() throws Exception {
        profileColumn = ProfileColumn.GENDER;
        asc = false;
        activity = false;
        PaginationDto<ResponseDto> dtos = new PaginationDto<>(List.of(), 0, 0, 0, false, false);
        HandledResult<PaginationDto<ResponseDto>> result = new HandledResult<PaginationDto<ResponseDto>>("success", dtos);
        doReturn(result).when(dispatcher).dispatch(any());

        mockMvc.perform(
            get("/?page=" + page + "&size=" + size + "&profileColumn=" + profileColumn + "&asc=" + asc + "&activity=" + activity)
            .session(session)
        )
        .andExpect(request().sessionAttribute("requestDto", new RequestDto()))
        .andExpect(request().sessionAttribute("page", 0))
        .andExpect(request().sessionAttribute("size", 10))
        .andExpect(request().sessionAttribute("profileColumn", ProfileColumn.GENDER))
        .andExpect(request().sessionAttribute("asc", false))
        .andExpect(request().sessionAttribute("activity", false))
        .andExpect(status().isOk())
        .andExpect(view().name("profile/list"))
        .andExpect(model().attribute("profiles", List.of()))
        .andExpect(model().attribute("empty", true))
        .andExpect(model().attribute("totalPages", 0))
        .andExpect(model().attribute("totalElements", 0L))
        .andExpect(model().attribute("hasNext", false))
        .andExpect(model().attribute("hasPrevious", false));

        verifyNoInteractions(messageUtil);
        assertThat(session.getAttribute("requestDto")).isEqualTo(new RequestDto());
    }

    // =================================================
    // プロフィール取得
    // =================================================
    @Test
    @DisplayName("プロフィール取得時、ProfileNotFoundException 発生でエラーを返す")
    void test_selectProfile_thenThrowProfileNotFoundException_returnError() throws Exception {
        when(dispatcher.dispatch(any())).thenThrow(ProfileNotFoundException.class);
        when(messageUtil.getMessage(any())).thenReturn("error");

        mockMvc.perform(
            get("/profile/" + PROFILE_ID)
            .session(session)
        )
        .andExpect(request().sessionAttribute("requestDto", requestDto))
        .andExpect(status().isBadRequest())
        .andExpect(view().name("error"))
        .andExpect(model().attribute("title", "400: Bad Request"))
        .andExpect(model().attribute("message", "error"));

        assertThat(session.getAttribute("requestDto")).isEqualTo(requestDto);
    }

    @Test
    @DisplayName("プロフィール取得時、RuntimeException 発生でエラーを返す")
    void test_selectProfile_thenThrowRuntimeException_returnError() throws Exception {
        when(dispatcher.dispatch(any())).thenThrow(RuntimeException.class);
        when(messageUtil.getMessage(any())).thenReturn("error");

        mockMvc.perform(
            get("/profile/" + PROFILE_ID)
            .session(session)
        )
        .andExpect(request().sessionAttribute("requestDto", requestDto))
        .andExpect(status().isInternalServerError())
        .andExpect(view().name("error"))
        .andExpect(model().attribute("title", "500: Internal Server Error"))
        .andExpect(model().attribute("message", "error"));

        assertThat(session.getAttribute("requestDto")).isEqualTo(requestDto);
    }

    @Test
    @DisplayName("プロフィール詳細取得")
    void test_selectProfile_thenSuccess_returnDetails() throws Exception {
        HandledResult<ResponseDto> result = new HandledResult<ResponseDto>("success", responseDto);
        doReturn(result).when(dispatcher).dispatch(any());

        mockMvc.perform(
            get("/profile/" + PROFILE_ID)
            .session(session)
        )
        .andExpect(request().sessionAttribute("requestDto", requestDto))
        .andExpect(status().isOk())
        .andExpect(view().name("profile/details"))
        .andExpect(model().attribute("profile", result.data()));

        verifyNoInteractions(messageUtil);
        assertThat(session.getAttribute("requestDto")).isEqualTo(requestDto);
    }

    // =================================================
    // 新規作成画面〜確認〜新規登録
    // =================================================
    @Test
    @DisplayName("新規登録フォーム画面を取得。フォームが空であること")
    void test_getNewForm_thenSuccess_returnForm() throws Exception {
        assertThat(((RequestDto)session.getAttribute("requestDto")).getLastName()).isEqualTo("山田");

        mockMvc.perform(
            get("/profile/new")
            .session(session)
        )
        .andExpect(request().sessionAttribute("requestDto", new RequestDto()))
        .andExpect(status().isOk())
        .andExpect(view().name("profile/form"))
        .andExpect(model().attribute("title", "新規登録"));

        verifyNoInteractions(dispatcher, messageUtil);
        assertThat(session.getAttribute("requestDto")).isEqualTo(new RequestDto());
    }

    @Test
    @DisplayName("新規登録確認画面移行時、同意にチェックなしでフォームにもどす")
    void test_agreeIsFalse_whenPostConfirm_returnForm() throws Exception {
        when(messageUtil.getMessage(any())).thenReturn("error");

        mockMvc.perform(
            post("/profile/new/confirm")
            .param("agree", "false")
            .session(session)
        )
        .andExpect(request().sessionAttribute("requestDto", requestDto))
        .andExpect(status().isOk())
        .andExpect(view().name("profile/form"))
        .andExpect(model().hasNoErrors())
        .andExpect(model().attribute("agreeError", "error"))
        .andExpect(model().attribute("title", "新規登録"));

        verifyNoInteractions(dispatcher);
        assertThat(session.getAttribute("requestDto")).isEqualTo(requestDto);
    }

    @Test
    @DisplayName("新規登録確認画面移行時、バリデーションエラーでフォームにもどす")
    void test_bindingResultHasError_whenPostConfirm_returnForm() throws Exception {
        requestDto = new RequestDto();
        session.setAttribute("requestDto", requestDto);
        when(messageUtil.getMessage(any())).thenReturn("error");
        
        mockMvc.perform(
            post("/profile/new/confirm")
            .param("agree", "true")
            .session(session)
        )
        .andExpect(request().sessionAttribute("requestDto", requestDto))
        .andExpect(status().isOk())
        .andExpect(view().name("profile/form"))
        .andExpect(model().hasErrors())
        .andExpect(model().attributeDoesNotExist("agreeError"))
        .andExpect(model().attribute("title", "新規登録"));

        verifyNoInteractions(dispatcher);
        assertThat(session.getAttribute("requestDto")).isEqualTo(requestDto);
    }

    @Test
    @DisplayName("新規登録確認画面にリダイレクト")
    void test_postConfirm_thenSuccess_redirectToGetConfirm() throws Exception {
        mockMvc.perform(
            post("/profile/new/confirm")
            .param("agree", "true")
            .session(session)
        )
        .andExpect(request().sessionAttribute("requestDto", requestDto))
        .andExpect(status().is3xxRedirection())
        .andExpect(model().attributeDoesNotExist("agreeError"))
        .andExpect(model().hasNoErrors())
        .andExpect(redirectedUrl("/profile/new/confirm"));

        verifyNoInteractions(dispatcher, messageUtil);
        assertThat(session.getAttribute("requestDto")).isEqualTo(requestDto);
    }

    @Test
    @DisplayName("リダイレクトで新規登録確認画面を表示")
    void test_getConfirm_thenSuccess_returnConfirm() throws Exception {
        mockMvc.perform(
            get("/profile/new/confirm")
            .session(session)
        )
        .andExpect(request().sessionAttribute("requestDto", requestDto))
        .andExpect(status().isOk())
        .andExpect(view().name("profile/confirm"))
        .andExpect(model().attribute("title", "新規登録確認"));

        verifyNoInteractions(dispatcher, messageUtil);
        assertThat(session.getAttribute("requestDto")).isEqualTo(requestDto);
    }

    @Test
    @DisplayName("新規登録確認画面から新規登録フォーム画面に戻る")
    void test_backToNewForm_returnForm() throws Exception {
        mockMvc.perform(
            get("/profile/new/back")
            .session(session)
        )
        .andExpect(request().sessionAttribute("requestDto", requestDto))
        .andExpect(status().isOk())
        .andExpect(view().name("profile/form"))
        .andExpect(model().attribute("title", "新規登録"));

        verifyNoInteractions(dispatcher, messageUtil);
        assertThat(session.getAttribute("requestDto")).isEqualTo(requestDto);
    }

    @Test
    @DisplayName("新規登録処理時、バリデーションエラーでエラー画面を表示")
    void test_bindingResultHasError_whenInsertProfile_returnError() throws Exception {
        requestDto = new RequestDto();
        session.setAttribute("requestDto", requestDto);
        when(messageUtil.getMessage(any())).thenReturn("error");

        mockMvc.perform(
            post("/profile/insert")
            .session(session)
        )
        .andExpect(request().sessionAttribute("requestDto", requestDto))
        .andExpect(status().isOk())
        .andExpect(view().name("error"))
        .andExpect(model().attribute("title", "400: Bad Request"))
        .andExpect(model().attribute("message", "誤った操作です。初めからやり直してください。"));

        verifyNoInteractions(dispatcher);
        assertThat(session.getAttribute("requestDto")).isEqualTo(requestDto);
    }

    @Test
    @DisplayName("新規登録処理成功でルートに移行。 requestDto は初期状態であるべき")
    void test_insertProfile_thenSuccess_redirectToRoot() throws Exception {
        HandledResult<Void> result = new HandledResult<Void>("success", null);
        doReturn(result).when(dispatcher).dispatch(any());
        
        mockMvc.perform(
            post("/profile/insert")
            .session(session)
        )
        .andExpect(request().sessionAttribute("requestDto", new RequestDto()))
        .andExpect(status().is3xxRedirection())
        .andExpect(flash().attribute("flashMessage", result.message()))
        .andExpect(redirectedUrl("/?page=0&size=10&profileColumn=PERSONAL_NUMBER&asc=true&activity=true"));
        
        verifyNoInteractions(messageUtil);
        assertThat(((RequestDto) session.getAttribute("requestDto"))).isEqualTo(new RequestDto());
    }

    // =================================================
    // 編集画面〜確認〜更新
    // =================================================
    @Test
    @DisplayName("プロフィール編集画面を取得時、ProfileNotFoundException 発生でエラー画面を返す")
    void test_getEditForm_thenThrowProfileNotFoundException_returnError() throws Exception {
        when(dispatcher.dispatch(any())).thenThrow(ProfileNotFoundException.class);
        when(messageUtil.getMessage(any())).thenReturn("error");
        
        mockMvc.perform(
            get("/profile/" + PROFILE_ID + "/edit")
            .session(session)
        )
        .andExpect(request().sessionAttribute("requestDto", requestDto))
        .andExpect(status().isBadRequest())
        .andExpect(view().name("error"))
        .andExpect(model().attribute("title", "400: Bad Request"))
        .andExpect(model().attribute("message", "error"));
        
        assertThat(session.getAttribute("requestDto")).isEqualTo(requestDto);
    }
    
    @Test
    @DisplayName("プロフィール編集画面を取得時、RuntimeException 発生でエラー画面を返す")
    void test_getEditForm_thenThrowAnyException_returnError() throws Exception {
        when(dispatcher.dispatch(any())).thenThrow(RuntimeException.class);
        when(messageUtil.getMessage(any())).thenReturn("error");
        
        mockMvc.perform(
            get("/profile/" + PROFILE_ID + "/edit")
            .session(session)
        )
        .andExpect(request().sessionAttribute("requestDto", requestDto))
        .andExpect(status().isInternalServerError())
        .andExpect(view().name("error"))
        .andExpect(model().attribute("title", "500: Internal Server Error"))
        .andExpect(model().attribute("message", "error"));
        
        assertThat(session.getAttribute("requestDto")).isEqualTo(requestDto);
    }
    
    @Test
    @DisplayName("プロフィール編集画面を取得。フォームが適切な値で埋められていること")
    void test_getEditForm_thenSuccess_returnForm() throws Exception {
        HandledResult<RequestDto> result = new HandledResult<RequestDto>("success", requestDto);
        doReturn(result).when(dispatcher).dispatch(any());

        mockMvc.perform(
            get("/profile/" + PROFILE_ID + "/edit")
            .session(session)
        )
        .andExpect(request().sessionAttribute("requestDto", result.data()))
        .andExpect(status().isOk())
        .andExpect(view().name("profile/form"))
        .andExpect(model().attribute("id", PROFILE_ID))
        .andExpect(model().attribute("title", "編集"));

        verifyNoInteractions(messageUtil);
        assertThat(session.getAttribute("requestDto")).isEqualTo(result.data());
    }

    @Test
    @DisplayName("更新確認画面移行時、同意にチェックなしでフォームにもどす")
    void test_agreeIsFalse_whenPostEditConfirm_returnForm() throws Exception {
        when(messageUtil.getMessage(any())).thenReturn("error");
        
        mockMvc.perform(
            post("/profile/" + PROFILE_ID + "/edit/confirm")
            .param("agree", "false")
            .session(session)
        )
        .andExpect(request().sessionAttribute("requestDto", requestDto))
        .andExpect(status().isOk())
        .andExpect(view().name("profile/form"))
        .andExpect(model().hasNoErrors())
        .andExpect(model().attribute("agreeError", "error"))
        .andExpect(model().attribute("id", PROFILE_ID))
        .andExpect(model().attribute("title", "編集"));

        verifyNoInteractions(dispatcher);
        assertThat(session.getAttribute("requestDto")).isEqualTo(requestDto);
    }

    @Test
    @DisplayName("更新確認画面移行時、バリデーションエラーでフォームにもどす")
    void test_bindingResultHasError_whenPostEditConfirm_returnForm() throws Exception {
        requestDto = new RequestDto();
        session.setAttribute("requestDto", requestDto);
        when(messageUtil.getMessage(any())).thenReturn("error");
        
        mockMvc.perform(
            post("/profile/" + PROFILE_ID + "/edit/confirm")
            .param("agree", "true")
            .session(session)
        )
        .andExpect(request().sessionAttribute("requestDto", requestDto))
        .andExpect(status().isOk())
        .andExpect(view().name("profile/form"))
        .andExpect(model().hasErrors())
        .andExpect(model().attributeDoesNotExist("agreeError"))
        .andExpect(model().attribute("id", PROFILE_ID))
        .andExpect(model().attribute("title", "編集"));

        verifyNoInteractions(dispatcher);
        assertThat(session.getAttribute("requestDto")).isEqualTo(requestDto);
    }

    @Test
    @DisplayName("更新確認画面にリダイレクト")
    void test_postConfirm_thenSuccess_redirectToGetEditConfirm() throws Exception {
        mockMvc.perform(
            post("/profile/" + PROFILE_ID + "/edit/confirm")
            .param("agree", "true")
            .session(session)
        )
        .andExpect(request().sessionAttribute("requestDto", requestDto))
        .andExpect(status().is3xxRedirection())
        .andExpect(model().attributeDoesNotExist("agreeError"))
        .andExpect(model().hasNoErrors())
        .andExpect(redirectedUrl("/profile/" + PROFILE_ID + "/edit/confirm"));

        verifyNoInteractions(dispatcher, messageUtil);
        assertThat(session.getAttribute("requestDto")).isEqualTo(requestDto);
    }

    @Test
    @DisplayName("リダイレクトで更新確認画面を表示")
    void test_getEditConfirm_thenSuccess_returnConfirm() throws Exception {
        mockMvc.perform(
            get("/profile/" + PROFILE_ID + "/edit/confirm")
            .session(session)
        )
        .andExpect(request().sessionAttribute("requestDto", requestDto))
        .andExpect(status().isOk())
        .andExpect(view().name("profile/confirm"))
        .andExpect(model().attribute("id", PROFILE_ID))
        .andExpect(model().attribute("title", "更新確認"));

        verifyNoInteractions(dispatcher, messageUtil);
        assertThat(session.getAttribute("requestDto")).isEqualTo(requestDto);
    }

    @Test
    @DisplayName("更新確認画面から編集フォーム画面に戻る")
    void test_backToEditForm_returnForm() throws Exception {
        mockMvc.perform(
            get("/profile/" + PROFILE_ID + "/edit/back")
            .session(session)
        )
        .andExpect(request().sessionAttribute("requestDto", requestDto))
        .andExpect(status().isOk())
        .andExpect(view().name("profile/form"))
        .andExpect(model().attribute("id", PROFILE_ID))
        .andExpect(model().attribute("title", "編集"));

        verifyNoInteractions(dispatcher, messageUtil);
        assertThat(session.getAttribute("requestDto")).isEqualTo(requestDto);
    }

    @Test
    @DisplayName("更新処理時、バリデーションエラーでエラー画面を表示")
    void test_bindingResultHasError_whenUpdateProfile_returnError() throws Exception {
        requestDto = new RequestDto();
        session.setAttribute("requestDto", requestDto);
        when(messageUtil.getMessage(any())).thenReturn("error");

        mockMvc.perform(
            post("/profile/" + PROFILE_ID + "/update")
            .session(session)
        )
        .andExpect(request().sessionAttribute("requestDto", requestDto))
        .andExpect(status().isOk())
        .andExpect(view().name("error"))
        .andExpect(model().attribute("title", "400: Bad Request"))
        .andExpect(model().attribute("message", "誤った操作です。初めからやり直してください。"));

        verifyNoInteractions(dispatcher);
        assertThat(((RequestDto) session.getAttribute("requestDto"))).isEqualTo(requestDto);
    }

    @Test
    @DisplayName("更新処理成功で更新元のプロフィール詳細画面に移行。 requestDto は初期状態であるべき")
    void test_updateProfile_thenSuccessed_returnDetails() throws Exception {
        HandledResult<Void> result = new HandledResult<Void>("success", null);
        doReturn(result).when(dispatcher).dispatch(any());
        
        mockMvc.perform(
            post("/profile/" + PROFILE_ID + "/update")
            .session(session)
        )
        .andExpect(request().sessionAttribute("requestDto", new RequestDto()))
        .andExpect(status().is3xxRedirection())
        .andExpect(flash().attribute("flashMessage", result.message()))
        .andExpect(redirectedUrl("/profile/" + PROFILE_ID + "?page=0&size=10&profileColumn=PERSONAL_NUMBER&asc=true&activity=true"));
        
        verifyNoInteractions(messageUtil);
        assertThat(((RequestDto) session.getAttribute("requestDto"))).isEqualTo(new RequestDto());
    }

    // =================================================
    // プロフィール削除
    // =================================================
    @Test
    @DisplayName("プロフィール削除時、ProfileNotFoundException 発生でエラーを返す")
    void test_deleteProfile_thenThrowProfileNotFoundException_returnError() throws Exception {
        when(dispatcher.dispatch(any())).thenThrow(ProfileNotFoundException.class);
        when(messageUtil.getMessage(any())).thenReturn("error");

        mockMvc.perform(
            post("/profile/" + PROFILE_ID + "/delete")
            .session(session)
        )
        .andExpect(request().sessionAttribute("requestDto", requestDto))
        .andExpect(status().isBadRequest())
        .andExpect(view().name("error"))
        .andExpect(model().attribute("title", "400: Bad Request"))
        .andExpect(model().attribute("message", "error"));

        assertThat(session.getAttribute("requestDto")).isEqualTo(requestDto);
    }

    @Test
    @DisplayName("プロフィール削除時、RuntimeException 発生でエラーを返す")
    void test_deleteProfile_thenThrowRuntimeException_returnError() throws Exception {
        when(dispatcher.dispatch(any())).thenThrow(RuntimeException.class);
        when(messageUtil.getMessage(any())).thenReturn("error");

        mockMvc.perform(
            post("/profile/" + PROFILE_ID + "/delete")
            .session(session)
        )
        .andExpect(request().sessionAttribute("requestDto", requestDto))
        .andExpect(status().isInternalServerError())
        .andExpect(view().name("error"))
        .andExpect(model().attribute("title", "500: Internal Server Error"))
        .andExpect(model().attribute("message", "error"));

        assertThat(session.getAttribute("requestDto")).isEqualTo(requestDto);
    }

    @Test
    @DisplayName("削除成功でルートに移行。requestDto は初期状態であるべき")
    void test_deleteProfile_thenSuccess_redirectToRoot() throws Exception {
        HandledResult<Void> result = new HandledResult<Void>("success", null);
        doReturn(result).when(dispatcher).dispatch(any());

        mockMvc.perform(
            post("/profile/" + PROFILE_ID + "/delete")
            .session(session)
        )
        .andExpect(request().sessionAttribute("requestDto", new RequestDto()))
        .andExpect(status().is3xxRedirection())
        .andExpect(flash().attribute("flashMessage", result.message()))
        .andExpect(redirectedUrl("/?page=0&size=10&profileColumn=PERSONAL_NUMBER&asc=true&activity=true"));

        verifyNoInteractions(messageUtil);
        assertThat(session.getAttribute("requestDto")).isEqualTo(new RequestDto());
    }

    // =================================================
    // プロフィール削除取消
    // =================================================
    @Test
    @DisplayName("プロフィール削除取消時、ProfileNotFoundException 発生でエラーを返す")
    void test_unDeletedProfile_throwProfileNotFoundException_returnError() throws Exception {
        when(dispatcher.dispatch(any())).thenThrow(ProfileNotFoundException.class);
        when(messageUtil.getMessage(any())).thenReturn("error");

        mockMvc.perform(
            post("/profile/" + PROFILE_ID + "/un-deleted")
            .session(session)
        )
        .andExpect(request().sessionAttribute("requestDto", requestDto))
        .andExpect(status().isBadRequest())
        .andExpect(view().name("error"))
        .andExpect(model().attribute("title", "400: Bad Request"))
        .andExpect(model().attribute("message", "error"));

        assertThat(session.getAttribute("requestDto")).isEqualTo(requestDto);
    }

    @Test
    @DisplayName("プロフィール削除取消時、RuntimeException 発生でエラーを返す")
    void test_unDeletedProfile_thenThrowRuntimeException_returnError() throws Exception {
        when(dispatcher.dispatch(any())).thenThrow(RuntimeException.class);
        when(messageUtil.getMessage(any())).thenReturn("error");

        mockMvc.perform(
            post("/profile/" + PROFILE_ID + "/un-deleted")
            .session(session)
        )
        .andExpect(request().sessionAttribute("requestDto", requestDto))
        .andExpect(status().isInternalServerError())
        .andExpect(view().name("error"))
        .andExpect(model().attribute("title", "500: Internal Server Error"))
        .andExpect(model().attribute("message", "error"));

        assertThat(session.getAttribute("requestDto")).isEqualTo(requestDto);
    }

    @Test
    @DisplayName("削除取消成功でルートに移行。requestDto は初期状態であるべき")
    void test_unDeletedProfile_thenSuccess_redirectToRoot() throws Exception {
        HandledResult<Void> result = new HandledResult<Void>("success", null);
        doReturn(result).when(dispatcher).dispatch(any());

        mockMvc.perform(
            post("/profile/" + PROFILE_ID + "/un-deleted")
            .session(session)
        )
        .andExpect(request().sessionAttribute("requestDto", new RequestDto()))
        .andExpect(status().is3xxRedirection())
        .andExpect(flash().attribute("flashMessage", result.message()))
        .andExpect(redirectedUrl("/?page=0&size=10&profileColumn=PERSONAL_NUMBER&asc=true&activity=true"));

        verifyNoInteractions(messageUtil);
        assertThat(session.getAttribute("requestDto")).isEqualTo(new RequestDto());
    }
}
