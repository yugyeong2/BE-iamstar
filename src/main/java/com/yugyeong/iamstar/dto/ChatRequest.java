package com.yugyeong.iamstar.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ChatRequest {
    private String message;
}
