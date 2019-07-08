package com.cheroliv.fiber.web.rest;

import com.cheroliv.fiber.domain.Inter;
import com.cheroliv.fiber.repository.InterRepository;
import com.cheroliv.fiber.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.cheroliv.fiber.domain.Inter}.
 */
@RestController
@RequestMapping("/api")
public class InterResource {

    private final Logger log = LoggerFactory.getLogger(InterResource.class);

    private static final String ENTITY_NAME = "inter";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final InterRepository interRepository;

    public InterResource(InterRepository interRepository) {
        this.interRepository = interRepository;
    }

    /**
     * {@code POST  /inters} : Create a new inter.
     *
     * @param inter the inter to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new inter, or with status {@code 400 (Bad Request)} if the inter has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/inters")
    public ResponseEntity<Inter> createInter(@Valid @RequestBody Inter inter) throws URISyntaxException {
        log.debug("REST request to save Inter : {}", inter);
        if (inter.getId() != null) {
            throw new BadRequestAlertException("A new inter cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Inter result = interRepository.save(inter);
        return ResponseEntity.created(new URI("/api/inters/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /inters} : Updates an existing inter.
     *
     * @param inter the inter to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated inter,
     * or with status {@code 400 (Bad Request)} if the inter is not valid,
     * or with status {@code 500 (Internal Server Error)} if the inter couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/inters")
    public ResponseEntity<Inter> updateInter(@Valid @RequestBody Inter inter) throws URISyntaxException {
        log.debug("REST request to update Inter : {}", inter);
        if (inter.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Inter result = interRepository.save(inter);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, inter.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /inters} : get all the inters.
     *
     * @param pageable the pagination information.
     * @param queryParams a {@link MultiValueMap} query parameters.
     * @param uriBuilder a {@link UriComponentsBuilder} URI builder.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of inters in body.
     */
    @GetMapping("/inters")
    public ResponseEntity<List<Inter>> getAllInters(Pageable pageable, @RequestParam MultiValueMap<String, String> queryParams, UriComponentsBuilder uriBuilder) {
        log.debug("REST request to get a page of Inters");
        Page<Inter> page = interRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(uriBuilder.queryParams(queryParams), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /inters/:id} : get the "id" inter.
     *
     * @param id the id of the inter to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the inter, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/inters/{id}")
    public ResponseEntity<Inter> getInter(@PathVariable Long id) {
        log.debug("REST request to get Inter : {}", id);
        Optional<Inter> inter = interRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(inter);
    }

    /**
     * {@code DELETE  /inters/:id} : delete the "id" inter.
     *
     * @param id the id of the inter to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/inters/{id}")
    public ResponseEntity<Void> deleteInter(@PathVariable Long id) {
        log.debug("REST request to delete Inter : {}", id);
        interRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
