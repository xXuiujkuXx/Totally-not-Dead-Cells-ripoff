package utilz;

public class Constans {
	
	public static class Directions{
		public static final int left = 0;
		public static final int right = 1;
		public static final int up = 2;
		public static final int down = 3;
	}
	public static class PlayerConstans{
		public static final int Idle = 0;
		public static final int Walk = 1;
		public static final int Attack01 = 2;
		public static final int Attack02 = 3;
		public static final int Attack03_Range = 4;
		public static final int Hurt = 5;
		public static final int Death = 6;
		
		public static int GetSpriteAmount(int player_Acotion) {
			switch(player_Acotion) {
			
				case Idle:
				case Attack01:
				case Attack02:
					return 6;
				case Walk:
					return 8;
				case Hurt:
				case Death:
					return 4;
				case Attack03_Range:
					return 9;
				default:
					return 1;
			}
		}
	}
}
 