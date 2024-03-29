package com.pratice_project.service.Impl;

import com.pratice_project.entities.Post;
import com.pratice_project.exception.ResourceNotFound;
import com.pratice_project.payload.PostDto;
import com.pratice_project.payload.PostResponse;
import com.pratice_project.repository.PostRepository;
import com.pratice_project.service.PostService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService {

    @Autowired
    private PostRepository postRepository;


    @Autowired
    private ModelMapper modelMapper;



    @Override
    public PostDto savePost(PostDto postDto) {
        Post post = mapToEntity(postDto);
        Post savedPost = postRepository.save(post);

        PostDto dto = mapToDto(savedPost);
        return dto;
    }

    @Override
    public void deletePost(long id) {
        postRepository.deleteById(id);
    }

    @Override
    public PostDto updatePost(long id, PostDto postDto) {
        Post post = postRepository.findById(id).orElseThrow(
                () -> new ResourceNotFound("Post not found with id" + id)
        );

        post.setTitle(postDto.getTitle());
        post.setContent(postDto.getContent());
        post.setDescription(postDto.getDescription());
        Post updatedPost = postRepository.save(post);
        PostDto dto = mapToDto(updatedPost);
        return dto;
    }

    @Override
    public PostDto getPostById(long id) {
        Post post = postRepository.findById(id).orElseThrow(
                () -> new ResourceNotFound("post not found" + id)
        );
        PostDto dto = mapToDto(post);
        return dto;
    }

    @Override
    public PostResponse  getPosts(int pageNo, int pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ?Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNo,pageSize, sort);
        Page<Post> pagePosts = postRepository.findAll(pageable);
        List<Post> posts = pagePosts.getContent();
        List<PostDto> postDtos = posts.stream().map(post -> mapToDto(post)).collect(Collectors.toList());

        PostResponse postResponse= new PostResponse();
        postResponse.setPostDto(postDtos);
        postResponse.setPageNo(pagePosts.getNumber());
        postResponse.setPageSize(pagePosts.getSize());
        postResponse.setTotalElements(Math.toIntExact(pagePosts.getTotalElements()));
        postResponse.setGetTotalElements(pagePosts.getTotalElements());
        postResponse.setLast(pagePosts.isLast());

        return postResponse;
    }

    PostDto mapToDto(Post post){
        PostDto dto= modelMapper.map(post, PostDto.class);
//        PostDto dto= new PostDto();
//        dto.setTitle(post.getTitle());
//        dto.setContent(post.getContent());
//        dto.setDescription(post.getDescription());
        return dto;
    }

    Post mapToEntity(PostDto postDto){
        Post post = modelMapper.map(postDto, Post.class);
//        Post post = new Post();
//        post.setTitle(postDto.getTitle());
//        post.setContent(postDto.getContent());
//        post.setDescription(postDto.getDescription());
        return post;
    }
}
