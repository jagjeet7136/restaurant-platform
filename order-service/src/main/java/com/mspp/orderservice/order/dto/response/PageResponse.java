package com.mspp.orderservice.order.dto.response;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;
import java.util.List;
import io.swagger.v3.oas.annotations.media.Schema;

@Getter
@Builder
@Schema(description = "Paginated API response")
public class PageResponse<T> {

    @Schema(description = "Items returned for the current page")
    private List<T> content;

    @Schema(description = "Current zero-based page number", example = "0")
    private int page;

    @Schema(description = "Maximum number of items per page", example = "10")
    private int size;

    @Schema(description = "Total number of matching records", example = "25")
    private long totalElements;

    @Schema(description = "Total number of available pages", example = "3")
    private int totalPages;

    @Schema(description = "Whether this is the first page", example = "true")
    private boolean first;

    @Schema(description = "Whether this is the last page", example = "false")
    private boolean last;

    @Schema(description = "Whether another page exists", example = "true")
    private boolean hasNext;

    @Schema(description = "Whether a previous page exists", example = "false")
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


