package com.company;

import java.util.Arrays;

public class SentimentAnalyzer {
    // labeled continue can be used when iterating featureSet + convert review to lower-case
    public static int[] detectProsAndCons(String review, String[][] featureSet, String[] posOpinionWords,
                                          String[] negOpinionWords) {
        int[] featureOpinions = new int[featureSet.length]; // output
        String feature = "";

        nextFeature:for(int a = 0; a<featureSet.length; a++) {
            for (int b = 0; b < featureSet[a].length; b++){
                if(review.toLowerCase().contains(featureSet[a][b])){
                    feature = featureSet[a][b].toString();
                    featureOpinions[a] = getOpinionOnFeature(review, feature, posOpinionWords, negOpinionWords);
                    continue nextFeature;
                }
            }
        }

        return featureOpinions;
    }

    // First invoke checkForWasPhrasePattern and
    // if it cannot find an opinion only then invoke checkForOpinionFirstPattern
    private static int getOpinionOnFeature(String review, String feature, String[] posOpinionWords, String[] negOpinionWords) {
        int result = checkForWasPhrasePattern(review, feature, posOpinionWords, negOpinionWords);
        if(result != 1){
            result = checkForOpinionFirstPattern(review, feature, posOpinionWords, negOpinionWords);
        }
        return result;
    }

    private static int checkForWasPhrasePattern(String review, String feature, String[] posOpinionWords,
                                                String[] negOpinionWords) {
        int opinion = 0;

        String pattern = feature + " was ";
        // your code
        for(int i = 0; i<posOpinionWords.length; i++){
            if(review.contains(pattern + posOpinionWords[i])){
                opinion = 1;
                break;
            }

        }
        for(int j = 0; j<negOpinionWords.length; j++){
            if(review.contains(pattern + negOpinionWords[j])){
                opinion = -1;
                break;
            }
        }
        return opinion;
    }
    // You can first look for positive opinion. If not found, only then you can look for negative opinion
    private static int checkForOpinionFirstPattern(String review, String feature, String[] posOpinionWords,
                                                   String[] negOpinionWords) {

        String[] sentences = review.toLowerCase().split("\\.");
        int opinion = 0;
        String pattern = " " + feature;

        labelBreak:for(int x = 0; x<sentences.length; x++){
            for(int y = 0; y<posOpinionWords.length; y++){
                if(sentences[x].contains(posOpinionWords[y] + pattern)){
                    opinion = 1;
                    break labelBreak;
                }
            }
            for(int z = 0; z<negOpinionWords.length; z++){
                if(sentences[x].contains(negOpinionWords[z] + pattern)){
                    opinion = -1;
                    break labelBreak;
                }
            }
        }

        // your code for processing each sentence. You can return if opinion is found in a sentence (no need to process subsequent ones)
        return opinion;
    }

    public static void main(String[] args) {
        String review = "Haven't been here in years! Fantastic service and the food was delicious! Definetly will be a frequent flyer! Francisco was very attentive";

        //String review = "Sorry OG, but you just lost some loyal customers. Horrible service, no smile or greeting just attitude. The breadsticks were stale and burnt, appetizer was cold and the food came out before the salad.";

        String[][] featureSet = {
                { "ambiance", "ambience", "atmosphere", "decor" },
                { "dessert", "ice cream", "desert" },
                { "food" },
                { "soup" },
                { "service", "management", "waiter", "waitress", "bartender", "staff", "server" } };
        String[] posOpinionWords = { "good", "fantastic", "friendly", "great", "excellent", "amazing", "awesome",
                "delicious" };
        String[] negOpinionWords = { "slow", "bad", "horrible", "awful", "unprofessional", "poor" };

        int[] featureOpinions = detectProsAndCons(review, featureSet, posOpinionWords, negOpinionWords);
        System.out.println("Opinions on Features: " + Arrays.toString(featureOpinions));
    }
}
