Develop microservice in Spring Boot that would serve a single Restful API 
 
GET on http://localhost:8083/pricing/v1/prices/{storeID}/{articleID}?page=1&pageSize=3
 
the response example:

    {
        "generated_date": "2023-11-22T16:44:50Z",
        "article": "1000102674",
        "store": "7001",
        “meta” : {
               “page” : 1,
               “size” : 3
         }
         “properties” : {
        	"uom": "EA",
        	“description”: “WH Halifax Passage Lever in Satin Nickel”,
        	“brand”: “Weiser”,
        	“model”: “9GLA1010”
        },
        "prices": [ 
{
            "type": "retail",
            "subtype": "regular",
            "currency": "CAD",
            "amount": 30.0,
            "valid_from": "2023-12-31T23:59:59Z",
            "valid_to": "9999-12-31T23:59:59Z"
},
{
            "type": "retail",
            "subtype": "discounted",
            "currency": "CAD",
            "amount": 27.0,
            "valid_from": "2023-12-21T23:59:59Z",
            "valid_to": "2025-12-31T23:59:58Z"
},
{
            "type": "retail",
            "subtype": "discounted",
            "currency": "CAD",
            "amount": 26.5,
            "valid_from": "2023-12-21T23:59:59Z",
            "valid_to": "2025-12-25T23:59:58Z",
}
       ]
    }
 




-	Please note that prices can have overlapping validity ranges. For application to check the prices and 
a.	mark prices having overlapping validity ranges and different price values as “overlapped” : true. In the example above second and 3rd prices will be marked as “overlapped”
b.	merge prices (two or more) having overlapping validity ranges and equal price values into a single price with largest combined validity range
c.	create set of unit tests to validate a and b logic above. 
-	In case and the requested price is not found respond with following 
{
    "type": "Not_Found",
    "title": "Unavailable prices",
    "status": 404,
    "detail": "No prices were found for a given request
}
-	Please use in-memory DB 
<dependency> 
<groupId>com.h2database</groupId> 
<artifactId>h2</artifactId> 
<scope>runtime</scope> 
</dependency>
-	Please populate required data into a DB tables on application start automatically
-	Please come up with the data model for this assignment (for you to decided what would it be)
-	For the application to use in-memory cache that could be implemented using Map interface. Serve prices from cache and if not found fall back to fetch prices from the DB
-	It should be a production ready code with comments , logs, swagger url should be available ( http://localhost:8083/{context-path}/swagger-ui.html and / or http://localhost:8083/v3/api-docs)

The acceptance criteria:
1.	working project that could be executed on the local computer and serve the API call from Postman or any other http client 
2.	use pom.xml – for maven 
3.	comments, errors handling, clean project structure 
4.	time for completion no more than 4 hr
5.	please ZIP the project and put back on the shared drive (same place you got an assignment from)


Attached are example unit tests that should pass to satisfy the price merging/overlapping logic (please use those test cases as a clue):
 @Test
public void testNonOverlappingPrices() {
    Price price1 = createPrice("retail", "regular", 30.0, "2023-01-01T00:00:00Z", "2023-01-31T23:59:59Z");
    Price price2 = createPrice("retail", "discounted", 25.0, "2023-02-01T00:00:00Z", "2023-02-28T23:59:59Z");

    List<Price> result = pricingService.processOverlappingPrices(Arrays.asList(price1, price2));

    assertEquals(2, result.size());
    assertFalse(result.get(0).isOverlapped());
    assertFalse(result.get(1).isOverlapped());
}

@Test
public void testExactOverlappingPricesWithSameAmount() {
    Price price1 = createPrice("retail", "regular", 30.0, "2023-01-01T00:00:00Z", "2023-01-31T23:59:59Z");
    Price price2 = createPrice("retail", "regular", 30.0, "2023-01-15T00:00:00Z", "2023-02-15T23:59:59Z");

    List<Price> result = pricingService.processOverlappingPrices(Arrays.asList(price1, price2));

    assertEquals(1, result.size());
    assertEquals("2023-01-01T00:00Z", result.get(0).getValidFrom().toString());
    assertEquals("2023-02-15T23:59:59Z", result.get(0).getValidTo().toString());
    assertFalse(result.get(0).isOverlapped());
}

@Test
public void testOverlappingPricesWithDifferentAmounts() {
    Price price1 = createPrice("retail", "regular", 30.0, "2023-01-01T00:00:00Z", "2023-01-31T23:59:59Z");
    Price price2 = createPrice("retail", "discounted", 25.0, "2023-01-15T00:00:00Z", "2023-02-15T23:59:59Z");

    List<Price> result = pricingService.processOverlappingPrices(Arrays.asList(price1, price2));

    assertEquals(2, result.size());
    assertTrue(result.get(0).isOverlapped());
    assertTrue(result.get(1).isOverlapped());
}

@Test
public void testContinuousOverlappingPricesWithSameAmount() {
    Price price1 = createPrice("retail", "regular", 30.0, "2023-01-01T00:00:00Z", "2023-01-31T23:59:59Z");
    Price price2 = createPrice("retail", "regular", 30.0, "2023-02-01T00:00:00Z", "2023-02-28T23:59:59Z");

    List<Price> result = pricingService.processOverlappingPrices(Arrays.asList(price1, price2));

    assertEquals(2, result.size());
    assertEquals("2023-01-01T00:00Z", result.get(0).getValidFrom().toString());
    assertEquals("2023-01-31T23:59:59Z", result.get(0).getValidTo().toString());
    assertFalse(result.get(0).isOverlapped());
}


@Test
public void testAlexScenario() {
    Price price1 = createPrice("retail", "regular", 30.0, "2023-01-01T00:00:00Z", "2023-01-31T23:59:59Z");
    Price price2 = createPrice("retail", "regular", 29.0, "2023-01-15T00:00:00Z", "2023-02-15T23:59:59Z");
    Price price3 = createPrice("retail", "regular", 29.0, "2023-01-16T00:00:00Z", "2023-03-15T23:59:59Z");
    Price price4 = createPrice("retail", "regular", 29.0, "2023-01-14T00:00:00Z", "2023-03-17T23:59:59Z");
    Price price5 = createPrice("retail", "regular", 30.0, "2023-01-02T00:00:00Z", "2023-03-30T23:59:59Z");

    List<Price> result = pricingService.processOverlappingPrices(Arrays.asList(price1, price2, price3, price4, price5));

    assertEquals(2, result.size());
    assertEquals("2023-01-01T00:00Z", result.get(0).getValidFrom().toString());
    assertEquals("2023-03-30T23:59:59Z", result.get(0).getValidTo().toString());
    assertTrue(result.get(0).isOverlapped());
    assertEquals("2023-01-14T00:00Z", result.get(1).getValidFrom().toString());
    assertEquals("2023-03-17T23:59:59Z", result.get(1).getValidTo().toString());
    assertTrue(result.get(1).isOverlapped());
}

// Helper method to create Price objects easily
private Price createPrice(String type, String subtype, double amount, String validFrom, String validTo) {
    Price price = new Price();
    price.setType(type);
    price.setSubtype(subtype);
    price.setCurrency("CAD");
    price.setAmount(BigDecimal.valueOf(amount));
    price.setValidFrom(ZonedDateTime.parse(validFrom));
    price.setValidTo(ZonedDateTime.parse(validTo));
    //price.setOverlapped(false);
    return price;
}

