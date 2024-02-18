
# Github-user

Github-user is simple application that returns repository data for specified Github account Owner.
The application has one GET request method that takes Owner's username and returns data from all public repositories that are not forks.

Application returns JSON file with name and owner of all public repositories as well as names of all branches and sha of the last commit for every branch.


## API Reference

### Get Github Repositories Data

```http
  GET /
```

| Parameter | Type     | Description                |
| :-------- | :------- | :------------------------- |
| `user_name`    | `string` | **Required**. Github Owner's username |

Takes Owner's username and returns data about github repositories.

#### curl

```http
  curl -X 'GET' \
  'http://localhost:8080/?user=user_name' \
  -H 'accept: */*'
```
#### Response

```http
{
  "repositories": [
    {
      "repository_name": "string",
      "owner": "string",
      "branches": [
        {
          "branch_name": "string",
          "last_commit_sha": "string"
        }
      ]
    }
  ]
}
```