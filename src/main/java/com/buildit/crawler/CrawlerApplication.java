package com.buildit.crawler;

import com.buildit.crawler.core.CrawlRequestHandler;
import com.buildit.crawler.resources.CrawlerResource;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class CrawlerApplication extends Application<CrawlerConfiguration> {

    public static void main(final String[] args) throws Exception {
        new CrawlerApplication().run(args);
    }

    @Override
    public String getName() {
        return "Crawler";
    }

    @Override
    public void initialize(final Bootstrap<CrawlerConfiguration> bootstrap) {
        // TODO: application initialization
    }

    @Override
    public void run(final CrawlerConfiguration configuration,
                    final Environment environment) {
        // TODO: implement application
        environment.jersey().register(new CrawlerResource(new CrawlRequestHandler()));
    }

}
