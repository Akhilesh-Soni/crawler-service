package com.buildit.crawler.core;

import com.buildit.crawler.exception.ConnectionTimeOutException;
import org.hamcrest.MatcherAssert;
import org.hamcrest.core.Is;
import org.junit.Before;
import org.junit.Test;

import java.util.Set;


public class CrawlRequestHandlerTest {

    private CrawlRequestHandler objUnderTest;


    @Before
    public void setUp() throws Exception {
        objUnderTest = new CrawlRequestHandler();
    }


    @Test(expected = ConnectionTimeOutException.class)
    public void crawlSiteShallThrowConnectionTimeOutException() throws Exception {
        objUnderTest.crawlSite("https://wewer.com");
    }

    @Test
    public void crawlSiteShallReturnCrawledSiteData() throws Exception {
        Set<CrawledSiteDto> crawledSiteDtos = objUnderTest.crawlSite("https://google.com");
        MatcherAssert.assertThat(crawledSiteDtos.size(), Is.is(1));
    }
}