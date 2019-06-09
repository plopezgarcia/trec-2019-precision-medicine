package at.medunigraz.imi.bst.trec.stats;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

import at.medunigraz.imi.bst.trec.model.Metrics;

public class XMLStatsWriterTest {

	private final File actualFile = new File("stats/test.xml");
	private final File expectedFile = new File(getClass().getResource("/stats/expected.xml").getFile());

	@Test
	public void testWrite() throws IOException {
		StatsWriter writer = new XMLStatsWriter(actualFile);

		Metrics metrics = new Metrics();
		metrics.put("ndcg", 0.5);

		Map<String, Metrics> metricsByTopic = new HashMap<>();
		metricsByTopic.put("all", metrics);

		writer.write(metricsByTopic);
		writer.close();

		String expected = FileUtils.readFileToString(expectedFile, "UTF-8");
		String actual = FileUtils.readFileToString(actualFile, "UTF-8");

		assertEquals(expected, actual);
	}

}
