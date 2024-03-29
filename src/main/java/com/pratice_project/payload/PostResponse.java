package com.pratice_project.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostResponse {
    private List<PostDto> postDto;
    private int pageNo;
    private int pageSize;
    private long totalElements;
    private long getTotalElements;
    private boolean last;


}
