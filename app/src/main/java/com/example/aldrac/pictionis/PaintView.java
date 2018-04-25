package com.example.aldrac.pictionis;

/**
 * Created by AlDrac on 22/03/2018.
 */


        import android.app.AlertDialog;
        import android.content.Context;
        import android.graphics.Bitmap;
        import android.graphics.BlurMaskFilter;
        import android.graphics.Canvas;
        import android.graphics.Color;
        import android.graphics.EmbossMaskFilter;
        import android.graphics.MaskFilter;
        import android.graphics.Paint;
        import android.graphics.Path;
        import android.graphics.PorterDuff;
        import android.util.AttributeSet;
        import android.util.DisplayMetrics;
        import android.util.Log;
        import android.view.MotionEvent;
        import android.view.View;

        import com.google.firebase.auth.FirebaseAuth;
        import com.google.firebase.database.ChildEventListener;
        import com.google.firebase.database.DataSnapshot;
        import com.google.firebase.database.DatabaseError;
        import com.google.firebase.database.DatabaseReference;
        import com.google.firebase.database.FirebaseDatabase;
        import com.google.firebase.database.ValueEventListener;

        import java.util.HashMap;
        import java.util.List;

        import com.google.firebase.database.FirebaseDatabase;
        import java.util.ArrayList;
        import java.util.Arrays;
        import java.util.Date;
        import java.util.Random;


public class PaintView extends View {

    public static int BRUSH_SIZE = 20;
    public static final int DEFAULT_COLOR = Color.BLACK;
    public static final int DEFAULT_BG_COLOR = Color.WHITE;
    private static final float TOUCH_TOLERANCE = 4;
    private float mX, mY;
    private Path mPath;
    private Paint mPaint;
    private ArrayList<FingerPath> paths = new ArrayList<>();
    private ArrayList<Line> lines = new ArrayList<>();
    //private ArrayList<String> words = new ArrayList<>();
    private int currentColor;
    private int backgroundColor = DEFAULT_BG_COLOR;
    private int strokeWidth;
    private boolean emboss;
    private boolean blur;
    private MaskFilter mEmboss;
    private MaskFilter mBlur;
    private Bitmap mBitmap;
    private Canvas mCanvas;
    private Paint mBitmapPaint = new Paint(Paint.DITHER_FLAG);
    private Line myLine;

    //test firebase
    FirebaseDatabase database;
    DatabaseReference ref;
    //fin test firebase



    public PaintView(Context context) {
        this(context, null);
    }

    public PaintView(Context context, AttributeSet attrs) {
        super(context, attrs);
        //test firebase
        database = FirebaseDatabase.getInstance();
        ref = database.getReference("drawing");
        //fin test firebase

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setColor(DEFAULT_COLOR);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setXfermode(null);
        mPaint.setAlpha(0xff);
        mEmboss = new EmbossMaskFilter(new float[]{1, 1, 1}, 0.4f, 6, 3.5f);
        mBlur = new BlurMaskFilter(5, BlurMaskFilter.Blur.NORMAL);

        ref.child("lines").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Line line = dataSnapshot.getValue(Line.class);
                drawLine(line);
                invalidate();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                paths.clear();
                normal();
                invalidate();
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void init(DisplayMetrics metrics) {
        int height = metrics.heightPixels;
        int width = metrics.widthPixels;

        mBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);

        currentColor = DEFAULT_COLOR;
        strokeWidth = BRUSH_SIZE;
    }

    public void normal() {
        emboss = false;
        blur = false;
    }

    public void emboss() {
        emboss = true;
        blur = false;
    }

    public void blur() {
        emboss = false;
        blur = true;
    }


    public void clear() {
        backgroundColor = DEFAULT_BG_COLOR;
        ref.removeValue();
        paths.clear();
        normal();
        invalidate();
    }

    public void setSizeBrush(int size) {
        strokeWidth = size;
    }

    public void setCurrentColor(int color) {
        currentColor = color;
    }

