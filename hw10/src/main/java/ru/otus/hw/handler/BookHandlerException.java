package ru.otus.hw.handler;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;
import ru.otus.hw.exception.EntityNotFoundException;

@ControllerAdvice
public class BookHandlerException {

    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ModelAndView exception(EntityNotFoundException e) {
        ModelAndView modelAndView = new ModelAndView("errors/400err");
        modelAndView.addObject("message", e.getMessage());
        return modelAndView;
    }

    @ExceptionHandler(RuntimeException.class)
    public ModelAndView exception(Exception e) {
        ModelAndView modelAndView = new ModelAndView("errors/500err");
        modelAndView.addObject("message", e.getMessage());
        return modelAndView;
    }
}
