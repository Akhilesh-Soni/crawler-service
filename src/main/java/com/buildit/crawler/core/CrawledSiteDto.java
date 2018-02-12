package com.buildit.crawler.core;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Set;

public class CrawledSiteDto {

    private final String siteUrl;
    private final Set<String> otherDomainUrls;
    private final Set<String> staticContentUrls;
    private final Set<String> externalUrls;

    @JsonCreator()
    public CrawledSiteDto(@JsonProperty("siteUrl") final String siteUrl,
                          @JsonProperty("otherDomainUrls") final Set<String> otherDomainUrls,
                          @JsonProperty("staticContentUrls") final Set<String> staticContentUrls,
                          @JsonProperty("externalUrls") final Set<String> externalUrls) {
        this.siteUrl = siteUrl;
        this.otherDomainUrls = otherDomainUrls;
        this.externalUrls = externalUrls;
        this.staticContentUrls = staticContentUrls;
    }

    public String getSiteUrl() {
        return siteUrl;
    }

    public Set<String> getOtherDomainUrls() {
        return otherDomainUrls;
    }

    public Set<String> getStaticContentUrls() {
        return staticContentUrls;
    }

    public Set<String> getExternalUrls() {
        return externalUrls;
    }
}
