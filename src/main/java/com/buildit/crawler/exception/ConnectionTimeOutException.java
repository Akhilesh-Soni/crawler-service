package com.buildit.crawler.exception;

import com.buildit.crawler.core.CrawledSiteDto;

import java.util.Set;

public class ConnectionTimeOutException extends BaseException {
    private final Set<CrawledSiteDto> siteUrls;

    public ConnectionTimeOutException(final String message,
                                      final Set<CrawledSiteDto> siteUrls) {
        super(message);
        this.siteUrls = siteUrls;
    }

    public Set<CrawledSiteDto> getSiteUrls() {
        return siteUrls;
    }
}
