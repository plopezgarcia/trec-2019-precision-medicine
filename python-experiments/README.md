Place a **config.json** file under [trec_utils](https://github.com/plopezgarcia/trec-2019-precision-medicine/tree/master/python-experiments/trec_utils) with the following information:

```json
{
  "ELASTIC" : "http://your-elasticsearch-server:9200",
  "ABSTRACTS" : "/abstracts-index",
  "TRIALS" : "/trials-index"
}
```

Note: this **config.json** file has been added to .gitignore to prevent the accidental exposure of an elasticsearch server
