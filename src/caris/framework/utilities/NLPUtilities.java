package caris.framework.utilities;

import java.io.IOException;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;

import java.util.Properties;

import edu.stanford.nlp.io.IOUtils;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.parser.nndep.DependencyParser;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.AnnotationPipeline;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.stanford.nlp.semgraph.SemanticGraphCoreAnnotations;
import edu.stanford.nlp.util.CoreMap;
import edu.stanford.nlp.util.PropertiesUtils;
import edu.stanford.nlp.util.logging.Redwood;

public class NLPUtilities {
	
	public static void tag(String text) throws IOException, ClassNotFoundException {
		/* MaxentTagger mt = new MaxentTagger("taggers/left3words-wsj-0-18.tagger");
		String tagged = mt.tagString(text);
		System.out.println(tagged); */

	    Annotation ann = new Annotation(text);

	    Properties props = PropertiesUtils.asProperties(
	            "annotators", "tokenize,ssplit,pos,depparse",
	            "depparse.model", DependencyParser.DEFAULT_MODEL
	    );

	    AnnotationPipeline pipeline = new StanfordCoreNLP(props);

	    pipeline.annotate(ann);

	    for (CoreMap sent : ann.get(CoreAnnotations.SentencesAnnotation.class)) {
	      SemanticGraph sg = sent.get(SemanticGraphCoreAnnotations.BasicDependenciesAnnotation.class);
	      log.info(IOUtils.eolChar + sg.toString(SemanticGraph.OutputFormat.LIST));
	    }
	}
	
}