# Enca-Eats

https://docs.microsoft.com/en-us/azure/spring-cloud/spring-cloud-howto-github-actions?pivots=programming-language-java#build-the-workflow-1

1. Create app subscription -- This will be what manages all 
of our deploys
 
```az appservice plan create -g enca-eats -n enca-eats-app-service-plan --is-linux --sku F1```

2. Create web app -- Each service will need to have a webapp

```az webapp create -g enca-eats -p enca-eats-app-service-plan -n enca-eats-backend --runtime "TOMCAT|8.5-jre8"```

3. Deploy -- This specific repo

```mvn package azure-webapp:deploy```

join
```sql
SELECT r.id 
FROM restaurants r 
JOIN c in r.categories
WHERE c.alias = 'sushi'
```

### Credentials for github user in Github Actions

```
az ad sp create-for-rbac --name "myApp" --role contributor \
                            --scopes /subscriptions/3ec4ecf2-6e32-4101-886d-de883794b499/resourceGroups/enca-eats \
                            --sdk-auth
```                          