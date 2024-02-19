package com.tt.core.constant;

public class WorldConstant {

	/**
	 * 灯塔基本信息
	 * 二维数组
	 */
	public static class AOIConstant {
		/**
		 * 世界最大x
		 */
		public static final int WORLD_X = 500000;
		/**
		 * 世界最大y
		 */
		public static final int WORLD_Y = 500000;
		/**
		 * 单个灯塔xSize
		 */
		public static final int TOWER_X_SIZE = 100;
		/**
		 * 单个灯塔ySize
		 */
		public static final int TOWER_Y_SIZE = 100;
		/**
		 * 灯塔x轴数量
		 */
		public static final int MAX_TOWER_X_SIZE = WORLD_X / TOWER_X_SIZE;
		/**
		 * 灯塔y轴数量
		 */
		public static final int MAX_TOWER_Y_SIZE = WORLD_Y / TOWER_Y_SIZE;
	}

	/**
	 * 周围灯塔index offset
	 */
	public static class AoiNearIndexConst {
		/**
		 * 左边
		 */
		public static int LEFT_TOWER_INDEX_OFFSET = -1;
		/**
		 * 右边
		 */
		public static int RIGHT_TOWER_INDEX_OFFSET = 1;
		/**
		 * 上方
		 */
		public static int UP_TOWER_INDEX_OFFSET = AOIConstant.MAX_TOWER_X_SIZE;
		/**
		 * 下方
		 */
		public static int DOWN_TOWER_INDEX_OFFSET = -AOIConstant.MAX_TOWER_X_SIZE;
		/**
		 * 左上
		 */
		public static int LEFT_UP_TOWER_INDEX_OFFSET = AOIConstant.MAX_TOWER_X_SIZE - 1;
		/**
		 * 左下
		 */
		public static int LEFT_DOWN_TOWER_INDEX_OFFSET = -AOIConstant.MAX_TOWER_X_SIZE - 1;
		/**
		 * 右上
		 */
		public static int RIGHT_UP_TOWER_INDEX_OFFSET = AOIConstant.MAX_TOWER_X_SIZE + 1;
		/**
		 * 右下
		 */
		public static int RIGHT_DOWN_TOWER_INDEX_OFFSET = -AOIConstant.MAX_TOWER_X_SIZE + 1;

		public static int[] ALL_NEAR_OFFSET = new int[]{
				0,
				LEFT_TOWER_INDEX_OFFSET,
				RIGHT_TOWER_INDEX_OFFSET,
				UP_TOWER_INDEX_OFFSET,
				DOWN_TOWER_INDEX_OFFSET,
				LEFT_UP_TOWER_INDEX_OFFSET,
				LEFT_DOWN_TOWER_INDEX_OFFSET,
				RIGHT_UP_TOWER_INDEX_OFFSET,
				RIGHT_DOWN_TOWER_INDEX_OFFSET
		};
	}
}
