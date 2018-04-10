package com.example.aldrac.pictionis;

import android.graphics.Paint;
import android.graphics.Path;

public class DrawingBoards {

    private Path dbPath;
    private Paint dbPaint;
    private long created_at;

    public Path getPath() {
        return dbPath;
    }

    public void setPath(Path path) {
        this.dbPath = path;
    }

    public Paint getPaint() {
        return dbPaint;
    }

    public void setPaint(Paint mPaint) {
        this.dbPaint = mPaint;
    }

    public long getCreated_at() {
        return created_at;
    }

    public void setCreated_at(long created_at) {
        this.created_at = created_at;
    }

    /*private int width;
    private int height;
    private long created_at;

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public long getCreated_at() {
        return created_at;
    }

    public void setCreated_at(long created_at) {
        this.created_at = created_at;
    }*/



}
