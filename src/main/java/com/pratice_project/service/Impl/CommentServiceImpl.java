package com.pratice_project.service.Impl;

import com.pratice_project.entities.Comment;
import com.pratice_project.entities.Post;
import com.pratice_project.exception.ResourceNotFound;
import com.pratice_project.payload.CommentDto;
import com.pratice_project.repository.CommentRepository;
import com.pratice_project.repository.PostRepository;
import com.pratice_project.service.CommentService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentService {


    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private ModelMapper mapper;




    @Override
    public CommentDto createComment(long postId, CommentDto commentDto) {
        Comment comment= maptoEntity(commentDto);

        Post post = postRepository.findById(postId).orElseThrow(
                () -> new ResourceNotFound("Post not found with postId:" + postId)
        );

        comment.setPost(post);
        Comment savedComment = commentRepository.save(comment);

        CommentDto dto= mapToDto(savedComment);

        return dto;
    }

    @Override
    public List<CommentDto> getCommentsByPostId(long postId) {
        postRepository.findById(postId).orElseThrow(
                ()->new ResourceNotFound("post not found with id"+postId)
        );
        List<Comment> comments = commentRepository.findByPostId(postId);
        List<CommentDto> commentDtos = comments.stream().map(comment -> mapToDto(comment)).collect(Collectors.toList());
        return commentDtos;
    }

    @Override
    public CommentDto getCommentsById(Long postId, Long commentId) {
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new ResourceNotFound("Post not found with postId:" + postId)
        );

        Comment comment = commentRepository.findById(commentId).orElseThrow(
                () -> new ResourceNotFound("Comment not found with postId:" +commentId)
        );
        CommentDto commentDto = mapToDto(comment);
        return  commentDto;
    }

    @Override
    public List<CommentDto> getAllCommentsById() {
        List<Comment> comments = commentRepository.findAll();
        List<CommentDto> commentDtos = comments.stream().map(comment -> mapToDto(comment)).collect(Collectors.toList());
        return commentDtos;
    }

    @Override
    public void deleteCommentById(Long postId, Long commentId) {
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new ResourceNotFound("Post not found with postId:" + postId)
        );

        Comment comment = commentRepository.findById(commentId).orElseThrow(
                () -> new ResourceNotFound("Comment not found with postId:" +commentId)
        );
        commentRepository.deleteById(commentId);
    }

    private Comment maptoEntity(CommentDto commentDto){
        Comment comment = mapper.map(commentDto, Comment.class);
        return comment;
    }

    private CommentDto mapToDto(Comment savedComment){
        CommentDto dto = mapper.map(savedComment, CommentDto.class);
        return dto;
    }
}
