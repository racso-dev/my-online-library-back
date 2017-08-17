package com.marketpay.services.operation;

import be.quodlibet.boxable.BaseTable;
import be.quodlibet.boxable.Cell;
import be.quodlibet.boxable.Row;
import be.quodlibet.boxable.image.Image;
import be.quodlibet.boxable.line.LineStyle;
import com.marketpay.persistence.entity.Operation;
import com.marketpay.references.CARD_TYPE;
import com.marketpay.references.LANGUAGE;
import com.marketpay.references.OPERATION_SENS;
import com.marketpay.references.OPERATION_TYPE;
import com.marketpay.utils.I18nUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import javax.validation.constraints.Max;
import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

@Component
public class PdfOperationService {
    private final int MAX_LINE = 10;
    private final Color HEADER_COLOR =  new Color(17, 169, 157);
    private final Color HEADER_LIGHT_COLOR = new Color(147,202,196);

    private final int yStart = 400;
    private final int yStartNewPage = 1000;
    private final int bottomMargin = 100;
    private final int tableWidth = 1200;
    private final int xStart = 50;

    private long totalGrossAmount = 0;
    private long totalNetAmount = 0;
    private List<Operation> operationList;
    private PDDocument mainDocument;

    @Autowired
    private I18nUtils i18nUtils;

    @Autowired
    private ApplicationContext applicationContext;

    public PdfOperationService() {
    }

    public void setOperationList(List<Operation> operationList) {
        this.operationList = operationList;
    }

    /**
     * Crée le pdf et retourne le fichier
     * @param language: Utiliser pour la traduction i18n
     * @return : un document
     */
    public PDDocument getPdfDocument(LANGUAGE language, String buName, String shopName) {
        this.mainDocument = new PDDocument();
        // Si la liste est vide on renvoit directement le document
        if(operationList == null || operationList.isEmpty()) {
            return mainDocument;
        }

        for(int i = 0; i * MAX_LINE < operationList.size(); i++) {
            int startIndex = i * MAX_LINE;
            int endIndex = startIndex + MAX_LINE;
            // Si on arrive a la fin de la liste
            if(endIndex > operationList.size()) {
                endIndex = operationList.size();
            }
            List<Operation> subList = operationList.subList(startIndex, endIndex);
            createTable(subList, i == 0, endIndex == operationList.size(), language, buName, shopName);
        }

        return mainDocument;
    }

