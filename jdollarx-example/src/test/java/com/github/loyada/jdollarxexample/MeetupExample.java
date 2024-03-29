package com.github.loyada.jdollarxexample;

import com.github.loyada.jdollarx.singlebrowser.InBrowserSinglton;
import static com.github.loyada.jdollarx.singlebrowser.InBrowserSinglton.*;
import com.github.loyada.jdollarx.Operations;
import com.github.loyada.jdollarx.Path;

import static com.github.loyada.jdollarx.BasicPath.firstOccurrenceOf;
import static com.github.loyada.jdollarx.BasicPath.*;
import static com.github.loyada.jdollarx.ElementProperties.*;


public class MeetupExample {
      public static void main(String[] argc) throws Operations.OperationFailedException {
          InBrowserSinglton.driver = DriverSetup.createStandardChromeDriver();
          InBrowserSinglton.driver.get("http://www.meetup.com");

          Path viewableItem = div.withClasses("carousel-cell", "is-selected");
          dragAndDrop(firstOccurrenceOf(viewableItem)).to(firstOccurrenceOf(viewableItem));

          Path techCategory = anchor.withClass("categoryLink").that(hasAggregatedTextContaining("Tech"));
          scrollTo(techCategory);
          clickAt(techCategory);

          clickAt(anchor.withText("5 miles"));
          clickAt(anchor.inside(listItem).withText("any distance"));

          Path navigationBar = div.that(hasId("findNavBar")).describedBy("navigation bar");
          Path searchInput = input.that(hasAttribute("name", "keywords")).inside(navigationBar);
          sendKeys("java NYC\n").to(searchInput);

          Path nycJavaCard = listItem.withClass("groupCard").that(hasAggregatedTextContaining("new york city Java meetup group"));
          clickAt(nycJavaCard);

          clickAt(anchor.withText("timothy fagan"));
          clickAt(anchor.inside(span.that(hasId("member-profile-photo"))));

          InBrowserSinglton.driver.quit();
    }
}
