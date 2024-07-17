package com.yugyeong.iamstar.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class ChatResponse {
    private String response;
    private List<String> sources;
}
