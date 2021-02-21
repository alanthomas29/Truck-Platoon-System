import java.util.*;

public class Acc_Decelerate {
	static int distance = 0;
	static int distanceFromMaster = 0;
	static int speed = 60;
	static int count = 0;
	static boolean obstacleDetected = false;

	public static void main(String[] args) {

		
		System.out.println("Enter the length of the platoon");
		Scanner sc = new Scanner(System.in);
		distance = sc.nextInt();
		System.out.println("enter the distance from Master Truck:");
		distanceFromMaster = sc.nextInt();
		setSpeed();

		// System.out.println("Check obstacle is detected:");
		// obstacleDetected = sc.nextBoolean();
		if (distanceFromMaster < 3) {
			Brake();
		}

	}
	
	public static void setServerSpeed() {
		
	}

	public static void setSpeed() {
		if (distance <= 100) { // distance for platoon to exist
			if (distanceFromMaster < 10) {
				// System.out.println("obstacle detected");
				if (distanceFromMaster < 3) {
					System.out.println("Apply emerygency brakes");
				} else {
					// speed= speed-30;
					// System.out.println("obstacle detected, Speed is reduced to " +speed);
					// while(distanceFromMaster<=10) {
					// count=distanceFromMaster+1;

					for (int i = distanceFromMaster; i < 10; i++) {
						count = i + 1;

						if (count == 10) {
							speed = 60;
							System.out
									.println("Distance maintained at 10 m from obstacle, Speed is maintained " + speed);
						} else {
							speed = 30;
							System.out.println("obstacle detected,Speed is reduced by " + speed);
						}
					}
				}

			} else if (distanceFromMaster > 10) {
				speed = speed + 10;
				System.out.println("No obstacle detected maintain 10 m distance, Speed is increaded to " + speed);
				System.out.println("Reducing the distance to safe distance");
				for (int i = distanceFromMaster; i >= 10; i--) {
					distanceFromMaster = i;
					System.out.println(distanceFromMaster);
				}
				System.out.println("safe distance acheived decrease speed to 60");
			} else {
				speed = 60;
				System.out.println("No obstacle, Speed is maintained" + speed);
			}
		} else {

			System.out.println("Max Platoon reached, Platoom possible only in range of 100 m");
		}

	}

	public static boolean Brake() {

		if (distanceFromMaster <= 3) {
			System.out.println("Obstacle detected in front reduce speed to 0 apply urgent brakes");

		} else {
			// Do Nothing
		}
		return obstacleDetected;
	}
	
	

}