    /**
     * Fonction qui crée un tableau sur une page
     * @param operationList : 10 éléments max
     * @param isFirstPage : Permet de changer la couleur en fonction de si c'est la première page ou non
     * @param isFinalPage : Permet d'ajouter la ligne final avec le total
     * @param language : utilisé pour i18n
     */
    private void createTable(List<Operation> operationList, Boolean isFirstPage, Boolean isFinalPage, LANGUAGE language, String buName, String shopName) {

        // On crée la page
        PDPage myPage = new PDPage(new PDRectangle(PDRectangle.A4.getHeight(), PDRectangle.A4.getWidth()));
        Boolean isCoda = operationList.get(0).getCardType() != null;

        List<String> headerList = Arrays.asList(i18nUtils.getMessage("pdfOperationService.contractNumber", null, language),
            i18nUtils.getMessage("pdfOperationService.shop", null , language),
            i18nUtils.getMessage("pdfOperationService.tradeDate", null, language),
            isCoda ? i18nUtils.getMessage("pdfOperationService.cardType", null, language) : i18nUtils.getMessage("pdfOperationService.operationType", null, language),
            i18nUtils.getMessage("pdfOperationService.sens", null, language),
            i18nUtils.getMessage("pdfOperationService.grossAmount", null, language),
            i18nUtils.getMessage("pdfOperationService.commission", null, language),
            i18nUtils.getMessage("pdfOperationService.netAmount", null, language));


        try {
            // On crée le tableau
            BaseTable baseTable  = new BaseTable(yStart , yStartNewPage, bottomMargin, tableWidth, xStart, mainDocument, myPage, true, true);

            PDPageContentStream contentStream = new PDPageContentStream(mainDocument, myPage);

            contentStream.beginText();
            contentStream.setFont(PDType1Font.HELVETICA_BOLD, 20);
            contentStream.moveTextPositionByAmount(350, 500);
            contentStream.drawString(i18nUtils.getMessage("pdfOperationService.title", null, language));

            contentStream.endText();

            contentStream.beginText();
            contentStream.setFont(PDType1Font.TIMES_ROMAN, 12);
            contentStream.moveTextPositionByAmount(100, 430);
            // On met la BU
            String textToDraw =  "  " + i18nUtils.getMessage("pdfOperationService.bu", null, language) + " : " + buName;
            // On ajoute le shop
            textToDraw += "                " + i18nUtils.getMessage("pdfOperationService.shop", null, language) + " : " + shopName;
            // On ajoute la funding date
            textToDraw += "                  " + i18nUtils.getMessage("pdfOperationService.fundingDate", null, language) + " : " +  operationList.get(0).getFundingDate();
            contentStream.drawString(textToDraw);
            contentStream.endText();

            // Ajout de l'image dans le coin en haut à gauche du pdf
            Image image = new Image(ImageIO.read(applicationContext.getResource("classpath:img/cornerLogoMP-transparent.png").getInputStream()));
            image = image.scaleByWidth(140);
            image.draw(mainDocument, contentStream, 0, 600);
            contentStream.close();

            Color headerColor = HEADER_COLOR;

            // Si c'est pas la première page le header est moins foncé
            if(!isFirstPage) {
                headerColor = HEADER_LIGHT_COLOR;
            }

            // On remplie le header
            Row<PDPage> headerRow = baseTable.createRow(20);
            headerRow.setHeaderRow(true);
            for(String headerName: headerList) {
                Cell<PDPage> cell = headerRow.createCell(8, headerName);
                cell.setFillColor(headerColor);
            }

            // utiliser pour fixer le bug de la lib, évite que toute la ligne soit colorié
            headerRow.createCell(0, "");

            baseTable.addHeaderRow(headerRow);

            addTableBody(baseTable, isCoda, language, operationList);

            if(isFinalPage) {
                addTableFooter(baseTable);
            }

            baseTable.draw();
            mainDocument.addPage(myPage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Ajout de la ligne total a la fin du tableau
     * @param table : le tableau sur lequel on doit ajouter la dernière ligne
     */
    private void addTableFooter(BaseTable table) {
        // On ajoute la ligne total a la fin du tableau
        Row<PDPage> row = table.createRow(20);
        for(int i =0; i < 4; i++) {
            row.createCell(8, "").setBorderStyle(new LineStyle(Color.white, 0));
        }
        row.createCell(8, "Total").setBorderStyle(new LineStyle(Color.black, 1));
        row.createCell(8, getFormattedNumber(totalGrossAmount));
        row.createCell(8, getFormattedNumber(totalGrossAmount - totalNetAmount));
        row.createCell(8, getFormattedNumber(totalNetAmount));

    }


    /**
     * Crée chacune des lignes du tableau
     * @param table : Tableau sur lequel on rajoute le body
     * @param isCoda: Permet de savoir le type de tableau
     * @param language : utilisé pour la traduction i18n
     */
    private void addTableBody(BaseTable table, Boolean isCoda, LANGUAGE language, List<Operation> operationList) {
        Boolean shouldColor = false;

        // On crée chacune des lignes
        for(Operation operation: operationList) {
            Row<PDPage> row = table.createRow(20);
            row.createCell(8, operation.getContractNumber());
            row.createCell(8, operation.getNameShop());
            row.createCell(8, String.valueOf(operation.getTradeDate()));

            String rowValue;
            // Si c'est un coda on prend le type de carte sinon le type d'opération
            if(isCoda) {
                String cardTypeValue = CARD_TYPE.getByCode(operation.getCardType()).getValue();
                rowValue = i18nUtils.getMessage(cardTypeValue, null, language);
            } else {
                String operationTypeValue = OPERATION_TYPE.getByCode(operation.getOperationType()).getValue();
                rowValue = i18nUtils.getMessage(operationTypeValue, null, language);
            }

            row.createCell(8, rowValue);

            String sensProperties = OPERATION_SENS.getByCode(operation.getSens()).getValue();
            row.createCell(8, i18nUtils.getMessage(sensProperties, null, language));

            row.createCell(8, getFormattedNumber(operation.getGrossAmount()));
            row.createCell(8, getFormattedNumber((operation.getGrossAmount() - operation.getNetAmount())));
            row.createCell(8, getFormattedNumber(operation.getNetAmount()));

            // Permet de colorier une ligne sur 2
            if( shouldColor ) {
                for(Cell<PDPage> cell: row.getCells()) {
                    cell.setFillColor(Color.lightGray);
                }
                // utiliser pour fixer le bug de la lib, évite que toute la ligne soit colorié
                row.createCell(0, "");
            }

            shouldColor = !shouldColor;
            totalGrossAmount += operation.getGrossAmount();
            totalNetAmount += operation.getNetAmount();
        }
    }


    /**
     * Utiliser pour avoir les chiffres au bon format
     * @param number
     * @return
     */
    private String getFormattedNumber(Long number) {
        // On utilise pas de formatter car la lib ne gère pas le format du string retourné
        DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(Locale.UK);
        otherSymbols.setDecimalSeparator(',');
        otherSymbols.setGroupingSeparator(' ');
        NumberFormat formatter =  new DecimalFormat("#,###,###,##0.00", otherSymbols);
        String numberString = String.valueOf(formatter.format(number/100.0));

        if (number > 0) {
            numberString = "+" + numberString;
        } else if (number < 0) {
            numberString = "-" + numberString;
        }

        return numberString;
    }

}
