package ru.rabbit.cookbook.service;

import java.util.List;

import ru.rabbit.cookbook.dto.CreatePageRequest;
import ru.rabbit.cookbook.dto.Page;
import ru.rabbit.cookbook.dto.PageUpdateParams;

public interface PageService {

    List<Page> getPages(String subsectionId);

    Page createPage(String subsectionId, CreatePageRequest request);

    Page updatePage(PageUpdateParams params);

    void deletePage(String pageId);
}
