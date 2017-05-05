package at.medunigraz.imi.bst.medline;

import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.node.Node;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.junit.Test;

import java.net.InetAddress;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

public class MedlineTests {

    String SAMPLE_XML_COMPRESSED_FILE = "/Volumes/PabloSSD/trec/medline_xml_all/medline17n0739.xml.gz";
    String SAMPLE_SMALL_XML_UNCOMPRESSED_FILE = "src/main/resources/data/medline-sample.xml";
    String SAMPLE_LARGE_XML_UNCOMPRESSED_FILE = "src/main/resources/data/medline17n0569.xml";

    @Test
    public void smallUncompressedTest() throws Exception {

        List<PubMedArticle> pubMedArticles = XmlPubMedArticleSet.getPubMedArticlesFromXml(SAMPLE_SMALL_XML_UNCOMPRESSED_FILE);
        System.out.println(pubMedArticles);
        System.out.println(pubMedArticles.size());
    }

    @Test
    public void largeUncompressedTest() throws Exception {

        List<PubMedArticle> pubMedArticles = XmlPubMedArticleSet.getPubMedArticlesFromXml(SAMPLE_LARGE_XML_UNCOMPRESSED_FILE);

        System.out.println(pubMedArticles);
        System.out.println(pubMedArticles.size());
    }

    @Test
    public void largeCompressedTest() throws Exception {

        List<PubMedArticle> pubMedArticles = XmlPubMedArticleSet.getPubMedArticlesFromGzippedXml(SAMPLE_XML_COMPRESSED_FILE);
        System.out.println(pubMedArticles);
        System.out.println(pubMedArticles.size());
    }

    @Test
    public void indexDoc() throws Exception {

        TransportAddress address =
                new InetSocketTransportAddress(
                        InetAddress.getByName("localhost"), 9300);

        Client client = new PreBuiltTransportClient(Settings.EMPTY).addTransportAddress(address);



        long startTime = System.currentTimeMillis();


        PubMedArticle article = new PubMedArticle("12345", "this is the title", "this is the abstract");

        IndexResponse response = client.prepareIndex("medline", "medline", article.pubMedId)
                .setSource(jsonBuilder()
                        .startObject()
                        .field("title", article.docTitle)
                        .field("abstract", article.docAbstract)
                        .endObject()
                )
                .get();

        System.out.print(response);

        client.close();

        long endTime = System.currentTimeMillis();

        long duration = (endTime - startTime);

        System.out.println("TIME: " + duration/1000 + " secs");
    }

    @Test
    public void uncompressAndIndex30Kdocs() throws Exception {

        TransportAddress address =
                new InetSocketTransportAddress(
                        InetAddress.getByName("localhost"), 9300);

        Client client = new PreBuiltTransportClient(Settings.EMPTY).addTransportAddress(address);

        long startTime = System.currentTimeMillis();

        List<PubMedArticle> pubMedArticles = XmlPubMedArticleSet.getPubMedArticlesFromGzippedXml(SAMPLE_XML_COMPRESSED_FILE);

        long endTime = System.currentTimeMillis();

        long xmlDuration = (endTime - startTime);

        System.out.println("UNCOMPRESSING + PARSING XML TIME: " + xmlDuration/1000 + " secs - " + pubMedArticles.size() + " articles");



        startTime = System.currentTimeMillis();

        for (PubMedArticle article: pubMedArticles) {
            IndexResponse response = client.prepareIndex("medline", "medline", article.pubMedId)
                    .setSource(jsonBuilder()
                            .startObject()
                            .field("title", article.docTitle)
                            .field("abstract", article.docAbstract)
                            .endObject()
                    )
                    .get();
        }

        client.close();

        endTime = System.currentTimeMillis();

        long indexingDuration = (endTime - startTime);

        System.out.println("INDEXING TIME: " + indexingDuration/1000 + " secs - " + pubMedArticles.size() + " articles");

        long total = xmlDuration + indexingDuration;

        System.out.println("TOTAL: " + TimeUnit.MILLISECONDS.toSeconds(total) + " seconds");

    }

}
