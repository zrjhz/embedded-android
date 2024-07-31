package car.car2024.Utils.Command;


/**
 * 用于存放相应的坐标
 * @author hdy
 * 模型类
 * 不用修改.这个类主要用于交通灯的识别
 */
public class Coordinates {
	private int x;
	private int y;

	public Coordinates(int x, int y) {
		super();
		this.x = x;
		this.y = y;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

}
