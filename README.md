
**Design**
--
Technologies used: **Grails**

This is a poc for book recommendation service implemented purely using grails framework.

The core of the recommedation engine is BookRecommender interface along with [UserBasedJdbcBookRecommender](https://github.com/snimavat/zenjob/blob/master/src/main/groovy/com/zenjob/recommender/UserBasedJdbcBookRecommender.groovy) 
implementation.

There's a Mahout based [recommender](https://github.com/snimavat/zenjob/blob/master/src/main/groovy/com/zenjob/recommender/MahoutBookRecommender.groovy) implementation which works but has been commented out to keep the dependencies 
simple.


**User preferences** :
User's preference for a book (like/dislike) is represented by domain [UserPreference](https://github.com/snimavat/zenjob/blob/master/grails-app/domain/com/zenjob/recommender/UserPreference.groovy).
preference value of 1 represents like and -1 represents dislike.



****UserBasedJdbcBookRecommender****

It is a jdbc based recommender implementation which works by identifying neighbourhood of similar users based on the 
likes/dislikes. 

Two user's are considered as similar if they have liked or disliked same books.
Recommendations are made from the books liked by similar users but not yet rated by given user. 

Weightage is assigned to each similar user based on the number of similar preferences (likes/dislikes). Books from a 
user with higher weightage will rank higher. 

```zenjob.recommender.proximityThreshold``` config can be used to specify how many preferences should match before a 
user is
 considered as similar user, default value is 3


**How to run**

```gradle bootRun``` or ```grails run-app```


**Note:** Sample books data is put inside ```data/books.txt``` which is imported on application startup. 

****Endpoints****
--

App exposes following Rest endpoints 

****like/dislike a book****

```POST http://localhost:8080/books/$id/preferences```

****body****
```json
{"userId":2, "preference":1}
```


```
curl -X POST -H 'Accept: application/json' -H 'Content-Type: application/json' -i http://localhost:8080/books/1/preferences --data '{"userId":1, "preference":1}'
```

This endpoint creates a like/dislike for the book

**Note**: preference = 1 represents like, and preference = -1 represents dislike




****User's preferences list****

```GET http://localhost:8080/users/$id/preferences```


```
curl -X GET -H 'Accept: application/json' -i http://localhost:8080/users/1/preferences
```



This endpoint returns list of likes/dislikes for the user.

****Recommendations****

```GET http://localhost:8080/users/$id/recommendations```

```
curl -X GET -H 'Accept: application/json' -i http://localhost:8080/users/1/recommendations
```


This endpoint returns list of recommended books for the given user.

