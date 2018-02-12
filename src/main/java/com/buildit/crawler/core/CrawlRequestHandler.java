package com.buildit.crawler.core;

import com.buildit.crawler.exception.ConnectionTimeOutException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class CrawlRequestHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(CrawlRequestHandler.class);

    public Set<CrawledSiteDto> crawlSite(final String siteUrl) throws ConnectionTimeOutException {
        final HashSet<CrawledSiteDto> siteUrls = new HashSet<>();
        return crawlSiteFurther(siteUrls, siteUrl, siteUrl);
    }

    private HashSet<CrawledSiteDto> crawlSiteFurther(final HashSet<CrawledSiteDto> crawledSiteDtos,
                                                     final String siteUrl,
                                                     final String domainUrl) throws ConnectionTimeOutException {

        if (!isSiteUrlPresent(crawledSiteDtos, siteUrl)) {
            try {
                final Set<String> otherDomainUrls = new HashSet<>();
                final Set<String> externalUrls = new HashSet<>();
                final Set<String> staticContentUrls = new HashSet<>();
                crawledSiteDtos.add(new CrawledSiteDto(siteUrl, otherDomainUrls, staticContentUrls, externalUrls));

                final Document document = Jsoup.connect(siteUrl).get();
                //get all links and recursively call the processPage method
                Elements elements = document.select("a[href]");

                iterateOverAllElements(crawledSiteDtos, domainUrl, otherDomainUrls, externalUrls,
                        staticContentUrls, elements);

            } catch (final IOException e) {
                LOGGER.error("Some problem in connecting " + siteUrl, e);
                throw new ConnectionTimeOutException("Some problem in connecting " + siteUrl, crawledSiteDtos);
            }
        }
        return crawledSiteDtos;
    }

    private void iterateOverAllElements(final HashSet<CrawledSiteDto> crawledSiteDtos,
                                        final String domainUrl,
                                        final Set<String> otherDomainUrls,
                                        final Set<String> externalUrls,
                                        final Set<String> staticContentUrls,
                                        final Elements elements) throws ConnectionTimeOutException {
        for (Element element : elements) {
            staticContentUrls.addAll(findStaticContentUrls(element.children()));

            if (!element.attr("href").contains(domainUrl)) {
                externalUrls.add(element.attr("abs:href"));
            } else {
                otherDomainUrls.add(element.attr("abs:href"));
                crawlSiteFurther(crawledSiteDtos, element.attr("abs:href"), domainUrl);
            }
        }
    }

    private Set<String> findStaticContentUrls(final Elements elements) {
        return elements.stream().map(element -> element.attr("src"))
                .collect(Collectors.toSet());
    }

    private boolean isSiteUrlPresent(final HashSet<CrawledSiteDto> crawledSiteDtos,
                                     final String siteUrl) {
        final Predicate<CrawledSiteDto> sitePredicate = crawledSiteDto -> crawledSiteDto.getSiteUrl().equals(siteUrl);
        return crawledSiteDtos.stream().filter(sitePredicate).count() > 0;

    }
}
