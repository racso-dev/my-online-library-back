package com.marketpay.job.parsing.coda;

import com.marketpay.job.parsing.ParsingJob;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.*;

/**
 * Created by etienne on 03/07/17.
 */
public class ParsingCODAJob extends ParsingJob {

    /**
     * Permet de découper le fichier en block de n relevés
     * @param filepath : path du fichier à parser
     */
    public static void getBlocksFromCodaFile(String filepath) {
        try {
            FileReader input = new FileReader(filepath);
            BufferedReader buffer = new BufferedReader(input);
            ArrayList<String> myList = new ArrayList<>();
            String line;
            Pattern endBlockPattern = Pattern.compile("^9 .*");
            while ((line = buffer.readLine()) != null) {
                myList.add(line);
                Matcher matcher = endBlockPattern.matcher(line);
                if (matcher.find()) {
                    ParsingCODAJob.traitementDuBlock(myList);
                    // Ecrasement de la liste
                    myList.clear();
                }
            }

            // Fermeture du fichier
            buffer.close();
            input.close();


        } catch (FileNotFoundException e) {
            // TODO: Log file not found
            e.printStackTrace();

        } catch (IOException e) {
            // TODO: Log can't close file
            e.printStackTrace();
        }
    }

    private static void traitementDuBlock(ArrayList list) {
        System.out.println(list.size());
    }
}
