package com.marketpay.job.parsing.repositoryshop;

import com.marketpay.job.parsing.ParsingJob;
import com.marketpay.job.parsing.repositoryshop.resource.ShopCsvResource;
import com.marketpay.persistence.entity.JobHistory;
import com.marketpay.persistence.repository.JobHistoryRepository;
import com.marketpay.references.JOB_STATUS;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.supercsv.io.CsvBeanReader;
import org.supercsv.io.ICsvBeanReader;
import org.supercsv.prefs.CsvPreference;

import java.io.FileReader;
import java.io.IOException;
import java.util.List;

/**
 * Created by etienne on 24/07/17.
 */
@Component
public class ParsingRepositoryShopJob extends ParsingJob {

    private final Logger LOGGER = LoggerFactory.getLogger(ParsingRepositoryShopJob.class);

    @Autowired
    private JobHistoryRepository jobHistoryRepository;

    @Override
    public void parsing(String filePath, JobHistory jobHistory) throws IOException {
        //On récupère le fichier csv et on initialise le reader
        ICsvBeanReader csvBeanReader = null;
        try {
            csvBeanReader = new CsvBeanReader(new FileReader(filePath), CsvPreference.EXCEL_NORTH_EUROPE_PREFERENCE);

            //On récupère le header
            final String[] header = csvBeanReader.getHeader(true);

            ShopCsvResource shopCsv;
            while ((shopCsv = csvBeanReader.read(ShopCsvResource.class, header)) != null) {
                System.err.println(shopCsv.getNum_Contrat() + " " + shopCsv.getCode_BU());
                //TODO ETI
            }

        } catch (Exception e) {
            //TODO ETI
        } finally {
            if(csvBeanReader != null){
                csvBeanReader.close();
            }
        }


        //Pour chaque ligne on met à jour la table shop et business_unit
        //TODO ETI
    }

    @Override
    protected void errorBlock(Exception e, List<String> block, JobHistory jobHistory) {
        // S'il y a une erreur on invalid le fichier referentiel
        LOGGER.error("Une erreur s'est produite pendant le parsing du fichier referentielle : ", e);
        jobHistory.setStatus(JOB_STATUS.FAIL.getCode());
        jobHistory.addError((e.getMessage()));
        jobHistoryRepository.save(jobHistory);
    }
}
