package com.example.ccb.advice;

import com.example.ccb.common.BaseResult;
import com.example.ccb.controller.StudentGraduateController;
import com.example.ccb.exception.CustomizeException;
import com.example.ccb.exception.ErrorCode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
@ResponseBody
public class ExceptionHandlerAdvice {


    @ExceptionHandler(Exception.class)
    public BaseResult handleControllerException(HttpServletRequest request, Throwable ex) {
        //HttpStatus status = getStatus(request);
        if (ex instanceof CustomizeException) {
            return BaseResult.failWithException((CustomizeException)ex);
        } else {
            return BaseResult.failWithErrorCode(ErrorCode.INNER_ERROR);
        }
    }

//    private HttpStatus getStatus(HttpServletRequest request) {
//        Integer code = (Integer) request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
//        HttpStatus status = HttpStatus.resolve(code);
//        return (status != null) ? status : HttpStatus.INTERNAL_SERVER_ERROR;
//    }

}