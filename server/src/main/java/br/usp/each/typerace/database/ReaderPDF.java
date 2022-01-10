package br.usp.each.typerace.database;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.encryption.AccessPermission;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.File;
import java.io.IOException;
import java.util.*;
/**
 * Class that read all words in a .pdf file and store this in a Set of Strings
 *
 * */
public class ReaderPDF {

    private String path;
    private String fullText;
    private Set<String[]> utilDB;

    /**
     * @param path : The path to the file.pdf
     * */
    public ReaderPDF(String path) {
        this.path = path;
        this.fullText = "";
        this.utilDB = new HashSet<>();
    }

    /**
     * read all the pdf and store them in one only String.
     * */
    public void gerateFullText(){
        File file = new File(path);
        try {
            PDDocument document = Loader.loadPDF(file);
            AccessPermission ap = document.getCurrentAccessPermission();

            if(!ap.canExtractContent())
                throw new IOException("You do not have permission to extract text");

            PDFTextStripper tracy = new PDFTextStripper(); //HIMYM S01E09
            tracy.setSortByPosition(true);
            tracy.setStartPage(0);
            tracy.setEndPage(document.getNumberOfPages() -1);
            this.fullText = tracy.getText(document);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Read the string of the full text and get the words in a Set of Strings.
     * */
    public void gerateUtilDB(){
            String[] temp = this.fullText.split("\\s");
        for(String word: temp){
           // System.out.println(word);
            if(word.length() > 5){
                String result = word.replaceAll("\\p{Punct}", "");
                String[] wordArray = {result.toLowerCase()};
                this.utilDB.add(wordArray);
            }
        }

    }


    /**
     * Get the String of path
     *
     * @return String path
     * */
    public String getPath() {
        return path;
    }

    /**
     * Set the path to a new path
     *
     * @param path: new path of file
     * */
    public void setPath(String path) {
        this.path = path;
    }

    /**
     * Get the String of Full Text
     *
     * @return String fullText
     * */
    public String getFullText() {
        return fullText;
    }

    /**
     * Set the Full Text to be convert in text
     *
     * @param fullText: new FullText
     * */

    public void setFullText(String fullText) {
        this.fullText = fullText;
    }

    /**
     * Get the Set of all words
     *
     * @return Collection "<String[]>" path
     * */
    public Collection<? extends String[]> getUtilDB() {
        return utilDB;
    }

    /**
     * Set the collection of String
     *
     * @param utilDB: new collection of String
     * */
    public void setUtilDB(Set<String[]> utilDB) {
        this.utilDB = utilDB;
    }
}
