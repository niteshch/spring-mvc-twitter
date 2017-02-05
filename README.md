# REST API for Twitter data
Interacting with twitter data in MySQL database using Spring MVC


# Twitter User REST API
API to access twitter user data. Following operations are supported
1. An endpoint to read the tweets for a given user (includes tweets of the user and the people being followed by the user). An extra “search=” argument can be used to further filter tweets based on keyword.
2. Endpoints to get the list of people a user is following as well as the followers of the user.
3. An endpoint to start following another user.
4. An endpoint to unfollow another user.
