package jeffery.com.app;

import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.StaticLayout;
import android.util.Log;
import android.view.ViewTreeObserver;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

  private static final String TAG = "yangkai";
  private static final int INTERVAL = 500;
  private static final int MAX_COUNT = 10;

  private TextView textView;
  private TextView textView2;
  private Handler mainHandler = new Handler(Looper.getMainLooper());

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    new Thread(new Runnable() {
      @Override
      public void run() {
        StaticLayoutManager.getInstance().initLayout(MainActivity.this);
      }
    }).start();

    setContentView(R.layout.activity_main);
    textView = (TextView) findViewById(R.id.text);
    textView2 = (TextView) findViewById(R.id.text2);

    getWindow().getDecorView().getViewTreeObserver()
        .addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
          @Override
          public void onGlobalLayout() {
            Log.d(TAG, "onGlobalLayout");
          }
        });

    count = -1;
    runOnUiThread(countRunnable);
  }

  private int count = 0;

  private Runnable countRunnable = new Runnable() {
    @Override
    public void run() {
      if (count >= MAX_COUNT) {
        return;
      }
      Log.d(TAG, "count " + count);

      count++;
      textView.setText(String.valueOf(count));
      if (textView2 != null) {
        textView2.setText(String.valueOf(count));
      }
      mainHandler.postDelayed(this, INTERVAL);
    }
  };
}
