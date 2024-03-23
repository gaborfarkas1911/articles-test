package com.article.backend;

import com.article.backend.controller.ArticleController;
import com.article.backend.handler.CustomExceptionHandler;
import com.article.backend.model.Article;
import com.article.backend.model.ArticleTag;
import com.article.backend.model.enums.ArticleCategory;
import com.article.backend.model.enums.ArticleStatus;
import com.article.backend.service.ArticleService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@Import(ArticleController.class)
@ContextConfiguration(classes = {ArticleService.class, CustomExceptionHandler.class})
@WebMvcTest(controllers = ArticleController.class, excludeAutoConfiguration = {SecurityAutoConfiguration.class})
@ExtendWith(MockitoExtension.class)
class ArticleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ArticleService articleService;

    @Autowired
    private ObjectMapper objectMapper;

    private Article emptyArticle;
    private Article validArticleRequest;
    private Article validArticleResponse;
    private Article validArticleResponseWithTag;

    @BeforeEach
    public void init() {
        emptyArticle = new Article();

        validArticleRequest = new Article();
        validArticleRequest.setTitle("title");
        validArticleRequest.setSubTitle("subTitle");
        validArticleRequest.setContent("content");
        validArticleRequest.setCategory(ArticleCategory.SPORT);

        validArticleResponse = new Article();
        validArticleResponse.setId(1L);
        validArticleResponse.setTitle("title");
        validArticleResponse.setSubTitle("subTitle");
        validArticleResponse.setContent("content");
        validArticleResponse.setCategory(ArticleCategory.SPORT);
        validArticleResponse.setStatus(ArticleStatus.APPROVED);
        validArticleResponse.setTags(new ArrayList<>());
        validArticleResponse.setImages(new ArrayList<>());

        validArticleResponseWithTag = new Article();
        validArticleResponseWithTag.setId(2L);
        validArticleResponseWithTag.setTitle("title");
        validArticleResponseWithTag.setSubTitle("subTitle");
        validArticleResponseWithTag.setContent("content");
        validArticleResponseWithTag.setCategory(ArticleCategory.SPORT);
        validArticleResponseWithTag.setStatus(ArticleStatus.APPROVED);

        ArticleTag validArticleTag = new ArticleTag();
        validArticleTag.setId(1L);
        validArticleTag.setArticle(validArticleResponseWithTag);
        validArticleTag.setTag("#tag");

        validArticleResponseWithTag.setTags(Collections.singletonList(validArticleTag));
        validArticleResponseWithTag.setImages(new ArrayList<>());
    }

    @Test
    void saveArticle_WhenEmptyRequest_ExpectedBadRequest() throws Exception {
        ResultActions response = mockMvc.perform(post("/article")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(emptyArticle)));
        response.andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void saveArticle_WhenArticleIsValidAndIdIsPresent_ExpectedValidationExceptionAndBadRequestStatus() throws Exception {
        validArticleRequest.setId(1L);
        ResultActions response = mockMvc.perform(post("/article")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validArticleRequest)));
        response.andExpect(result -> assertTrue(result.getResolvedException() instanceof ValidationException))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void saveArticle_WhenTitleIsNull_ExpectedBadRequest() throws Exception {
        validArticleRequest.setTitle(null);
        ResultActions response = mockMvc.perform(post("/article")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validArticleRequest)));
        response.andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void saveArticle_WhenSubTitleIsNull_ExpectedBadRequest() throws Exception {
        validArticleRequest.setSubTitle(null);
        ResultActions response = mockMvc.perform(post("/article")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validArticleRequest)));
        response.andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void saveArticle_WhenContentIsNull_ExpectedBadRequest() throws Exception {
        validArticleRequest.setContent(null);
        ResultActions response = mockMvc.perform(post("/article")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validArticleRequest)));
        response.andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void saveArticle_WhenCategoryIsNull_ExpectedBadRequest() throws Exception {
        validArticleRequest.setCategory(null);
        ResultActions response = mockMvc.perform(post("/article")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validArticleRequest)));
        response.andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void saveArticle_WhenStatusIsNull_ExpectedOk() throws Exception {
        ResultActions response = mockMvc.perform(post("/article")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validArticleRequest)));
        response.andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void saveArticle_WhenArticleIsValid_ExpectedOk() throws Exception {
        ResultActions response = mockMvc.perform(post("/article")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validArticleRequest)));
        response.andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void updateArticleStatus_WhenArticleByIdNotFound_ExpectedEntityNotFoundExceptionWithNotFoundStatus() throws Exception {
        when(articleService.getArticleById(1L)).thenReturn(null);
        ResultActions response = mockMvc.perform(put("/article/update-status")
                .contentType(MediaType.APPLICATION_JSON)
                .param("id", String.valueOf(1L))
                .param("status", ArticleStatus.APPROVED.toString()));

        response.andExpect(result -> assertTrue(result.getResolvedException() instanceof EntityNotFoundException))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void updateArticleStatus_WhenThenGivenIdIsNull_ExpectedBadRequest() throws Exception {
        when(articleService.getArticleById(1L)).thenReturn(validArticleResponse);
        ResultActions response = mockMvc.perform(put("/article/update-status")
                .contentType(MediaType.APPLICATION_JSON)
                .param("status", ArticleStatus.APPROVED.toString()));
        response.andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void updateArticleStatus_WhenThenGivenStatusIsNull_ExpectedBadRequest() throws Exception {
        when(articleService.getArticleById(1L)).thenReturn(validArticleResponse);
        ResultActions response = mockMvc.perform(put("/article/update-status")
                .contentType(MediaType.APPLICATION_JSON)
                .param("id", String.valueOf(1L)));
        response.andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void updateArticleStatus_WhenArticleIsValid_ExpectedOk() throws Exception {
        when(articleService.getArticleById(1L)).thenReturn(validArticleResponse);
        ResultActions response = mockMvc.perform(put("/article/update-status")
                .contentType(MediaType.APPLICATION_JSON)
                .param("id", String.valueOf(1L))
                .param("status", ArticleStatus.APPROVED.toString()));
        response.andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void search_WhenEmptyRequest_ExpectedOk() throws Exception {
        when(articleService.findAll(null, null, ArticleStatus.APPROVED, null))
                .thenReturn(Collections.singletonList(validArticleResponse));

        String expected = "[{\"id\":1,\"title\":\"title\",\"subTitle\":\"subTitle\",\"category\":\"SPORT\",\"tag\":null}]";

        ResultActions response = mockMvc.perform(get("/article/search")
                .contentType(MediaType.APPLICATION_JSON));
        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().string(expected));
    }

    @Test
    void search_WhenValidRequest_ExpectedOkAndMatchingContent() throws Exception {
        when(articleService.findAll("title", ArticleCategory.SPORT, ArticleStatus.APPROVED, "#tag"))
                .thenReturn(Collections.singletonList(validArticleResponseWithTag));

        String expectedContent = "[{\"id\":2,\"title\":\"title\",\"subTitle\":\"subTitle\",\"category\":\"SPORT\",\"tag\":{\"id\":1,\"tag\":\"#tag\"}}]";

        ResultActions response = mockMvc.perform(get("/article/search")
                .contentType(MediaType.APPLICATION_JSON)
                .param("title", "title")
                .param("category", ArticleCategory.SPORT.toString())
                .param("tag", "#tag"));
        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().string(expectedContent));
    }

    @Test
    void search_WhenTheGivenCategoryFound_ExpectedOkAndMatchingContent() throws Exception {
        when(articleService.findAll(null, ArticleCategory.SPORT, ArticleStatus.APPROVED, null))
                .thenReturn(Collections.singletonList(validArticleResponse));

        String expected = "[{\"id\":1,\"title\":\"title\",\"subTitle\":\"subTitle\",\"category\":\"SPORT\",\"tag\":null}]";

        ResultActions response = mockMvc.perform(get("/article/search")
                .contentType(MediaType.APPLICATION_JSON)
                .param("category", ArticleCategory.SPORT.toString()));
        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().string(expected));
    }

    @Test
    void search_WhenTheGivenCategoryNotFound_ExpectedOkAndMatchingContent() throws Exception {
        when(articleService.findAll(null, ArticleCategory.SPORT, ArticleStatus.APPROVED, null))
                .thenReturn(Collections.singletonList(validArticleResponse));

        ResultActions response = mockMvc.perform(get("/article/search")
                .contentType(MediaType.APPLICATION_JSON)
                .param("category", ArticleCategory.TRAVEL.toString()));
        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().string("[]"));
    }

    @Test
    void search_WhenTheGivenTagFound_ExpectedOkAndMatchingContent() throws Exception {
        when(articleService.findAll(null, null, ArticleStatus.APPROVED, "#tag"))
                .thenReturn(Collections.singletonList(validArticleResponseWithTag));

        String expectedContent = "[{\"id\":2,\"title\":\"title\",\"subTitle\":\"subTitle\",\"category\":\"SPORT\",\"tag\":{\"id\":1,\"tag\":\"#tag\"}}]";

        ResultActions response = mockMvc.perform(get("/article/search")
                .contentType(MediaType.APPLICATION_JSON)
                .param("tag", "#tag"));
        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().string(expectedContent));
    }

    @Test
    void search_WhenTheGivenTagNotFound_ExpectedOkAndMatchingContent() throws Exception {
        when(articleService.findAll(null, null, ArticleStatus.APPROVED, "#tag"))
                .thenReturn(Collections.singletonList(validArticleResponseWithTag));

        ResultActions response = mockMvc.perform(get("/article/search")
                .contentType(MediaType.APPLICATION_JSON)
                .param("tag", "#tg"));
        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().string("[]"));
    }

    @Test
    void search_WhenTheGivenTagIsIncorrect_ExpectedValidationExceptionWithBadRequestStatus() throws Exception {
        ResultActions response = mockMvc.perform(get("/article/search")
                .contentType(MediaType.APPLICATION_JSON)
                .param("tag", "tag"));
        response.andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ValidationException));
    }

    @Test
    void search_WhenTheGivenTitleFound_ExpectedOkAndMatchingContent() throws Exception {
        when(articleService.findAll("title", null, ArticleStatus.APPROVED, null))
                .thenReturn(Collections.singletonList(validArticleResponse));

        String expected = "[{\"id\":1,\"title\":\"title\",\"subTitle\":\"subTitle\",\"category\":\"SPORT\",\"tag\":null}]";

        ResultActions response = mockMvc.perform(get("/article/search")
                .contentType(MediaType.APPLICATION_JSON)
                .param("title", "title"));
        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().string(expected));
    }

    @Test
    void search_WhenTheGivenTitleNotFound_ExpectedOkAndMatchingContent() throws Exception {
        when(articleService.findAll("title", null, ArticleStatus.APPROVED, null))
                .thenReturn(Collections.singletonList(validArticleResponse));

        ResultActions response = mockMvc.perform(get("/article/search")
                .contentType(MediaType.APPLICATION_JSON)
                .param("title", "a"));
        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().string("[]"));
    }

    @Test
    void search_WhenTheGivenTitleFoundAndCategoryNotFound_ExpectedOkAndMatchingContent() throws Exception {
        when(articleService.findAll("title", ArticleCategory.SPORT, ArticleStatus.APPROVED, null))
                .thenReturn(Collections.singletonList(validArticleResponse));

        ResultActions response = mockMvc.perform(get("/article/search")
                .contentType(MediaType.APPLICATION_JSON)
                .param("title", "title")
                .param("category", ArticleCategory.TRAVEL.toString()));
        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().string("[]"));
    }

    @Test
    void search_WhenTheGivenTagFoundAndTitleNotFound_ExpectedOkAndMatchingContent() throws Exception {
        when(articleService.findAll("title", ArticleCategory.SPORT, ArticleStatus.APPROVED, "#tag"))
                .thenReturn(Collections.singletonList(validArticleResponseWithTag));

        ResultActions response = mockMvc.perform(get("/article/search")
                .contentType(MediaType.APPLICATION_JSON)
                .param("title", "a")
                .param("tag", "#tag"));
        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().string("[]"));
    }

    @Test
    void search_WhenTheGivenTagAndTitleFound_ExpectedOkAndMatchingContent() throws Exception {
        when(articleService.findAll("title", null, ArticleStatus.APPROVED, "#tag"))
                .thenReturn(Collections.singletonList(validArticleResponseWithTag));

        String expectedContent = "[{\"id\":2,\"title\":\"title\",\"subTitle\":\"subTitle\",\"category\":\"SPORT\",\"tag\":{\"id\":1,\"tag\":\"#tag\"}}]";

        ResultActions response = mockMvc.perform(get("/article/search")
                .contentType(MediaType.APPLICATION_JSON)
                .param("title", "title")
                .param("tag", "#tag"));
        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().string(expectedContent));
    }

    @Test
    void search_WhenTheGivenTitleFoundAndTagIsIncorrect_ExpectedValidationExceptionAndBadRequestStatus() throws Exception {
        when(articleService.findAll("title", null, ArticleStatus.APPROVED, "#tag"))
                .thenReturn(Collections.singletonList(validArticleResponseWithTag));

        ResultActions response = mockMvc.perform(get("/article/search")
                .contentType(MediaType.APPLICATION_JSON)
                .param("title", "title")
                .param("tag", "a"));
        response.andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ValidationException));
    }
}
