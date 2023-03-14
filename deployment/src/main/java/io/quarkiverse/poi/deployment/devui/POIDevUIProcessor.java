package io.quarkiverse.poi.deployment.devui;

import org.apache.poi.ss.util.SheetUtil;

import io.quarkus.deployment.IsDevelopment;
import io.quarkus.deployment.annotations.BuildProducer;
import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.devui.spi.page.CardPageBuildItem;
import io.quarkus.devui.spi.page.Page;
import io.quarkus.devui.spi.page.PageBuilder;

/**
 * Dev UI card for displaying important details such as the POI library version.
 */
public class POIDevUIProcessor {

    @BuildStep(onlyIf = IsDevelopment.class)
    void createVersion(BuildProducer<CardPageBuildItem> cardPageBuildItemBuildProducer) {
        final CardPageBuildItem card = new CardPageBuildItem("Apache POI");

        final PageBuilder versionPage = Page.externalPageBuilder("Version")
                .icon("font-awesome-regular:file-excel")
                .url("https://poi.apache.org/")
                .isHtmlContent()
                .staticLabel(SheetUtil.class.getPackage().getImplementationVersion());

        card.addPage(versionPage);

        card.setCustomCard("qwc-poi-card.js");

        cardPageBuildItemBuildProducer.produce(card);
    }
}
