# CD Flow Backend

Simple backend to capture, aggregate and calculate CD metrics

For this to work you need a PostgreSQL database of your choice and the Helm Chart will look for a kubernetes secret called `db` with the following keys:

```yaml
apiVersion: v1
kind: Secret
metadata:
  name: db
data:
  db-name: XXX 
  db-url: XXX
  db-password: XXX
```

touch 
