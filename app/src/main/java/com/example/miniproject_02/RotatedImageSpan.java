package com.example.miniproject_02;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.text.style.ImageSpan;

public class RotatedImageSpan extends ImageSpan {
    private float rotationDegrees;

    public RotatedImageSpan(Context context, int resourceId, int verticalAlignment, float rotationDegrees) {
        super(context, resourceId, verticalAlignment);
        this.rotationDegrees = rotationDegrees;
    }

    @Override
    public void draw(Canvas canvas, CharSequence text, int start, int end,
                     float x, int top, int y, int bottom, Paint paint) {
        Drawable drawable = getDrawable();
        canvas.save();

        // Calculate the center point to rotate the image
        float centerX = x + drawable.getBounds().width() / 2f;
        float centerY = ((float) bottom + top) / 2f;

        // Rotate the canvas around the center point
        canvas.rotate(rotationDegrees, centerX, centerY);

        // Draw the rotated image
        super.draw(canvas, text, start, end, x, top, y, bottom, paint);
        canvas.restore();
    }
}

