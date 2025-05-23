package car.car2024.UI.Transparent;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import car.car2024.ActivityView.R;

class TransparentDialog extends Dialog {

	public TransparentDialog(Context context, int theme) {
		super(context, theme);
	}
	
	public static TransparentDialog createDialog(Context context) {
		TransparentDialog dialog = new TransparentDialog(context, R.style.Transparent);
		dialog.setContentView(R.layout.transparent);
		dialog.getWindow().getAttributes().gravity = Gravity.CENTER;
		return dialog;
	}

	public void setMessage(String message) {
		TextView msgView = (TextView)findViewById(R.id.transparent_message);
		msgView.setText(message);
	}
	
	public void setImage(Context ctx, int resId) {
		ImageView image = (ImageView)findViewById(R.id.transparent_image);
		image.setImageResource(resId);
		
		if(resId==R.drawable.transparent_spinner) {
			Animation anim = AnimationUtils.loadAnimation(ctx,R.anim.progressbar);
			anim.start();
			image.startAnimation(anim);
		}

	}

}
