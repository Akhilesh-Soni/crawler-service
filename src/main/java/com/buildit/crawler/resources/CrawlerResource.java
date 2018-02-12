package com.buildit.crawler.resources;

import com.buildit.crawler.core.CrawlRequestHandler;
import com.buildit.crawler.core.CrawledSiteDto;
import com.buildit.crawler.core.CrawledSiteListDto;
import com.buildit.crawler.exception.ConnectionTimeOutException;

import javax.annotation.Nonnull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Collections;
import java.util.Set;

/**
 * It is the rest resource exposed to external client.
 **/

@Path("/crawler-service")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CrawlerResource {

    private final CrawlRequestHandler crawlRequestHandler;

    public CrawlerResource(final CrawlRequestHandler crawlRequestHandler) {
        this.crawlRequestHandler = crawlRequestHandler;
    }

    /**
     * Entry point to crawl the site.
     * It will validate the query param.
     * <br>
     * Example of Response JSON payload:
     * <p>
     * {<br>
     *      "message": "All site url has been crawled",<br>
     *      "accessedSites": [<br>
     *                          {<br>
     *                              "siteUrl": "http://google.com",<br>
     *                              "otherDomainUrls": [],<br>
     *                              "staticContentUrls": [<br>
     *                                                      "/logos/doodles/2018/doodle-snow-games-day-2-6047722892165120.3-l.png"<br>
     *                                                  ],<br>
     *                              "externalUrls": [<br>
     *                                                      "http://maps.google.co.in/maps?hl=en&tab=wl",<br>
     *                                                      "https://play.google.com/?hl=en&tab=w8",<br>
     *                                                      "http://www.google.co.in/language_tools?hl=en-IN&authuser=0",<br>
     *                                                      "http://www.google.co.in/services/",<br>
     *                                                      "http://www.google.co.in/search?dcr=0&site=&ie=UTF-8&q=Winter+Olympics
     *                                                      &oi=ddle&ct=doodle-snow-games-day-2-6047722892165120&hl=en&kgmid*
     *                                                      =/m/03tng8&sa=X&ved=0ahUKEwi7o5v496DZAhVEsY8KHeyRAOAQPQgD",<br>
     *                                                      "http://news.google.co.in/nwshp?hl=en&tab=wn",<br>
     *                                                      "http://www.google.co.in/intl/en/ads/",<br>
     *                                                      "https://drive.google.com/?tab=wo",<br>
     *                                                      "http://www.google.co.in/setprefs?sig=0_phdggVIzZur2vG6cLIxbwNvTbDY%3D&hl=te
     *                                                      &source=homepage&sa=X&ved* =0ahUKEwi7o5v496DZAhVEsY8KHeyRAOAQ2ZgBCAc",<br>
     *                                                      "http://www.google.co.in/advanced_search?hl=en-IN&authuser=0",<br>
     *                                                      "http://www.google.co.in/intl/en/about.html",<br>
     *                                                      "http://www.google.co   .in/setprefdomain?prefdom=US
     *                                                      &sig=__r1sEFaEHrf8UQAFWiJQJt0BDEzc%3D",<br>
     *                                                      "http://www.google.co.in/intl/en/policies/privacy/",<br>
     *                                                      "http://www.google.co.in/setprefs?sig=0_phdggVIzZur2vG6cLIxbwNvTbDY%3D&hl=mr
     *                                                      &source=homepage&sa=X&ved* =0ahUKEwi7o5v496DZAhVEsY8KHeyRAOAQ2ZgBCAg",<br>
     *                                                      "https://mail.google.com/mail/?tab=wm",<br>
     *                                                      "http://www.google.co.in/intl/en/policies/terms/",<br>
     *                                                      "http://www.google.co.in/setprefs?sig=0_phdggVIzZur2vG6cLIxbwNvTbDY%3D&hl=pa
     *                                                      &source=homepage&sa=X&ved* =0ahUKEwi7o5v496DZAhVEsY8KHeyRAOAQ2ZgBCA0",<br>
     *                                                      "http://www.google.co.in/setprefs?sig=0_phdggVIzZur2vG6cLIxbwNvTbDY%3D&hl=ml
     *                                                      &source=homepage&sa=X&ved* =0ahUKEwi7o5v496DZAhVEsY8KHeyRAOAQ2ZgBCAw",<br>
     *                                                      "http://www.google.co.in/setprefs?sig=0_phdggVIzZur2vG6cLIxbwNvTbDY%3D&hl=gu
     *                                                      &source=homepage&sa=X&ved* =0ahUKEwi7o5v496DZAhVEsY8KHeyRAOAQ2ZgBCAo",<br>
     *                                                      "http://www.google.co.in/history/optout?hl=en",<br>
     *                                                      "http://www.google.co.in/setprefs?sig=0_phdggVIzZur2vG6cLIxbwNvTbDY%3D
     *                                                      &hl=kn&source=homepage&sa=X&ved* =0ahUKEwi7o5v496DZAhVEsY8KHeyRAOAQ2ZgBCAs",<br>
     *                                                      "http://www.youtube.com/?gl=IN&tab=w1",<br>
     *                                                      "http://www.google.co.in/imghp?hl=en&tab=wi",<br>
     *                                                      "http://www.google.co.in/preferences?hl=en",<br>
     *                                                      "https://accounts.google.com/ServiceLogin?hl=en&passive=true
     *                                                      &continue=http://www.google.co*.in/
     *                                                      %3Fgfe_rd%3Dcr%26dcr%3D0%26ei%3DmdOBWvjKK6GmX7TtlsgC",<br>
     *                                                      "http://www.google.co.in/setprefs?sig=0_phdggVIzZur2vG6cLIxbwNvTbDY%3D&hl=ta
     *                                                      &source=homepage&sa=X&ved* =0ahUKEwi7o5v496DZAhVEsY8KHeyRAOAQ2ZgBCAk",<br>
     *                                                      "http://www.google.co.in/setprefs?sig=0_phdggVIzZur2vG6cLIxbwNvTbDY%3D&hl=hi
     *                                                      &source=homepage&sa=X&ved* =0ahUKEwi7o5v496DZAhVEsY8KHeyRAOAQ2ZgBCAU",<br>
     *                                                      "http://www.google.co.in/setprefs?sig=0_phdggVIzZur2vG6cLIxbwNvTbDY%3D&hl=bn
     *                                                      &source=homepage&sa=X&ved* =0ahUKEwi7o5v496DZAhVEsY8KHeyRAOAQ2ZgBCAY",<br>
     *                                                      "https://plus.google.com/104205742743787718296",<br>
     *                                                      "https://www.google.co.in/intl/en/options/"<br>
     *                                              ]<br>
     *                              }<br>
     *                      ]<br>
     * }<br>
     *
     * @return {@link Response} <br>
     * 200 if successful<br>
     * 206 if partial sites were crawled.<br>
     * 400 if the site url is invalid format.
     **/
    @GET
    @Path("/crawl-site")
    public Response crawlSite(@QueryParam("site_url") @Nonnull final String siteUrl) {
        Response response;
        Set<CrawledSiteDto> siteUrls = null;
        try {
            if (siteUrl.startsWith("http://")||siteUrl.startsWith("https://")) {
                siteUrls = crawlRequestHandler.crawlSite(siteUrl);
                response = Response.status(Response.Status.OK).entity(new CrawledSiteListDto(
                        "All site url has been crawled.",
                        siteUrls)).build();
            } else {
                response = Response.status(Response.Status.BAD_REQUEST)
                        .entity(new CrawledSiteListDto("Site url is invalid.", Collections.emptySet())).build();
            }

        } catch (final ConnectionTimeOutException e) {
            response = Response.status(Response.Status.PARTIAL_CONTENT).entity(new CrawledSiteListDto(e.getMessage(),
                    e.getSiteUrls())).build();
        }
        return response;
    }


}
