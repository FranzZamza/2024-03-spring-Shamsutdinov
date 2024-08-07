package ru.otus.hw.commands;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.otus.hw.converters.CommentConverter;
import ru.otus.hw.service.CommentService;

import java.util.stream.Collectors;

@RequiredArgsConstructor
@ShellComponent
public class CommentCommands {
    private final CommentService commentService;

    private final CommentConverter commentConverter;

    @ShellMethod(value = "Find all comments by book id", key = "cbbid")
    public String findAllCommentsByBookId(String bookId) {
        return commentService.findByBookId(bookId)
                .stream()
                .map(commentConverter::commentToString)
                .collect(Collectors.joining("," + System.lineSeparator()));
    }

    @ShellMethod(value = "Find comment by id", key = "cbid")
    public String findCommentById(String id) {
        return commentService.findById(id)
                .map(commentConverter::commentToString)
                .orElse("Comment with id %s not found".formatted(id));
    }

    @ShellMethod(value = "Add comment to book", key = "cins")
    public String addCommentToBook(String bookId, String text) {
        var comment = commentService.insert(text, bookId);
        return commentConverter.commentToString(comment);
    }

    @ShellMethod(value = "Update comment of book", key = "cupd")
    public String updateComment(String id, String text, String bookId) {
        var comment = commentService.update(id, text, bookId);
        return commentConverter.commentToString(comment);
    }

    @ShellMethod(value = "Delete comment", key = "cdel")
    public void deleteComment(String id) {
        commentService.deleteById(id);
    }
}