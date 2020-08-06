package com.zen.model.manage.common.ResponseEncoder;


import brave.Tracer;
import com.zen.model.manage.common.exception.ValidateException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.PrintWriter;
import java.io.StringWriter;



/**
 * @Author: morris
 * @Date: 2020/6/11 15:41
 * @description
 * @reviewer
 */

@Aspect
@Component
public class ControllerResponseEncoder {
  @Autowired
  Tracer tracer;

  private static Logger LOG = LoggerFactory.getLogger(ControllerResponseEncoder.class);

  @Around(" execution(* com.zen.model.manage..*Controller.*(..)) ")
  public Response wrapResultAndException(ProceedingJoinPoint joinPoint) {
    Object[] args = joinPoint.getArgs();

    Response response = new Response();

    try {
      Object result = joinPoint.proceed(args);
      response.setData(result);
      response.setCodeAndMsg(ResponseStatus.SUCCESS);
      response.setTraceId(tracer.currentSpan().context().traceIdString());
    }catch (ValidateException e) {
      response.setErrorCode(e.getCode());
      response.setErrorMsg(e.getMessage());
      StringWriter writer = new StringWriter();
      PrintWriter printer = new PrintWriter(writer);
      e.printStackTrace(printer);
      LOG.error(writer.toString());
      e.printStackTrace();
    }catch (Exception e) {
      response.setErrorCode(ResponseStatus.SYSTEM_NEXCEPTION.getCode());
      response.setErrorMsg(ResponseStatus.SYSTEM_NEXCEPTION.getStandardMessage());
      StringWriter writer = new StringWriter();
      PrintWriter printer = new PrintWriter(writer);
      e.printStackTrace(printer);
      LOG.error(writer.toString());
      e.printStackTrace();
    } catch (Throwable e) {
      response.setErrorCode(ResponseStatus.SYSTEM_NEXCEPTION.getCode());
      response.setErrorMsg(ResponseStatus.SYSTEM_NEXCEPTION.getStandardMessage());
      StringWriter writer = new StringWriter();
      PrintWriter printer = new PrintWriter(writer);
      e.printStackTrace(printer);
      LOG.error(writer.toString());
      e.printStackTrace();
    }
    return response;
  }
}
