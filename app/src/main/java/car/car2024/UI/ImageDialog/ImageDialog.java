package car.car2024.UI.ImageDialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Window;
import android.widget.ImageView;

import car.car2024.ActivityView.R;

public class ImageDialog extends Dialog {
    private final Bitmap bitmap;

    public ImageDialog(Context context, Bitmap bitmap) {
        super(context);
        this.bitmap = bitmap;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_image);

        ImageView imageView = findViewById(R.id.image_view);
        imageView.setImageBitmap(bitmap);
    }
}