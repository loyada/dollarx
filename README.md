# dollarx

## Example 1:
    //Given I on google.com
    driver get "www.google.com"

    // when I google for amazon
    val searchFormWrapper = has id "searchform" and contains(form)
    val google = input inside searchFormWrapper
    sendKeys("amazon") to google

    // then the first result contains "amazon.com" 
    val results = div that (has id "search")
    val resultsLink = anchor inside results
    val firstResult = first occuranceOf resultsLink
    val amazonResult = firstResult that (has textContaining "amazon.com")
    amazonResult must be(present)


## Example 2:
    val menuItem = li withClass "foo" describedBy "search result"
    val myExpected = li that (is after(5 occurancesOf menuItem))
    println(myExpected)
    // "list item, that is after 5 occurances of search result"

    myExpected must (appear(5 times) in browser)
    // example assertion error:
    // (list item, that is after 5 occurances of search result) should appear 5 times,
    // ...but it appears 4 times