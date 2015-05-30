package dollarx

object DslPlay {
  import ElementProperties._
  import WebEl._
  import InBrowser._

  div sibling
    print(is after 5 siblings div )
  print(is afterSibling div)
  print(is after 5 div)
  print(is after div)
  print(is after (div sibling)

    print(is after 5 (div siblings)
    print(is after div sibling)
    print(is after 5 div)
    print(is after div)

    WebEl
    WebEl sibling
    WebEl parent
    WebEl child
    5 WebEl siblings
}
