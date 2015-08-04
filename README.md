# dollarx

## Example:
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
