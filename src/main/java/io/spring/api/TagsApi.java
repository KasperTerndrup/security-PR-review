package io.spring.api;

import io.spring.api.exception.ResourceNotFoundException;
import io.spring.application.TagsQueryService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
    private TagsQueryService tagsQueryService;
    private final ArticleRepository articleRepository;

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
  public ResponseEntity addTags(@PathVariable String articleId, List<String> tags) {
    Article article = articleRepository.findById(articleId).orElseThrow(ResourceNotFoundException::new);
    List<Tag> mappedTags = mapTags(tags);
    article.getTags().addAll(mappedTags);
    articleRepository.save(article);
    return ResponseEntity.ok().build();
  }

  private List<Tag> mapTags(List<String> tags) {
    List<Tag> results = new ArrayList<>();
    for (String tag : tags) {
      results.add(new Tag(tag));
    }
    return results;
  }

}
