/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.parse;

/**
 *
 * @author denis
 */

import java.util.List;

import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.Sentence;
import edu.stanford.nlp.parser.lexparser.LexicalizedParser;
import edu.stanford.nlp.trees.GrammaticalStructure;
import edu.stanford.nlp.trees.GrammaticalStructureFactory;
import edu.stanford.nlp.trees.PennTreebankLanguagePack;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreePrint;
import edu.stanford.nlp.trees.TreebankLanguagePack;
import edu.stanford.nlp.trees.TypedDependency;

public class Dependency {
    
    	public static void main(String[] args) {
            LexicalizedParser lp = LexicalizedParser.loadModel("edu/stanford/nlp/models/lexparser/englishPCFG.ser.gz");
            lp.setOptionFlags(new String[]{"-maxLength", "80", "-retainTmpSubcategories", });

            String[] sent = { "This", "is", "an", "easy", "sentence", "." };
            List<CoreLabel> rawWords = Sentence.toCoreLabelList(sent);
            Tree parse = lp.apply(rawWords);
            parse.pennPrint();
            System.out.println();

            TreebankLanguagePack tlp = new PennTreebankLanguagePack();
            GrammaticalStructureFactory gsf = tlp.grammaticalStructureFactory();
            GrammaticalStructure gs = gsf.newGrammaticalStructure(parse);
            List<TypedDependency> tdl = gs.typedDependenciesCCprocessed();
            System.out.println(tdl);
            //System.out.println();

            //TreePrint tp = new TreePrint("penn,typedDependenciesCollapsed");
            // tp.printTree(parse);

            String sentence = "which movies were directed by Christopher Nolan";
            Tree t2 = lp.parse(sentence);
            System.out.println(t2.firstChild().toString());
            gs = gsf.newGrammaticalStructure(t2);
            tdl = gs.typedDependenciesCCprocessed();
            System.out.println(tdl); System.out.println(tdl.get(0).dep().nodeString());


    }
}
