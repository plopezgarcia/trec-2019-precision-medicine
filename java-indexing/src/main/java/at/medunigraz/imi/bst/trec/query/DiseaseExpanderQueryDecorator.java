package at.medunigraz.imi.bst.trec.query;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.mashape.unirest.http.exceptions.UnirestException;

import at.medunigraz.imi.bst.lexigram.GraphUtils;
import at.medunigraz.imi.bst.trec.model.Result;
import at.medunigraz.imi.bst.trec.model.Topic;

public class DiseaseExpanderQueryDecorator extends QueryDecorator {

	private static final Logger LOG = LogManager.getLogger();

	public DiseaseExpanderQueryDecorator(Query decoratedQuery) {
		super(decoratedQuery);
	}

	@Override
	public List<Result> query(Topic topic) {
		expandDisease(topic);
		return decoratedQuery.query(topic);
	}

	private void expandDisease(Topic topic) {
		String disease = topic.getDisease();

		List<String> synonyms = null;
		try {
			synonyms = GraphUtils.addSynonymsFromBestConceptMatch(disease);
		} catch (UnirestException e) {
			e.printStackTrace();
			return;
		}
		
		String expandedDisease = String.join(" ", synonyms);
		topic.withDisease(expandedDisease);
		
		LOG.debug(disease + " changed to " + expandedDisease);
	}

}
