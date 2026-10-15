package com.mspp.restaurantservice.restaurant.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;
import java.util.List;

@Getter
@Builder
@Schema(description = "Paginated API response")
public class PageResponse<T> {

    @Schema(
            description = "Records contained in the current page"
    )
    private List<T> content;

    @Schema(
            description = "Current zero-based page number",
            example = "0"
    )
    private int page;

    @Schema(
            description = "Maximum number of records returned per page",
            example = "10"
    )
    private int size;

    @Schema(
            description = "Total number of matching records",
            example = "42"
    )
    private long totalElements;

    @Schema(
            description = "Total number of available pages",
            example = "5"
    )
    private int totalPages;

    @Schema(
            description = "Whether this is the first page",
            example = "true"
    )
    private boolean first;

    @Schema(
            description = "Whether this is the last page",
            example = "false"
    )
    private boolean last;

    @Schema(
            description = "Whether another page exists after the current page",
            example = "true"
    )
    private boolean hasNext;

    @Schema(
            description = "Whether a page exists before the current page",
            example = "false"
    )
    private boolean hasPrevious;

    public static <T> PageResponse<T> from(Page<T> page) {

        return PageResponse.<T>builder()
                .content(page.getContent())
                .page(page.getNumber())
                .size(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .first(page.isFirst())
                .last(page.isLast())
                .hasNext(page.hasNext())
                .hasPrevious(page.hasPrevious())
                .build();
    }
}