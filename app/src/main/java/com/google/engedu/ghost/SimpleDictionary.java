package com.google.engedu.ghost;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Random;

public class SimpleDictionary implements GhostDictionary {
    private ArrayList<String> words;
    private Random random=new Random();

    public SimpleDictionary(InputStream wordListStream) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(wordListStream));
        words = new ArrayList<>();
        String line = null;
        while((line = in.readLine()) != null) {
            String word = line.trim();
            if (word.length() >= MIN_WORD_LENGTH)
              words.add(line.trim());
        }
    }

    @Override
    public boolean isWord(String word) {
        return words.contains(word);
    }

    @Override
    public String getAnyWordStartingWith(String prefix) {
      /*        for (String s:words ) {
              if (s.startsWith(prefix))
                  return s;
           }*/
           if (prefix==null || prefix.equals("")){
               return words.get(random.nextInt(words.size()));
           }
        int l=0,r=words.size()-1,mid;
        while (l<=r){
            mid=(l+r)/2;
            if (words.get(mid).startsWith(prefix)){
                return words.get(mid);
            }
            if (words.get(mid).compareTo(prefix)>0){
                r=mid+1;
            }
            else{
                l=mid+1;
            }
        }

        return null;
    }

    @Override
    public String getGoodWordStartingWith(String prefix) {
        String selected = null;
        return selected;
    }
}
