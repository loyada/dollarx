package com.github.loyada.jdollarxexample;

import com.github.loyada.jdollarx.InBrowser;
import com.github.loyada.jdollarx.Operations;
import com.github.loyada.jdollarx.Path;

import static com.github.loyada.jdollarx.BasicPath.*;
import static com.github.loyada.jdollarx.ElementProperties.*;


public class MeetupExample {
      public static void main(String[] argc) throws Operations.OperationFailedException {
          InBrowser browser = new InBrowser(DriverSetup.createStandardChromeDriver());
          browser.getDriver().get("http://www.meetup.com");

          Path techCategory = anchor.withClass("categoryLink").that(hasAggregatedTextContaining("Tech"));
          browser.clickAt(techCategory);

          Path navigationBar = div.that(hasId("findNavBar")).describedBy("navigation bar");
          Path searchInput = input.that(hasAttribute("name", "keywords")).inside(navigationBar);
          browser.sendKeys("java NYC\n").to(searchInput);

          Path nycJavaCard = listItem.withClass("groupCard").that(hasAggregatedTextContaining("new york city Java meetup group"));
          browser.clickAt(nycJavaCard);

          browser.clickAt(anchor.withText("timothy fagan"));
          browser.clickAt(anchor.inside(span.that(hasId("member-profile-photo"))));

          browser.getDriver().quit();
    }
}
