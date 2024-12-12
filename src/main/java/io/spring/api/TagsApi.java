package io.spring.api;

import io.spring.api.exception.ResourceNotFoundException;
import io.spring.application.TagsQueryService;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import io.spring.core.article.Article;
import io.spring.core.article.ArticleRepository;
import io.spring.core.article.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "tags")
@AllArgsConstructor
public class TagsApi {
    private final ArticleRepository articleRepository;
    private TagsQueryService tagsQueryService;

  @GetMapping
  public ResponseEntity getTags() {
    return ResponseEntity.ok(
        new HashMap<String, Object>() {
          {
            put("tags", tagsQueryService.allTags());
          }
        });
  }

  @PutMapping(path = "{articleId}")
  public ResponseEntity<?> addTags(@PathVariable String articleId, List<String> tags) {
      Article article = articleRepository.findById(articleId)
              .orElseThrow(ResourceNotFoundException::new);
      List<Tag> newTags = tags.stream().map(Tag::new).collect(Collectors.toList());
      article.getTags().addAll(newTags);
      articleRepository.save(article);
      return ResponseEntity.ok().build();
  }

}
