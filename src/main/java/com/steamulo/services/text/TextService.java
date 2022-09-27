package com.steamulo.services.text;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.steamulo.enums.Page;
import com.steamulo.exception.ApiException;
import com.steamulo.persistence.entity.TextPage;
import com.steamulo.persistence.repository.TextPageRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class TextService {

    private final TextPageRepository textPageRepository;

    public TextService(TextPageRepository textPageRepository) {
        this.textPageRepository = textPageRepository;
    }

    public TextPage getTextPage(Page page) {
        log.info("Fetching text page: " + page);
        return textPageRepository.findByPage(page);
    }

    public void updateTextPage(Page page, String content) {
        log.info("Updating text page: " + page);
        TextPage textPage = textPageRepository.findByPage(page);
        log.info("Text page found: " + textPage);
        log.info("New content: " + content);
        if (textPage == null) {
            throw new ApiException(HttpStatus.NOT_FOUND, "Text page not found");
        }
        textPage.setContent(content);
        textPageRepository.save(textPage);
    }
}
