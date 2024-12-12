package io.spring.core.comment;

import io.spring.core.user.User;

import java.util.Optional;

public interface CommentRepository {
  void save(Comment comment);

  Optional<Comment> findById(String articleId, String id);

  void remove(Comment comment);

  void editComment(String commentId, User user, String commentEdit);
}
