package ru.netology.controller;

import com.google.gson.Gson;

import ru.netology.model.Post;
import ru.netology.service.PostService;


import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Reader;

public class PostController {
    public static final String APPLICATION_JSON = "application/json";
    private final PostService service;
    private final Gson gson = new Gson();

    public PostController(PostService service) {
        this.service = service;
    }

    private void setResponseHeaders(HttpServletResponse response) {
        response.setContentType(APPLICATION_JSON);
    }

    public void all(HttpServletResponse response) throws IOException {
        setResponseHeaders(response);
        final var data = service.all();
        response.getWriter().print(gson.toJson(data));
    }

    public void getById(long id, HttpServletResponse response) throws IOException {
        setResponseHeaders(response);
        final var post = service.getById(id);

        if (post != null) {
            response.getWriter().print(gson.toJson(post));
        } else {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND); // 404 Not Found
        }
    }

    public void save(Reader body, HttpServletResponse response) throws IOException {
        setResponseHeaders(response);
        Post post;

        try {
            post = gson.fromJson(body, Post.class);
            if (post == null || post.getContent() == null) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST); // 400 Bad Request
                response.getWriter().write("Invalid post data.");
                return;
            }
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST); // 400 Bad Request
            response.getWriter().write("Invalid JSON format.");
            return;
        }

        final var savedPost = service.save(post);
        response.setStatus(HttpServletResponse.SC_CREATED); // 201 Created
        response.getWriter().print(gson.toJson(savedPost));
    }

    public void removeById(long id, HttpServletResponse response) throws IOException {
        setResponseHeaders(response);
        try {
            service.removeById(id);
            response.setStatus(HttpServletResponse.SC_NO_CONTENT); // 204 No Content
        } catch (IllegalArgumentException e) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND); // 404 Not Found
        }
    }
}