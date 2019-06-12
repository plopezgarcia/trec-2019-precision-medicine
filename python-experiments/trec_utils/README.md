TREC utilities in python, based on the pytrec_eval package.

Place your **config.json** file here with the following information:

```json
{
  "ELASTIC" : "http://your-elasticsearch-server:9200",
  "ABSTRACTS" : "/abstracts",
  "TRIALS" : "/trials"
}
```

Note: this config.json file has been added to .gitignore to prevent the accidental exposure of an elasticsearch server
