package at.medunigraz.imi.bst.config;

import java.util.ResourceBundle;

public final class TrecConfig {	
	private static final ResourceBundle PROPERTIES = ResourceBundle.getBundle("config");

    /* STORAGE - ELASTICSEARCH */

    public static final String INDEX_NAME = "abstracts";
    public static final String MEDLINE_TYPE = "medline";
    public static final String EXTRA_TYPE = "extra";
    public static final String INDEX_TRIALS_NAME = "trials";
    public static final String TRIALS_TYPE = "trials";
    
    public static final String ELASTIC_HOSTNAME = getString("ELASTIC_HOSTNAME");
    public static final int ELASTIC_PORT = 9300;
    
    


    /* DATA - MEDLINE */

    public static final String MEDLINE_FILE_PATTERN = "pubmed19n0%03d.xml";

    public static final int MEDLINE_FILE_END = 972;


    public static final String SAMPLE_SMALL_XML = "src/main/resources/data/medline-sample.xml";
    public static final String SAMPLE_EXTRA_ABSTRACT_TXT = "src/main/resources/data/extra-abstract-sample.txt";

	
	public static String getString(String key) {
		return PROPERTIES.getString(key);
	}

}
