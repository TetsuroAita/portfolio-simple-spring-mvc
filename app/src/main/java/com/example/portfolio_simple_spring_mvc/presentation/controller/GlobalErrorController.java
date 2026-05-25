package com.example.portfolio_simple_spring_mvc.presentation.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import com.example.portfolio_simple_spring_mvc.infrastructure.framework.util.MessageUtil;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;

//すべての例外を拾う最終的な場所
@Controller
public class GlobalErrorController implements ErrorController {
    private final Logger logger = LoggerFactory.getLogger(GlobalErrorController.class);
    private final MessageUtil messageUtil;

    public GlobalErrorController(MessageUtil messageUtil) {
        this.messageUtil = messageUtil;
    }

    @GetMapping("/error")
    public ModelAndView handleError(HttpServletRequest httpServletRequest) {
        Integer statusCode = (Integer) httpServletRequest.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        Throwable throwable = (Throwable) httpServletRequest.getAttribute(RequestDispatcher.ERROR_EXCEPTION);

        logger.error("StatusCode={}, Throwable={}", statusCode, throwable);

        ModelAndView mav = new ModelAndView("error");
        mav.addObject("title", "500: Internal Server Error");
        mav.addObject("message", messageUtil.getMessage(ControllerMessage.INTERNAL_SERVER_ERROR_500.getKey()));
        return mav;
    }
}
