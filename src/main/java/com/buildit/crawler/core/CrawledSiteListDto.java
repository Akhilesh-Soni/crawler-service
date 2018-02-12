package com.buildit.crawler.core;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Set;

/**
 * The response dto returned to external clients.
 **/
public class CrawledSiteListDto {

    private final String message;
    private final Set<CrawledSiteDto> accessedSites;

    @JsonCreator
    public CrawledSiteListDto(@JsonProperty("message") final String message,
                              @JsonProperty("accessedSites") final Set<CrawledSiteDto> accessedSites) {
        this.accessedSites = accessedSites;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public Set<CrawledSiteDto> getAccessedSites() {
        return accessedSites;
    }
}
