package com.codingacademy.testerapp.model;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class Sample {
    @SerializedName("sample_id")
    private   Integer sampleId;

    @SerializedName("sample_name")
    private   String sampleName;

     private   Integer status;

    @SerializedName("sample_date")
    Date sample_date;

    public Integer getSample_id() {
        return sampleId;
    }

    public Sample(Integer sampleId, String sampleName, Integer status, Date sample_date) {
        this.sampleId = sampleId;
        this.sampleName = sampleName;
        this.status = status;
        this.sample_date = sample_date;
    }

    public String getSample_name() {
        return sampleName;
    }


    public Integer getStatus() {
        return status;
    }

    public Date getSample_date() {
        return sample_date;
    }
}
