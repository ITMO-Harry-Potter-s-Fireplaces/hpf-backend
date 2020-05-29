package ru.fireplaces.harrypotter.itmo.utils.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.springframework.data.domain.Page;

import java.io.Serializable;
import java.util.List;

/**
 * Custom POJO for paged responses.
 *
 * @param <T> Class type of paged resource.
 *
 * @author seniorkot
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class PageBody<T> implements Serializable {

    /**
     * Response content
     */
    private List<T> content;

    /**
     * Requested page number
     */
    private long page;

    /**
     * Requested page size
     */
    private long size;

    /**
     * Total number of pages
     */
    private long totalPages;

    /**
     * Total number of elements
     */
    private long totalElements;

    /**
     * Number of elements on requested page
     */
    private long numberOfElements;

    /**
     * Constructor with {@link Page} object as argument.
     *
     * @param pageT Page object
     */
    public PageBody(Page<T> pageT) {
        this.content = pageT.getContent();
        this.page = pageT.getNumber();
        this.size = pageT.getSize();
        this.totalPages = pageT.getTotalPages();
        this.totalElements = pageT.getTotalElements();
        this.numberOfElements = pageT.getNumberOfElements();
    }
}
