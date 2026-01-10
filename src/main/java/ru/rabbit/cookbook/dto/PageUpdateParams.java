package ru.rabbit.cookbook.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PageUpdateParams {

    private String pageId;

    private String sectionId;

    private UpdatePageRequest request;
}
