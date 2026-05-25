package com.example.portfolio_simple_spring_mvc.presentation.advice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import com.example.portfolio_simple_spring_mvc.domain.exception.DomainIllegalStateException;
import com.example.portfolio_simple_spring_mvc.domain.exception.ProfileNotFoundException;
import com.example.portfolio_simple_spring_mvc.infrastructure.exception.InfrastructureException;
import com.example.portfolio_simple_spring_mvc.infrastructure.framework.util.MessageUtil;

import jakarta.servlet.http.HttpServletResponse;

//あくまでコントローラーから出るエラーの範囲までしか拾えない
@Order(Ordered.LOWEST_PRECEDENCE)
@ControllerAdvice
public class GlobalExceptionHandler {
    private final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    private final MessageUtil messageUtil;

    public GlobalExceptionHandler(MessageUtil messageUtil) {
        this.messageUtil = messageUtil;
    }

    @ExceptionHandler(
        ProfileNotFoundException.class
    )
    public ModelAndView handleProfileNotFoundException(
        ProfileNotFoundException e,
        HttpServletResponse response
    ) {
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        ModelAndView mav = new ModelAndView("error");
        mav.addObject(
            "title", 
            "400: Bad Request"
        );
        mav.addObject(
            "message", 
            messageUtil.getMessage(e.getMessage())
        );
        return mav;
    }

    @ExceptionHandler(
        NoResourceFoundException.class
    )
    public ModelAndView handleNoResourcrFoundException(
        NoResourceFoundException e,
        HttpServletResponse response
    ) {
        response.setStatus(HttpStatus.NOT_FOUND.value());
        ModelAndView mav = new ModelAndView("error");
        mav.addObject(
            "title", 
            "404: Not Found"
        );
        mav.addObject(
            "message", 
            messageUtil.getMessage(AdviceMessage.NOT_FOUND_404.getKey())
        );
        return mav;
    }

    @ExceptionHandler({
        DomainIllegalStateException.class,
        InfrastructureException.class,
        Exception.class
    })
    public ModelAndView handleUnexpected(
        Exception e,
        HttpServletResponse response
    ) {
        logger.error("予期せぬ例外が発生しました。", e);
        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        ModelAndView mav = new ModelAndView("error");
        mav.addObject(
            "title", 
            "500: Internal Server Error"
        );
        mav.addObject(
            "message", 
            messageUtil.getMessage(AdviceMessage.INTERNAL_SERVER_ERROR_500.getKey())
        );
        return mav;
    }
}
