package com.example.practic;

import android.graphics.Bitmap;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

public class QRGen {

    private int width = 512, heignt = 512;
    private Bitmap qr;
    private String strCode;

    public QRGen(String strCode, int width, int heignt) {
        this.width = width;
        this.heignt = heignt;
        this.strCode = strCode;
    }

    public QRGen(String strCode) {
        this.strCode = strCode;
    }


    public void generate(){
        try{
            MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
            BitMatrix bitMatrix = multiFormatWriter.encode(strCode, BarcodeFormat.QR_CODE, width, heignt);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            qr = barcodeEncoder.createBitmap(bitMatrix);
        }
        catch (WriterException e){
            e.printStackTrace();
        }
    }


    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeignt() {
        return heignt;
    }

    public void setHeignt(int heignt) {
        this.heignt = heignt;
    }

    public Bitmap getQr() {
        generate();
        return qr;
    }

    public void setQr(Bitmap qr) {
        this.qr = qr;
    }

    public String getStrCode() {
        return strCode;
    }

    public void setStrCode(String strCode) {
        this.strCode = strCode;
    }
}