package car.car2024.Utils.Image;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.google.zxing.LuminanceSource;

import java.io.FileNotFoundException;

public class RGBLuminanceSource extends LuminanceSource {
	private final byte[] luminances;
	
	public RGBLuminanceSource(Bitmap bitmap) {
		super(bitmap.getWidth(), bitmap.getHeight());
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		int[] pixels = new int[width * height];
		bitmap.getPixels(pixels, 0, width, 0, 0, width, height);
		// In order to measure pure decoding speed, we convert the entire image
		// to a greyscale array
		// up front, which is the same as the Y channel of the
		// YUVLuminanceSource in the real app.
		luminances = new byte[width * height];
		for (int y = 0; y < height; y++) {
			int offset = y * width;
			for (int x = 0; x < width; x++) {
				int pixel = pixels[offset + x];
				int r = (pixel >> 16) & 0xff;
				int g = (pixel >> 8) & 0xff;
				int b = pixel & 0xff;
				if (r == g && g == b) {
					// Image is already greyscale, so pick any channel.
					luminances[offset + x] = (byte) r;
				} else {
					// Calculate luminance cheaply, favoring green.
					luminances[offset + x] = (byte) ((r + g + g + b) >> 2);
				}
			}
		}
	}

	public RGBLuminanceSource(String path) throws FileNotFoundException {
		this(loadBitmap(path));
	}

	@Override
	public byte[] getMatrix() {
		return luminances;
	}

	@Override
	public byte[] getRow(int arg0, byte[] arg1) {
		if (arg0 < 0 || arg0 >= getHeight()) {
			throw new IllegalArgumentException(
					"Requested row is outside the image: " + arg0);
		}
		int width = getWidth();
		if (arg1 == null || arg1.length < width) {
			arg1 = new byte[width];
		}
		System.arraycopy(luminances, arg0 * width, arg1, 0, width);
		return arg1;
	}

	private static Bitmap loadBitmap(String path) throws FileNotFoundException {
		Bitmap bitmap = BitmapFactory.decodeFile(path);
		if (bitmap == null) {
			throw new FileNotFoundException("Couldn't open " + path);
		}
		return bitmap;
	}
}
