package com.marsh.android.MB360.insurance.enrollment.repository.responseclass;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class EnrollmentSummary {
    @SerializedName("max_wallet_amount")
    @Expose
    private String maxWalletAmount;
    @SerializedName("sodexo_amount")
    @Expose
    private String sodexoAmount;
    @SerializedName("gmc_base_si")
    @Expose
    private String gmcBaseSi;
    @SerializedName("gmc_topup_si")
    @Expose
    private String gmcTopupSi;
    @SerializedName("gmc_total_si")
    @Expose
    private String gmcTotalSi;
    @SerializedName("gpa_base_si")
    @Expose
    private String gpaBaseSi;
    @SerializedName("gpa_topup_si")
    @Expose
    private String gpaTopupSi;
    @SerializedName("gpa_total_si")
    @Expose
    private String gpaTotalSi;
    @SerializedName("gtl_base_si")
    @Expose
    private String gtlBaseSi;
    @SerializedName("gtl_topup_si")
    @Expose
    private String gtlTopupSi;
    @SerializedName("gtl_total_si")
    @Expose
    private String gtlTotalSi;
    @SerializedName("parent_1")
    @Expose
    private String parent1;
    @SerializedName("parent_1_premium")
    @Expose
    private String parent1Premium;
    @SerializedName("parent_2")
    @Expose
    private String parent2;
    @SerializedName("parent_2_premium")
    @Expose
    private String parent2Premium;
    @SerializedName("gmc_topup_premium")
    @Expose
    private String gmcTopupPremium;
    @SerializedName("gpa_topup_premium")
    @Expose
    private String gpaTopupPremium;
    @SerializedName("gtl_topup_premium")
    @Expose
    private String gtlTopupPremium;
    @SerializedName("wallet_amount_used")
    @Expose
    private String walletAmountUsed;
    @SerializedName("wallet_amount_available")
    @Expose
    private String walletAmountAvailable;
    @SerializedName("payroll_amount_used")
    @Expose
    private String payrollAmountUsed;
    @SerializedName("total_premium")
    @Expose
    private String totalPremium;
    @SerializedName("no_of_installments")
    @Expose
    private String noOfInstallments;
    @SerializedName("confirmation_date")
    @Expose
    private String confirmationDate;
    @SerializedName("gmc_base_si_parent_set1")
    @Expose
    private String gmcBaseSiParentSet1;
    @SerializedName("gmc_base_si_parent_set2")
    @Expose
    private String gmcBaseSiParentSet2;
    @SerializedName("extra_premium")
    @Expose
    private String extraPremium;
    @SerializedName("payroll_amount_used_Int")
    @Expose
    private Integer payrollAmountUsedInt;
    @SerializedName("total_premium_Int")
    @Expose
    private Integer totalPremiumInt;

    public String getMaxWalletAmount() {
        return maxWalletAmount;
    }

    public void setMaxWalletAmount(String maxWalletAmount) {
        this.maxWalletAmount = maxWalletAmount;
    }

    public String getSodexoAmount() {
        return sodexoAmount;
    }

    public void setSodexoAmount(String sodexoAmount) {
        this.sodexoAmount = sodexoAmount;
    }

    public String getGmcBaseSi() {
        return gmcBaseSi;
    }

    public void setGmcBaseSi(String gmcBaseSi) {
        this.gmcBaseSi = gmcBaseSi;
    }

    public String getGmcTopupSi() {
        return gmcTopupSi;
    }

    public void setGmcTopupSi(String gmcTopupSi) {
        this.gmcTopupSi = gmcTopupSi;
    }

    public String getGmcTotalSi() {
        return gmcTotalSi;
    }

    public void setGmcTotalSi(String gmcTotalSi) {
        this.gmcTotalSi = gmcTotalSi;
    }

    public String getGpaBaseSi() {
        return gpaBaseSi;
    }

    public void setGpaBaseSi(String gpaBaseSi) {
        this.gpaBaseSi = gpaBaseSi;
    }

    public String getGpaTopupSi() {
        return gpaTopupSi;
    }

    public void setGpaTopupSi(String gpaTopupSi) {
        this.gpaTopupSi = gpaTopupSi;
    }

    public String getGpaTotalSi() {
        return gpaTotalSi;
    }

    public void setGpaTotalSi(String gpaTotalSi) {
        this.gpaTotalSi = gpaTotalSi;
    }

    public String getGtlBaseSi() {
        return gtlBaseSi;
    }

    public void setGtlBaseSi(String gtlBaseSi) {
        this.gtlBaseSi = gtlBaseSi;
    }

    public String getGtlTopupSi() {
        return gtlTopupSi;
    }

    public void setGtlTopupSi(String gtlTopupSi) {
        this.gtlTopupSi = gtlTopupSi;
    }

    public String getGtlTotalSi() {
        return gtlTotalSi;
    }

    public void setGtlTotalSi(String gtlTotalSi) {
        this.gtlTotalSi = gtlTotalSi;
    }

    public String getParent1() {
        return parent1;
    }

    public void setParent1(String parent1) {
        this.parent1 = parent1;
    }

    public String getParent1Premium() {
        return parent1Premium;
    }

    public void setParent1Premium(String parent1Premium) {
        this.parent1Premium = parent1Premium;
    }

    public String getParent2() {
        return parent2;
    }

    public void setParent2(String parent2) {
        this.parent2 = parent2;
    }

    public String getParent2Premium() {
        return parent2Premium;
    }

    public void setParent2Premium(String parent2Premium) {
        this.parent2Premium = parent2Premium;
    }

    public String getGmcTopupPremium() {
        return gmcTopupPremium;
    }

    public void setGmcTopupPremium(String gmcTopupPremium) {
        this.gmcTopupPremium = gmcTopupPremium;
    }

    public String getGpaTopupPremium() {
        return gpaTopupPremium;
    }

    public void setGpaTopupPremium(String gpaTopupPremium) {
        this.gpaTopupPremium = gpaTopupPremium;
    }

    public String getGtlTopupPremium() {
        return gtlTopupPremium;
    }

    public void setGtlTopupPremium(String gtlTopupPremium) {
        this.gtlTopupPremium = gtlTopupPremium;
    }

    public String getWalletAmountUsed() {
        return walletAmountUsed;
    }

    public void setWalletAmountUsed(String walletAmountUsed) {
        this.walletAmountUsed = walletAmountUsed;
    }

    public String getWalletAmountAvailable() {
        return walletAmountAvailable;
    }

    public void setWalletAmountAvailable(String walletAmountAvailable) {
        this.walletAmountAvailable = walletAmountAvailable;
    }

    public String getPayrollAmountUsed() {
        return payrollAmountUsed;
    }

    public void setPayrollAmountUsed(String payrollAmountUsed) {
        this.payrollAmountUsed = payrollAmountUsed;
    }

    public String getTotalPremium() {
        return totalPremium;
    }

    public void setTotalPremium(String totalPremium) {
        this.totalPremium = totalPremium;
    }

    public String getNoOfInstallments() {
        return noOfInstallments;
    }

    public void setNoOfInstallments(String noOfInstallments) {
        this.noOfInstallments = noOfInstallments;
    }

    public String getConfirmationDate() {
        return confirmationDate;
    }

    public void setConfirmationDate(String confirmationDate) {
        this.confirmationDate = confirmationDate;
    }

    public String getGmcBaseSiParentSet1() {
        return gmcBaseSiParentSet1;
    }

    public void setGmcBaseSiParentSet1(String gmcBaseSiParentSet1) {
        this.gmcBaseSiParentSet1 = gmcBaseSiParentSet1;
    }

    public String getGmcBaseSiParentSet2() {
        return gmcBaseSiParentSet2;
    }

    public void setGmcBaseSiParentSet2(String gmcBaseSiParentSet2) {
        this.gmcBaseSiParentSet2 = gmcBaseSiParentSet2;
    }

    public String getExtraPremium() {
        return extraPremium;
    }

    public void setExtraPremium(String extraPremium) {
        this.extraPremium = extraPremium;
    }

    public Integer getPayrollAmountUsedInt() {
        return payrollAmountUsedInt;
    }

    public void setPayrollAmountUsedInt(Integer payrollAmountUsedInt) {
        this.payrollAmountUsedInt = payrollAmountUsedInt;
    }

    public Integer getTotalPremiumInt() {
        return totalPremiumInt;
    }

    public void setTotalPremiumInt(Integer totalPremiumInt) {
        this.totalPremiumInt = totalPremiumInt;
    }

}

