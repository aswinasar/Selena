/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.wordNet;

/**
 *
 * @author denis
 */
import java.util.HashSet;

import edu.smu.tspell.wordnet.AdjectiveSynset;
import edu.smu.tspell.wordnet.NounSynset;
import edu.smu.tspell.wordnet.Synset;
import edu.smu.tspell.wordnet.SynsetType;
import edu.smu.tspell.wordnet.VerbSynset;
import edu.smu.tspell.wordnet.WordNetDatabase;

public class WordNet {

public static HashSet<String>getSisterTerms(String word, String wordType ){
HashSet<String> words = new HashSet<String>();
WordNetDatabase database = WordNetDatabase.getFileInstance();
Synset[] synsets;
if(wordType.equalsIgnoreCase("NOUN")){
synsets = database.getSynsets(word, SynsetType.NOUN);
for (int i = 0; i < synsets.length; i++) {
NounSynset nounSynset = (NounSynset)(synsets[i]);	
for(Synset s : nounSynset.getHypernyms())	
for(String w: s.getWordForms())
words.add(w);
}
}
else if(wordType.equalsIgnoreCase("VERB")){
synsets = database.getSynsets(word, SynsetType.VERB);
for (int i = 0; i < synsets.length; i++) {
VerbSynset verbSynset = (VerbSynset)(synsets[i]);	
for(Synset s : verbSynset.getHypernyms())	
for(String w: s.getWordForms())
words.add(w);
}
}
else if(wordType.equalsIgnoreCase("ADJECTIVE")){
synsets = database.getSynsets(word, SynsetType.ADJECTIVE);
for (int i = 0; i < synsets.length; i++) {
AdjectiveSynset adjSynset = (AdjectiveSynset)(synsets[i]);	
for(Synset s : adjSynset.getSimilar())	
for(String w: s.getWordForms())
words.add(w);
}
}

return words;

}
}