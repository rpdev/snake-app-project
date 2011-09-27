package se.chalmers.snake.gameguiold;

import java.util.LinkedList;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Join;
import android.view.MotionEvent;
import android.view.View;

public class DrawTest extends View {
	private LinkedList<PointF> l = new LinkedList<PointF>();
	private Paint paint = new Paint();
	
	
	public DrawTest(Context context) {
		super(context);
		this.setBackgroundColor(Color.BLACK);
		paint.setStyle(Paint.Style.FILL);
	}

	@Override
	public void onDraw(Canvas canvas) {
		
		if(!l.isEmpty()){
			PointF[] pts = l.toArray(new PointF[l.size()]);
			/*
			Snake is a shape
			Path path = new Path();
			path.moveTo(pts[0].x, pts[0].y);
			float w = 20;
			for(int i=0;i<pts.length;i++)
				path.lineTo(pts[i].x+w, pts[i].y+w);
			for(int i=pts.length-1;i>0;i--)
				path.lineTo(pts[i].x-w, pts[i].y-w);
			path.setLastPoint(pts[0].x, pts[0].y);
			path.close();
			canvas.drawPath(path, paint);
			*/
			// In theory http://download.oracle.com/javase/tutorial/2d/geometry/strokeandfill.html
			int w = 15, e = pts.length -1;
			paint.setStrokeMiter(10); //TODO Unsure what this do or how it work
			paint.setStrokeCap(Cap.ROUND);
			paint.setStrokeJoin(Join.ROUND);
			paint.setStrokeWidth(w);
			paint.setColor(Color.YELLOW);
			
			Rect r1 = new Rect((int)pts[0].x-w, (int)pts[0].y-w, (int)pts[0].x+w, (int)pts[0].y+w);
			Rect r2 = new Rect((int)pts[e].x-w, (int)pts[e].y-w, (int)pts[e].x+w, (int)pts[e].y+w);
			boolean inter = !Rect.intersects(r1,r2);
			if(!inter)
				paint.setColor(Color.MAGENTA);
			for(int i=0;i<pts.length;i++) {
				//canvas.drawLine(pts[i].x, pts[i].y, pts[i+1].x, pts[i+1].y, paint);
				canvas.drawCircle(pts[i].x, pts[i].y, w, paint);
			}
			if(inter)
				paint.setColor(Color.RED);
			canvas.drawRect(r1, paint);//canvas.drawCircle(pts[0].x, pts[0].y, 20, paint);
			if(inter)
				paint.setColor(Color.GREEN);
			canvas.drawRect(r2, paint);//canvas.drawCircle(pts[pts.length-1].x, pts[pts.length-1].y, 20, paint);
		}
	}


	@Override
	public boolean onTouchEvent(MotionEvent me) {
		if (l.size() > 50 && l.size() != 0)
			l.removeFirst();
		l.add(new PointF(me.getX(), me.getY()));
		this.invalidate();
		return true;
	}
}
