package car.car2024.Utils.Image;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfInt;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
/**
 * 此类不需要进行任何的参数调整
 * 不知道参数.就不要随意调整
 * 这个类的主要功能是扣去视野范围内最大的矩形
 * 不是根据颜色扣的.是根据轮廓.
 * 参数已经调好.除非效果不好,否则不用调整
 * @author hdy
 *
 */
public class RectUtils {
	public static Rect findRectangle(Bitmap image) {
		try {
			Mat tempor = new Mat();
			Mat src = new Mat();
			Utils.bitmapToMat(image, tempor);

			Imgproc.cvtColor(tempor, src, Imgproc.COLOR_BGR2RGB);

			Mat blurred = src.clone();
			Imgproc.medianBlur(src, blurred, 9);

			Mat gray0 = new Mat(blurred.size(), CvType.CV_8U), gray = new Mat();

			List<MatOfPoint> contours = new ArrayList<MatOfPoint>();

			List<Mat> blurredChannel = new ArrayList<Mat>();
			blurredChannel.add(blurred);
			List<Mat> gray0Channel = new ArrayList<Mat>();
			gray0Channel.add(gray0);

			MatOfPoint2f approxCurve;

			double maxArea = 0;
			int maxId = -1;

			for (int c = 0; c < 3; c++) {
				int ch[] = {c, 0};
				Core.mixChannels(blurredChannel, gray0Channel, new MatOfInt(ch));
				Utils.matToBitmap(gray0,image);
//				FileService.savePhoto(image,"gray0.png");
				int thresholdLevel = 1;
				for (int t = 0; t < thresholdLevel; t++) {
					if (t == 0) {
						Imgproc.Canny(gray0, gray, 10, 20, 3, true); // true ?
						Imgproc.dilate(gray, gray, new Mat(), new Point(-1, -1), 1); // 1
						// ?
					} else {
						Imgproc.adaptiveThreshold(gray0, gray, thresholdLevel,
								Imgproc.ADAPTIVE_THRESH_GAUSSIAN_C,
								Imgproc.THRESH_BINARY,
								(src.width() + src.height()) / 200, t);
					}

					Imgproc.findContours(gray, contours, new Mat(),
							Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_SIMPLE);

					for (MatOfPoint contour : contours) {
						MatOfPoint2f temp = new MatOfPoint2f(contour.toArray());

						double area = Imgproc.contourArea(contour);
						approxCurve = new MatOfPoint2f();
						Imgproc.approxPolyDP(temp, approxCurve,
								Imgproc.arcLength(temp, true) * 0.02, true);

						if (approxCurve.total() == 4 && area >= maxArea) {
							double maxCosine = 0;

							List<Point> curves = approxCurve.toList();
							for (int j = 2; j < 5; j++) {

								double cosine = Math.abs(angle(curves.get(j % 4),
										curves.get(j - 2), curves.get(j - 1)));
								maxCosine = Math.max(maxCosine, cosine);
							}

							if (maxCosine < 0.3) {
								maxArea = area;
								maxId = contours.indexOf(contour);
							}
						}
					}
				}
			}

			if (maxId >= 0) {
				Rect rect = Imgproc.boundingRect(contours.get(maxId));

				Imgproc.rectangle(src, rect.tl(), rect.br(), new Scalar(255, 0, 0, .8), 4);

				int mDetectedWidth = rect.width;
				int mDetectedHeight = rect.height;

				Log.d("", "Rectangle width :" + mDetectedWidth + " Rectangle height :" + mDetectedHeight);
				return rect;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private static double angle(Point p1, Point p2, Point p0) {
		double dx1 = p1.x - p0.x;
		double dy1 = p1.y - p0.y;
		double dx2 = p2.x - p0.x;
		double dy2 = p2.y - p0.y;
		return (dx1 * dx2 + dy1 * dy2)
				/ Math.sqrt((dx1 * dx1 + dy1 * dy1) * (dx2 * dx2 + dy2 * dy2)
				+ 1e-10);
	}


	public static Bitmap deepCopyBitmap(Bitmap srcBitmap) {
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		srcBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
		byte[] byteArray = stream.toByteArray();
		return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
	}
}
