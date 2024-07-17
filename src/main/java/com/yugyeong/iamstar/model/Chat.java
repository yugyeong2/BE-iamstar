package com.yugyeong.iamstar.model;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Builder
@Document(collection = "chat")
public class Chat {
    @Id
    private String id;
    private String content;
    private String metadata;
}
