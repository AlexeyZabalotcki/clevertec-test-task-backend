# __Project setup steps__
___
* ```git clone <username>/clevertec-test-task-backend.git ```
* go to the folder ```cd 'your project folder'```
* paste project url from the first step
* open the project in your IDE ```File->Open->'your project folder'```

# __To ```run``` application you need:__

* open folder with project in the terminal ```cd 'your project folder'```
* enter ```gradle build```
* enter ```docker compose up -d --build```

# __To ```stop``` application you need:__

* enter ```docker-compose down```

___
# __Steps for work with application:__

__Work with products:__
* For add product to the database you should go to ```http://localhost:8083/products/add```
* For get information about products in the database you should go to ```http://localhost:8083/products/all```
* You can get product from the database using product's id ```http://localhost:8083/products/id/<product's id in the db>```
* You can update information by ```http://localhost:8083/poducts/update```
* For delete product from the database you should go to  ```http://localhost:8083/products/delete/<product's id in the db>```

___Structure of the JSON in POST request to add a product:___ 
```
{
        "title": <String product name>,
        "producer": <producer>,
        "price": <BigDecimal price>,
        "discount": <boolean discount>
}
```

__Work with discount cards:__
* For add discount card to the database you should go to ```http://localhost:8083/card/add```
* For get information about discount cards in the database you should go to ```http://localhost:8083/card/```
* You can get discount card from the database using product's id ```http://localhost:8083/card/id/<card's id in the db>```
* You can update information by ```http://localhost:8083/card/update```
* For delete discount card from the database you should go to  ```http://localhost:8083/card/delete/<card's id in the db>```

___Structure of the JSON in POST request to add a card:___ 
```
{
        "number": <Integer number>,
        "color": <color>,
        "producer":<Producer>,
        "discount": <boolean discount>
}
```
__In this application default cache type is LRU, but it could change to LFU__

* Both products and cards cached
* Maximum cache capacity and cache type specified in the `application.yml` file
* Cache type can be changed in `application.yml`  

__For getting information about maximum cache capacity and cahe size:__

* GET request ```http://localhost:8083/cache/``` and you could get information in XML format