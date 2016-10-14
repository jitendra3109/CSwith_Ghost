package com.google.engedu.ghost;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;


public class TrieNode {
    private HashMap<Character, TrieNode> children;
    private boolean isWord;
    private Random random =new Random();
      public TrieNode() {
        children = new HashMap<>();
        isWord = false;
    }

    public void add(String s) {
        HashMap<Character,TrieNode>temp=children;
        for (int i=0;i<s.length();i++){
            if (!temp.containsKey(s.charAt(i))){
                temp.put(s.charAt(i),new TrieNode());
            }
            if (i==s.length()-1){
                temp.get(s.charAt(i)).isWord = true;
            }
            temp=temp.get(s.charAt(i)).children;
        }
    }

    public boolean isWord(String s) {
        TrieNode temp=searchNode(s);
        if (temp==null){
            return false;
        }else{
            return temp.isWord;
        }

    }
    private TrieNode searchNode(String s){
        TrieNode temp=this;
        for (int i=0;i<s.length();i++) {
            if (!temp.children.containsKey(s.charAt(i))) {
                return null;
            }
            temp = temp.children.get(s.charAt(i));
        }
        return temp;
    }

    public String getAnyWordStartingWith(String s) {
        TrieNode temp=searchNode(s);
         if (temp==null){
             return null;
         }
        while (!temp.isWord){
            for (Character c:temp.children.keySet()) {
                temp=temp.children.get(c);
                s+=c;
                break;
            }

            }
        return s;
    }

    public String getGoodWordStartingWith(String s) {
        TrieNode temp = searchNode(s);
        if (temp == null) {
            return null;
        }
        ArrayList<Character>charNoWord=new ArrayList<>();
        ArrayList<Character>charWord=new ArrayList<>();
        Character c;
        while (true){
            charNoWord.clear();;
            charWord.clear();
            for (Character ch:temp.children.keySet()) {
                if (temp.children.get(ch).isWord){
                    charWord.add(ch);
                }else {
                    charNoWord.add(ch);
                }

            }
            if (charNoWord.size()==0){
                s+=charWord.get(random.nextInt(charWord.size()));
                break;
            }else{
                c=charNoWord.get(random.nextInt(charNoWord.size()));
                s+= c;
                temp=temp.children.get(c);
            }
        }
    return s;
    }
}
