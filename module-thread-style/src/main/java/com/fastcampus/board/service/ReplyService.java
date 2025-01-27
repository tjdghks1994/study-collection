package com.fastcampus.board.service;

import com.fastcampus.board.exception.post.PostNotFoundException;
import com.fastcampus.board.exception.reply.ReplyNotFoundException;
import com.fastcampus.board.exception.user.UserNotAllowedException;
import com.fastcampus.board.exception.user.UserNotFoundException;
import com.fastcampus.board.model.entity.PostEntity;
import com.fastcampus.board.model.entity.ReplyEntity;
import com.fastcampus.board.model.entity.UserEntity;
import com.fastcampus.board.model.post.Post;
import com.fastcampus.board.model.post.PostPatchRequestBody;
import com.fastcampus.board.model.post.PostPostRequestBody;
import com.fastcampus.board.model.reply.Reply;
import com.fastcampus.board.model.reply.ReplyPatchRequestBody;
import com.fastcampus.board.model.reply.ReplyPostRequestBody;
import com.fastcampus.board.repository.PostEntityRepository;
import com.fastcampus.board.repository.ReplyEntityRepository;
import com.fastcampus.board.repository.UserEntityRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class ReplyService {

    private final ReplyEntityRepository replyEntityRepository;
    private final PostEntityRepository postEntityRepository;
    private final UserEntityRepository userEntityRepository;

    public ReplyService(ReplyEntityRepository replyEntityRepository,
                        PostEntityRepository postEntityRepository,
                        UserEntityRepository userEntityRepository) {
        this.replyEntityRepository = replyEntityRepository;
        this.postEntityRepository = postEntityRepository;
        this.userEntityRepository = userEntityRepository;
    }

    public List<Reply> getRepliesByPostId(Long postId) {
        var postEntity =
                postEntityRepository.findById(postId).orElseThrow(() -> new PostNotFoundException(postId));

        return replyEntityRepository.findByPost(postEntity).stream()
                .map(Reply::from).collect(Collectors.toList());
    }

    @Transactional
    public Reply createReply(Long postId, ReplyPostRequestBody body, UserEntity currentUser) {
        var postEntity =
                postEntityRepository.findById(postId).orElseThrow(() -> new PostNotFoundException(postId));

        var replyEntity = replyEntityRepository.save(
                ReplyEntity.of(body.body(), currentUser, postEntity)
        );

        postEntity.setRepliesCount(postEntity.getRepliesCount() + 1);

        return Reply.from(replyEntity);
    }

    @Transactional
    public Reply updateReply(Long postId, Long replyId, ReplyPatchRequestBody body, UserEntity currentUser) {
        postEntityRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException(postId));
        var replyEntity = replyEntityRepository.findById(replyId)
                .orElseThrow(() -> new ReplyNotFoundException(replyId));

        if (!replyEntity.getUser().equals(currentUser)) {
            throw new UserNotAllowedException();
        }

        replyEntity.setBody(body.body());

        return Reply.from(replyEntity);
    }

    @Transactional
    public void deleteReply(Long postId, Long replyId, UserEntity currentUser) {
        var postEntity = postEntityRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException(postId));
        var replyEntity = replyEntityRepository.findById(replyId)
                .orElseThrow(() -> new ReplyNotFoundException(replyId));

        if (!replyEntity.getUser().equals(currentUser)) {
            throw new UserNotAllowedException();
        }

        replyEntityRepository.delete(replyEntity);
        postEntity.setRepliesCount(Math.max(0, postEntity.getRepliesCount() - 1));
    }

    public List<Reply> getRepliesByUsername(String username) {
        var userEntity = userEntityRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));

        var replyEntities = replyEntityRepository.findByUser(userEntity);

        return replyEntities.stream().map(Reply::from).collect(Collectors.toList());
    }
}
