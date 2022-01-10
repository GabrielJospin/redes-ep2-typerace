package br.usp.each.typerace.database;

import com.opencsv.CSVWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Class to write the .csv with all the words
 * */
public class WriterCSV {


    private String pathRead, pathWrite;
    private static final Logger LOGGER = LoggerFactory.getLogger(WriterCSV.class);

    /**
     * @param pathRead: Path of the file .pdf to be read
     * @param pathWrite: Path of the file .csv to be written
     * */
    public WriterCSV(String pathRead, String pathWrite) {
        this.pathRead = pathRead;
        this.pathWrite = pathWrite;
    }

    /**
     * Read the pdf file with {@link br.usp.each.typerace.database.ReaderPDF}
     * and write a .csv with the words of the file
     * */
    public void generate(){
        String[] cabecalho = {"Palavras"};
        ReaderPDF readerPDF = new ReaderPDF(pathRead);
        readerPDF.gerateFullText();
        LOGGER.info("Text Gerate");
        readerPDF.gerateUtilDB();
        LOGGER.info("DB Gerate");
        List<String[]> linhas = new ArrayList<>(readerPDF.getUtilDB());

        try {
            Writer writer = Files.newBufferedWriter(Paths.get(pathWrite));
            CSVWriter csvWriter = new CSVWriter(writer);
            //csvWriter.writeNext(cabecalho);
            csvWriter.writeAll(linhas);
            csvWriter.flush();
            writer.close();
            LOGGER.info(".csv Write");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Get the String of path to be read
     *
     * @return String pathRead
     * */
    public String getPathRead() {
        return pathRead;
    }

    /**
     * Set the pathRead to a new pathRead
     *
     * @param pathRead: new path of file to be read
     * */
    public void setPathRead(String pathRead) {
        this.pathRead = pathRead;
    }

    /**
     * Get the String of path to be written
     *
     * @return String pathWrite
     * */
    public String getPathWrite() {
        return pathWrite;
    }

    /**
     * Set the pathWrite to a new pathWrite
     *
     * @param pathWrite: new path of file to be written
     * */
    public void setPathWrite(String pathWrite) {
        this.pathWrite = pathWrite;
    }
}
