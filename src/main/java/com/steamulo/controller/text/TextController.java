package com.steamulo.controller.text;

import org.springframework.web.bind.annotation.RestController;

import com.steamulo.controller.text.request.UpdateTextRequest;
import com.steamulo.controller.text.response.TextResponse;
import com.steamulo.enums.Page;
import com.steamulo.persistence.entity.TextPage;
import com.steamulo.services.text.TextService;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import lombok.extern.slf4j.Slf4j;

/**
 * WS concernant la gestion des textes
 */
@Slf4j
@RestController
@RequestMapping(value = "/text")
public class TextController {

    private final TextService textService;

    public TextController(TextService textService) {
        this.textService = textService;
    }

    /**
     * WS de récupération du texte pour une page donnée
     * 
     * @param page la page pour laquelle on veut récupérer les textes
     * @return le texte de la page
     */
    @GetMapping()
    public ResponseEntity<TextResponse> getTextOfPage(@RequestParam("page") String page) {
        log.info("Getting text of page {}", page);
        TextPage textPage = textService.getTextPage(Page.valueOf(page));
        return ResponseEntity.ok().body(
                TextResponse.builder().content(textPage.getContent()).page(textPage.getPage()).build());
    }

    /**
     * WS de modification du texte pour une page donnée
     * 
     * @param page la page pour laquelle on veut modifier le texte
     * @return 204 no content si le texte a été modifié, une erreur 400 sinon
     */
    @PreAuthorize("hasAuthority('TEXT_UPDATE')")
    @PutMapping()
    public ResponseEntity<Void> putTextOfPage(@RequestBody @Valid UpdateTextRequest request) {
        log.info("Putting text of page {}", request);
        textService.updateTextPage(request.getPage(), request.getContent());
        return ResponseEntity.noContent().build();
    }

}
