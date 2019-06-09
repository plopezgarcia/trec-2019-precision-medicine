package at.medunigraz.imi.bst.trec.query;

import java.util.List;
import java.util.Map;

import at.medunigraz.imi.bst.trec.model.Result;
import at.medunigraz.imi.bst.trec.model.Topic;

public class StaticMapQueryDecorator extends MapQueryDecorator {
	
	private Map<String, String> keymap;

	public StaticMapQueryDecorator(Map<String, String> keymap, Query decoratedQuery) {
		super(decoratedQuery);
		this.keymap = keymap;
	}
	
	@Override
	public List<Result> query(Topic topic) {
		map(keymap);
		return decoratedQuery.query(topic);
	}
	
	@Override
	protected String getMyName() {
		return getSimpleClassName() + "(" + keymap.values() + ")";
	}
}
