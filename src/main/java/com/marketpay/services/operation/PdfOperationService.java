package com.marketpay.services.operation;

import be.quodlibet.boxable.BaseTable;
import be.quodlibet.boxable.Cell;
import be.quodlibet.boxable.Row;
import be.quodlibet.boxable.line.LineStyle;
import com.marketpay.persistence.entity.Operation;
import com.marketpay.references.CARD_TYPE;
import com.marketpay.references.OPERATION_SENS;
import com.marketpay.references.OPERATION_TYPE;
import com.sun.org.apache.xpath.internal.operations.Bool;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDRectangle;

import java.awt.*;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

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

    public PdfOperationService(List<Operation> operationList) {
        this.operationList = operationList;
        this.mainDocument = new PDDocument();
    }


    public PDDocument getPdfDocument() {
        // Si la liste est vide on renvoit directement le document
        if(operationList.isEmpty()) {
            return mainDocument;
        }

        for(int i = 0; i < operationList.size() / MAX_LINE; i++) {
            int startIndex = i * MAX_LINE;
            int endIndex = startIndex + MAX_LINE;
            // Si on arrive a la fin de la liste
            if(endIndex > operationList.size()) {
                endIndex = operationList.size();
            }
            List<Operation> subList = operationList.subList(startIndex, endIndex);
            createTable(subList, i == 0, endIndex == operationList.size());
        }

        return mainDocument;
    }

    /**
     * Fonction qui crée un tableau sur une page
     * @param operationList : 10 éléments max
     * @param isFirstPage : Permet de changer la couleur en fonction de si c'est la première page ou non
     * @param isFinalPage : Permet d'ajouter la ligne final avec le total
     */
    private void createTable(List<Operation> operationList, Boolean isFirstPage, Boolean isFinalPage) {

        // On crée la page
        PDPage myPage = new PDPage(new PDRectangle(PDRectangle.A4.getHeight(), PDRectangle.A4.getWidth()));
        Boolean isCoda = operationList.get(0).getCardType() != null;

        List<String> headerList = Arrays.asList("Contract number", "Store", "Trade date" , isCoda ? "Card type" : "Operation type", "Sens", "Gross amount €", "Commission €", "Net amount €");


        try {
            // On crée le tableau
            BaseTable baseTable  = new BaseTable(yStart , yStartNewPage, bottomMargin, tableWidth, xStart, mainDocument, myPage, true, true);

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

            addTableBody(baseTable, isCoda);

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
     */
    private void addTableBody(BaseTable table, Boolean isCoda) {
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
                rowValue = CARD_TYPE.getByCode(operation.getCardType()).getValue();
            } else {
                rowValue = OPERATION_TYPE.getByCode(operation.getOperationType()).getValue();
            }

            row.createCell(8, rowValue);
            row.createCell(8, OPERATION_SENS.getByCode(operation.getSens()).getValue());

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
        String numberString = String.valueOf(number/100.0);
        numberString = numberString.replaceAll("\\d{3}\\.", " $0");
        return numberString;
    }

}
