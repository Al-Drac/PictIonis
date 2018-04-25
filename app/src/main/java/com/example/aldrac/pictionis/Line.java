package com.example.aldrac.pictionis;

import java.util.ArrayList;
import java.util.List;

public class Line {
        private int color;
        private int heightScreen;
        private int widthScreen;
        private boolean emboss;
        private boolean blur;
        private int strokeWidth;
        private List<PointP> listP = new ArrayList<PointP>();

    public Line() {
    }

    public Line(int color, boolean emboss, boolean blur, int strokeWidth) {
        this.color = color;
        this.emboss = emboss;
        this.blur = blur;
        this.strokeWidth = strokeWidth;
    }

    public Line(int color, boolean emboss, boolean blur, int strokeWidth,int height ,int width,List<PointP> points) {
        this.color = color;
        this.emboss = emboss;
        this.blur = blur;
        this.heightScreen = height;
        this.widthScreen = width;
        this.strokeWidth = strokeWidth;
        this.listP = points;
    }

    public boolean isBlur() {
        return blur;
    }

    public boolean isEmboss() {
        return emboss;
    }

    public int getColor() {
        return color;
    }

    public int getStrokeWidth() {
        return strokeWidth;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public List<PointP> getListP() {
        return listP;
    }

    public void setBlur(boolean blur) {
        this.blur = blur;
    }

    public void setEmboss(boolean emboss) {
        this.emboss = emboss;
    }

    public void setListP(List<PointP> listP) {
        this.listP = listP;
    }

    public void setStrokeWidth(int strokeWidth) {
        this.strokeWidth = strokeWidth;
    }

    public void addPoint(int x, int y){
        PointP p = new PointP(x , y);
        listP.add(p);
    }

    public int getHeightScreen() {
        return heightScreen;
    }

    public void setHeightScreen(int heightScreen) {
        this.heightScreen = heightScreen;
    }

    public int getWidthScreen() {
        return widthScreen;
    }

    public void setWidthScreen(int widthScreen) {
        this.widthScreen = widthScreen;
    }
}