    public void getWord(){
        String[] words = { "Canard", "Vache", "Chat", "Pompiers", "Automobiliste", "Maison", "Immeuble", "Voiture", "Camion", "Ile", "Plage", "Coquillage", "Coquillette", "Spaghetti",
                "Fleur", "Telephone", "Enveloppe", "Nenuphar", "Hexagone", "Pyramide", "Escarpin", "Escalator", "Escargot", "Lierre", "Vase", "Antenne", "Prise de courant", "Surfeur",
                "Bouton", "Fauteuil", "Clavier", "Autoradio", "Tournevis", "Punaise", "Electricité", "Lumiere", "Horloge", "Pedalo" };
        int rnd = new Random().nextInt(words.length);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage(words[rnd]);
        builder.setCancelable(true);
        //builder.setPositiveButton("I agree", new OkOnClickListener());
        //builder.setNegativeButton("No, no", new CancelOnClickListener());
        AlertDialog dialog = builder.create();
        dialog.show();


    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.save();
        mCanvas.drawColor(backgroundColor);

        for (FingerPath fp : paths) {
            mPaint.setColor(fp.color);
            mPaint.setStrokeWidth(fp.strokeWidth);
            mPaint.setMaskFilter(null);

            if (fp.emboss)
                mPaint.setMaskFilter(mEmboss);
            else if (fp.blur)
                mPaint.setMaskFilter(mBlur);

            mCanvas.drawPath(fp.path, mPaint);


        }

        canvas.drawBitmap(mBitmap, 0, 0, mBitmapPaint);
        canvas.restore();
    }

    private void touchStart(float x, float y) {
        myLine = new Line(currentColor,emboss,blur,strokeWidth);
        myLine.addPoint((int)x , (int)y);
        mPath = new Path();
        FingerPath fp = new FingerPath(currentColor, emboss, blur, strokeWidth, mPath);
        paths.add(fp);

        mPath.reset();
        mPath.moveTo(x, y);
        mX = x;
        mY = y;
    }

    private void touchMove(float x, float y) {
        float dx = Math.abs(x - mX);
        float dy = Math.abs(y - mY);

        /*int x1 = (int)x / 8;
        int y1 = (int)y / 8;
*/
        if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
            mPath.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);
            mX = x;
            mY = y;
            myLine.addPoint((int)mX,(int)mY);
        }
    }

    private void touchUp() {
        mPath.lineTo(mX, mY);
        myLine.addPoint((int)mX,(int)mY);

        Log.d("debug height","je suis "+ this.getHeight());
        Log.d("debug width","je suis " + this.getWidth());
        ref.child("lines").push().setValue(new Line(myLine.getColor(), myLine.isEmboss(), myLine.isBlur(), myLine.getStrokeWidth(), this.getHeight(), this.getWidth(),new ArrayList<PointP>(myLine.getListP())));
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                touchStart(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                touchMove(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                touchUp();
                invalidate();
                break;
        }

        return true;
    }

    public void addEventListener(){

    }

    public void drawLine(Line line){
        if(mCanvas != null){
            Log.d("debug drawLine","je suis dans drawline");
            mPaint.setColor(line.getColor());
            mPaint.setStrokeWidth(line.getStrokeWidth());
            int x  = this.getHeight();
            int y  = this.getWidth();
            double scaleX = (double) this.getHeight() / (double) line.getHeightScreen();
            double scaleY = (double) this.getWidth() / (double) line.getWidthScreen();
            Log.d("debug scaleLine","scaleX" + scaleX);
            Log.d("debug scaleLine","scaleY" + scaleY);
            FingerPath fp = new FingerPath(line.getColor(), line.isEmboss(), line.isBlur(), line.getStrokeWidth(), getPathForPoints(line.getListP(),scaleX, scaleY));
            paths.add(fp);
        }
    }

    public static Path getPathForPoints(List<PointP> points, double scaleY, double scaleX) {
        Path path = new Path();
        PointP current = points.get(0);
        path.moveTo(Math.round(scaleX * (double) current.getX()), Math.round(scaleY * (double) current.getY()));
        PointP next = null;
        for (int i = 1; i < points.size(); ++i) {
            Log.d("X", Integer.toString( current.getX()));
            next = points.get(i);
            path.quadTo(
                    Math.round(scaleX * (double) current.getX()), Math.round(scaleY * (double) current.getY()),
                    Math.round(scaleX * ((double) next.getX() + (double) current.getX()) / 2), Math.round(scaleY * (next.getY() + (double) current.getY()) / 2)
            );
            current = next;
        }
        if (next != null) {
            path.lineTo(Math.round(scaleX * (double)next.getX()), Math.round(scaleY * (double) next.getY()));
        }

        return path;
    }
}

// ON TOUCH END GITHUB COURS REGARDER CREATE SCALED VERSION SEGMENT
