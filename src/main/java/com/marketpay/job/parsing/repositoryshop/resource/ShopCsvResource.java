package com.marketpay.job.parsing.repositoryshop.resource;

/**
 * Created by etienne on 24/07/17.
 */
public class ShopCsvResource {

    private String Num_Contrat;
    private String Code_AL;
    private String GLN;
    private String ATICA;
    private String Nom_AL;
    private String Code_BU;
    private String CIF;
    private String Nom_BU;

    public ShopCsvResource() {
    }

    public String getNum_Contrat() {
        return Num_Contrat;
    }

    public void setNum_Contrat(String num_Contrat) {
        Num_Contrat = num_Contrat;
    }

    public String getCode_AL() {
        return Code_AL;
    }

    public void setCode_AL(String code_AL) {
        Code_AL = code_AL;
    }

    public String getGLN() {
        return GLN;
    }

    public void setGLN(String GLN) {
        this.GLN = GLN;
    }

    public String getNom_AL() {
        return Nom_AL;
    }

    public void setNom_AL(String nom_AL) {
        Nom_AL = nom_AL;
    }

    public String getCode_BU() {
        return Code_BU;
    }

    public void setCode_BU(String code_BU) {
        Code_BU = code_BU;
    }

    public String getCIF() {
        return CIF;
    }

    public void setCIF(String CIF) {
        this.CIF = CIF;
    }

    public String getNom_BU() {
        return Nom_BU;
    }

    public void setNom_BU(String nom_BU) {
        Nom_BU = nom_BU;
    }

    public String getATICA() {
        return ATICA;
    }

    public void setATICA(String ATICA) {
        this.ATICA = ATICA;
    }

    @Override
    public String toString() {
        return "ShopCsvResource{" +
            "Num_Contrat='" + Num_Contrat + '\'' +
            ", Code_AL='" + Code_AL + '\'' +
            ", GLN='" + GLN + '\'' +
            ", ATICA='" + ATICA + '\'' +
            ", Nom_AL='" + Nom_AL + '\'' +
            ", Code_BU='" + Code_BU + '\'' +
            ", CIF='" + CIF + '\'' +
            ", Nom_BU='" + Nom_BU + '\'' +
            '}';
    }
}
