package jeffery.com.app;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.text.*;
import android.util.Log;

/**
 * Created by Annie on 2017/4/23.
 */

public class StaticLayoutManager {

  private StaticLayout[] layout = new StaticLayout[10];

  private TextPaint textPaint;
  private TextDirectionHeuristic textDir;
  private Layout.Alignment alignment;

  private Canvas dummyCanvas;

  private int hardCodeWidth;

  @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
  public synchronized void initLayout(Context context) {
    textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
    textPaint.density = context.getResources().getDisplayMetrics().density;
    textPaint.setTextSize(Util.fromDPtoPix(context, Util.TEXT_SIZE_DP));

    textDir = TextDirectionHeuristics.LTR;

    alignment = Layout.Alignment.ALIGN_NORMAL;

    dummyCanvas = new Canvas();

    long time = System.currentTimeMillis();
    for (int i = 0; i < layout.length; i++) {
      final float[] widths = new float[1];
      String s = String.valueOf(i);
      textPaint.getTextWidths(s, widths);

      layout[i] = new StaticLayout(s, textPaint, Math.round(widths[0]), alignment, 1.0f, 0f, true);
      // Android在ICS中引入了TextLayoutCache用于缓存这些中间结果。TextLayoutCache是一个LRU缓存，缓存的key是文本。如果查询缓存时命中，文本的绘制速度会有很大提升。
      layout[i].draw(dummyCanvas);
    }
    Log.d("yangkai", "coast: " + (System.currentTimeMillis() - time));
  }

  public synchronized StaticLayout getLayout(int index) {
    return layout[index];
  }

  private static StaticLayoutManager INSTANCE = null;

  public static synchronized StaticLayoutManager getInstance() {
    if (INSTANCE == null) {
      INSTANCE = new StaticLayoutManager();
    }
    return INSTANCE;
  }

}
