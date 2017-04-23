package jeffery.com.app;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Canvas;
import android.text.StaticLayout;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;

/**
 *
 * @author yangkai
 */
public class LazyLayoutTextView extends TextView {

  private List<StaticLayout> layouts;
  private int width;

  public LazyLayoutTextView(Context context) {
    this(context, null);
  }

  public LazyLayoutTextView(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public LazyLayoutTextView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }

  @Override
  public void setText(CharSequence text, BufferType type) {
    if (layouts == null) {
      layouts = new ArrayList<>();
    } else {
      layouts.clear();
    }

    try {
      int value = Integer.valueOf(text.toString());

      while (true) {
        int d = value % 10;
        StaticLayout layout = StaticLayoutManager.getInstance().getLayout(d);
        layouts.add(0, layout);

        if (value < 10) {
          break;
        }
        value /= 10;
      }

      int newWidth = 0;
      if (!layouts.isEmpty()) {
        for (StaticLayout layout : layouts) {
          newWidth += layout.getWidth();
        }
      }
      if (newWidth != width) {
        width = newWidth;
        requestLayout();
      }

      invalidate();
    } catch (Exception e) {
      super.setText(text, type);
    }
  }

  @Override
  protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    if (layouts == null || layouts.isEmpty()) {
      super.onMeasure(widthMeasureSpec, heightMeasureSpec);
      return;
    }

    StaticLayout layout = layouts.get(0);
    setMeasuredDimension(MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY),
        MeasureSpec.makeMeasureSpec(layout.getHeight(), MeasureSpec.EXACTLY));
  }

  @Override
  protected void onDraw(Canvas canvas) {
    if (layouts == null || layouts.isEmpty()) {
      super.onDraw(canvas);
      return;
    }

    long time = System.currentTimeMillis();
    canvas.save();
    canvas.translate(getPaddingLeft(), getPaddingRight());
    if (layouts != null) {
      for (StaticLayout layout : layouts) {
        Log.d("yangkai", "draw layout " + layout.getWidth());
        layout.draw(canvas, null, null, 0);
        canvas.translate(layout.getWidth(), 0);
      }
    }
    canvas.restore();
    Log.d("yangkai", "onDraw coast: " + (System.currentTimeMillis() - time));
  }
}
