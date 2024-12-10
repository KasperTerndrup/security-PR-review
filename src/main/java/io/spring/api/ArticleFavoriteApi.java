package io.spring.api;

import io.spring.api.exception.ResourceNotFoundException;
import io.spring.application.ArticleQueryService;
import io.spring.application.data.ArticleData;
import io.spring.core.article.Article;
import io.spring.core.article.ArticleRepository;
import io.spring.core.favorite.ArticleFavorite;
import io.spring.core.favorite.ArticleFavoriteRepository;
import io.spring.core.user.User;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "articles/{slug}/favorite")
@AllArgsConstructor
public class ArticleFavoriteApi {
    private ArticleFavoriteRepository articleFavoriteRepository;
    private ArticleRepository articleRepository;
    private ArticleQueryService articleQueryService;

    @PostMapping
    public ResponseEntity<ArticleData> favoriteArticle(@PathVariable("slug") String slug, @AuthenticationPrincipal User user) {
        Article article = articleRepository
                .findBySlug(slug)
                .orElseThrow(ResourceNotFoundException::new);

        ArticleFavorite articleFavorite = new ArticleFavorite(article.getId(), user.getId());
        articleFavoriteRepository.save(articleFavorite);

        var articleData = articleQueryService.findBySlug(slug, user);
        return ResponseEntity.of(articleData);
    }

    @DeleteMapping
    public ResponseEntity<ArticleData> unfavoriteArticle(@PathVariable("slug") String slug, @AuthenticationPrincipal User user) {
        Article article = articleRepository
                .findBySlug(slug)
                .orElseThrow(ResourceNotFoundException::new);

        articleFavoriteRepository
                .find(article.getId(), user.getId())
                .ifPresent(articleFavoriteRepository::remove);

        var articleData = articleQueryService.findBySlug(slug, user);
        return ResponseEntity.of(articleData);
    }

}
