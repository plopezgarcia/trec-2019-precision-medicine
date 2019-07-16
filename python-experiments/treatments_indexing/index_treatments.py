import csv
from urllib.parse import urlparse
import os, sys
nb_dir = os.path.split(os.getcwd())[0]
if nb_dir not in sys.path:
    sys.path.append(nb_dir)
from trec_utils import utils
from elasticsearch import Elasticsearch
from elasticsearch import helpers

config = utils.load_config()
url_info = urlparse(config['ELASTIC'])

es = Elasticsearch([{'host':url_info.hostname,'port':url_info.port}])

def index_file(file):
    with open(file) as tsvfile:
        reader = csv.reader(tsvfile, delimiter='\t')
        fake_doc = {
                'pubmedId': '0',
                'treatments': {'faketreatment':1},
                'cuis': {'FAKECUI':1}
              }

        doc = fake_doc

        actions = []
        
        for row in reader: 
            try: 
                pubmedId, cui, treatment = row[0], row[1], row[2]

                if pubmedId==doc['pubmedId']:
                    if treatment in doc['treatments']:
                        doc['treatments'][treatment] = doc['treatments'][treatment] + 1
                    else:
                        doc['treatments'][treatment] = 1
                    if cui in doc['cuis']:
                        doc['cuis'][cui] = doc['cuis'][cui] + 1
                    else:
                        doc['cuis'][cui] = 1
                else:
                    # Transform dictionaries to list to avoid Elasticsearch issues
                    doc_to_save = {
                        'pubmedId': doc['pubmedId'],
                        'treatments': [ [k,str(v)] for k, v in doc['treatments'].items() ],
                        'cuis': [ [k,str(v)] for k, v in doc['cuis'].items() ]
                      }
                    #print(doc)
                    #print(doc) # INDEX DOC
                    #res = es.index(index="treatments", doc_type='treatments', id=int(pubmedId), body=doc)
                    #print(int(pubmedId))
                    actions.append({
                        "_id": doc_to_save['pubmedId'],
                        "_op_type": "index",
                        "_index": "treatments",
                        "_type": "treatments",
                        "_source": doc_to_save
                    })
                    doc = {'pubmedId':pubmedId,'treatments':{treatment:1}, 'cuis':{cui:1}}
            except:
                print("Exception, line blank?")
                continue
    
    helpers.bulk(es, actions)
    print(file + " indexed!")
    
# Download and extract this file here
# http://www.trec-cds.org/medline_treatments.tar.gz
def index_all_files():
    i = 0
    for file in os.listdir(nb_dir + "/treatments_indexing/medline_treatments"):
        if file.endswith(".txt"):
            file = nb_dir + "/treatments_indexing/medline_treatments/" + file
            index_file(file)
            i = i + 1
    print("Finished! - Files:", i)
    
if __name__ == "__main__":
    index_all_files()