package com.buildit.crawler.resources;

import com.buildit.crawler.core.CrawlRequestHandler;
import com.buildit.crawler.core.CrawledSiteDto;
import com.buildit.crawler.core.CrawledSiteListDto;
import com.buildit.crawler.exception.ConnectionTimeOutException;
import org.hamcrest.MatcherAssert;
import org.hamcrest.core.Is;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import javax.ws.rs.core.Response;
import java.util.HashSet;
import java.util.Set;

public class CrawlerResourceTest {

    private final CrawlRequestHandler crawlRequestHandler = Mockito.mock(CrawlRequestHandler.class);
    private CrawlerResource objUnderTest;

    @Before
    public void setUp() throws Exception {
        objUnderTest = new CrawlerResource(crawlRequestHandler);
    }

    private Set<CrawledSiteDto> prepareData(String siteUrl) {
        Set<CrawledSiteDto> crawledSiteDtos = new HashSet<>();
        Set<String> otherDomainUrls = new HashSet<>();
        Set<String> staticContentUrls = new HashSet<>();
        Set<String> externalUrls = new HashSet<>();

        staticContentUrls.add("/logos/doodles/2018/doodle-snow-games-day-2-6047722892165120.3-l.png");
        externalUrls.add("http://maps.google.co.in/maps?hl=en&tab=wl");
        externalUrls.add("https://play.google.com/?hl=en&tab=w8");

        crawledSiteDtos.add(new CrawledSiteDto(siteUrl, otherDomainUrls, staticContentUrls, externalUrls));
        return crawledSiteDtos;
    }

    @Test
    public void crawlSiteShallReturnBadRequestResponseIfSiteUrlIsInvalid() throws Exception {
        Response response = objUnderTest.crawlSite(" ");
        MatcherAssert.assertThat(response.getStatus(), Is.is(Response.Status.BAD_REQUEST.getStatusCode()));
    }

    @Test
    public void crawlSiteShallReturnPartialContentResponseIfTheSiteIsNotReachable() throws Exception {
        String siteUrl = "https://google.com";
        Mockito.when(crawlRequestHandler.crawlSite(Mockito.anyString()))
                .thenThrow(new ConnectionTimeOutException("Some problem in connecting " + siteUrl, prepareData(siteUrl)));
        Response response = objUnderTest.crawlSite(siteUrl);

        CrawledSiteListDto responseDto = (CrawledSiteListDto) response.getEntity();
        MatcherAssert.assertThat(response.getStatus(), Is.is(Response.Status.PARTIAL_CONTENT.getStatusCode()));
        MatcherAssert.assertThat(responseDto.getMessage(), Is.is("Some problem in connecting " + siteUrl));
        MatcherAssert.assertThat(responseDto.getAccessedSites().size(), Is.is(1));
    }

    @Test
    public void crawlSiteShallReturnOkResponseIfSiteHasBeenCrawled() throws Exception {
        String siteUrl = "https://google.com";
        Mockito.when(crawlRequestHandler.crawlSite(Mockito.anyString()))
                .thenReturn(prepareData(siteUrl));
        Response response = objUnderTest.crawlSite(siteUrl);

        CrawledSiteListDto responseDto = (CrawledSiteListDto) response.getEntity();
        CrawledSiteDto crawledSiteDto = responseDto.getAccessedSites().stream().findFirst().get();

        MatcherAssert.assertThat(response.getStatus(), Is.is(Response.Status.OK.getStatusCode()));
        MatcherAssert.assertThat(responseDto.getMessage(), Is.is("All site url has been crawled."));
        MatcherAssert.assertThat(responseDto.getAccessedSites().size(), Is.is(1));
        MatcherAssert.assertThat(crawledSiteDto.getSiteUrl(), Is.is(siteUrl));
        MatcherAssert.assertThat(crawledSiteDto.getOtherDomainUrls().size(), Is.is(0));
        MatcherAssert.assertThat(crawledSiteDto.getStaticContentUrls().size(), Is.is(1));
        MatcherAssert.assertThat(crawledSiteDto.getExternalUrls().size(), Is.is(2));
    }
}