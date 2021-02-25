import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.SWT;
import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.widgets.Scale;
import org.eclipse.swt.widgets.Slider;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Point;



public class TestGUI {
	
	int obstacleButton = 0;

	protected Shell shell;

	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			TestGUI window = new TestGUI();
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Open the window.
	 */
	public void open() {
		Display display = Display.getDefault();
		createContents();
		shell.open();
		shell.layout();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		
		shell = new Shell();
		shell.setMinimumSize(new Point(250, 60));
		shell.setSize(835, 507);
		shell.setText("SWT Application");
		shell.setLayout(new GridLayout(6, false));
		new Label(shell, SWT.NONE);
		new Label(shell, SWT.NONE);
		new Label(shell, SWT.NONE);
		new Label(shell, SWT.NONE);
		new Label(shell, SWT.NONE);
		new Label(shell, SWT.NONE);
		new Label(shell, SWT.NONE);
		new Label(shell, SWT.NONE);
		new Label(shell, SWT.NONE);
		
		Button btnTruckPlatoon = new Button(shell, SWT.NONE);
		btnTruckPlatoon.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
		btnTruckPlatoon.setFont(SWTResourceManager.getFont("Segoe UI", 9, SWT.BOLD));
		btnTruckPlatoon.setForeground(SWTResourceManager.getColor(SWT.COLOR_RED));
		btnTruckPlatoon.setText("TRUCK PLATOON");
		new Label(shell, SWT.NONE);
		
		DateTime dateTime = new DateTime(shell, SWT.BORDER);
		new Label(shell, SWT.NONE);
		new Label(shell, SWT.NONE);
		new Label(shell, SWT.NONE);
		new Label(shell, SWT.NONE);
		new Label(shell, SWT.NONE);
		new Label(shell, SWT.NONE);
		new Label(shell, SWT.NONE);
		
		Label vehicleSpeed_lbl = new Label(shell, SWT.NONE);
		vehicleSpeed_lbl.setFont(SWTResourceManager.getFont("Segoe UI", 11, SWT.BOLD));
		vehicleSpeed_lbl.setText("Vehicle Speed");
		new Label(shell, SWT.NONE);
		
		Scale scale = new Scale(shell, SWT.NONE);
		scale.setMaximum(80);
		new Label(shell, SWT.NONE);
		new Label(shell, SWT.NONE);
		new Label(shell, SWT.NONE);
		
		Label steeringAngle_lbl_1 = new Label(shell, SWT.NONE);
		steeringAngle_lbl_1.setText("SteeringAngle");
		steeringAngle_lbl_1.setFont(SWTResourceManager.getFont("Segoe UI", 11, SWT.BOLD));
		new Label(shell, SWT.NONE);
		
		Slider slider = new Slider(shell, SWT.NONE);
		new Label(shell, SWT.NONE);
		new Label(shell, SWT.NONE);
		new Label(shell, SWT.NONE);
		new Label(shell, SWT.NONE);
		new Label(shell, SWT.NONE);
		new Label(shell, SWT.NONE);
		new Label(shell, SWT.NONE);
		new Label(shell, SWT.NONE);
		new Label(shell, SWT.NONE);
		
		Button btnLeftObstacle = new Button(shell, SWT.NONE);
		btnLeftObstacle.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				
				obstacleButton = 1;
				
			}
		});
		btnLeftObstacle.setText("Left Obstacle (L)");
		new Label(shell, SWT.NONE);
		
		Button btnFrontObstacle = new Button(shell, SWT.NONE);
		btnFrontObstacle.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
		btnFrontObstacle.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				obstacleButton=2;
			}
		});
		btnFrontObstacle.setText("Front Obstacle  (F)");
		new Label(shell, SWT.NONE);
		
		Button btnRightObstacle = new Button(shell, SWT.NONE);
		btnRightObstacle.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
		btnRightObstacle.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				obstacleButton=3;
			}
		});
		btnRightObstacle.setText("Right Obstacle (R)");
		new Label(shell, SWT.NONE);
		new Label(shell, SWT.NONE);
		new Label(shell, SWT.NONE);
		new Label(shell, SWT.NONE);
		new Label(shell, SWT.NONE);
		new Label(shell, SWT.NONE);
		new Label(shell, SWT.NONE);
		new Label(shell, SWT.NONE);
		new Label(shell, SWT.NONE);
		
		Button btnNewButton = new Button(shell, SWT.NONE);
		btnNewButton.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));

		btnNewButton.setFont(SWTResourceManager.getFont("Segoe UI", 9, SWT.BOLD));
		btnNewButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
			}
		});
		btnNewButton.setText("START");
		new Label(shell, SWT.NONE);
		new Label(shell, SWT.NONE);
		new Label(shell, SWT.NONE);
		
		Label lblSpeed = new Label(shell, SWT.NONE);
		lblSpeed.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
		lblSpeed.setFont(SWTResourceManager.getFont("Segoe UI", 10, SWT.BOLD));
		lblSpeed.setText("SPEED");
		new Label(shell, SWT.NONE);
		
		Label lblObstacle = new Label(shell, SWT.NONE);
		lblObstacle.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, false, 1, 1));
		lblObstacle.setFont(SWTResourceManager.getFont("Segoe UI", 10, SWT.BOLD));
		lblObstacle.setText("SAFE");
		new Label(shell, SWT.NONE);
		
		Label lblSteerAngle = new Label(shell, SWT.NONE);
		GridData gd_lblSteerAngle = new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1);
		gd_lblSteerAngle.widthHint = 235;
		lblSteerAngle.setLayoutData(gd_lblSteerAngle);
		lblSteerAngle.setFont(SWTResourceManager.getFont("Segoe UI", 10, SWT.BOLD));
		lblSteerAngle.setText("STEER ANGLE");
		
		btnNewButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				
				int vSpeed = scale.getSelection();
				int Steer = slider.getSelection();
				
				lblSpeed.setText(Integer.toString(vSpeed));
				lblSteerAngle.setText(Integer.toString(Steer));
				if(obstacleButton==1)
				lblObstacle.setText("L");
				else if(obstacleButton==2)
					lblObstacle.setText("F");
				else if(obstacleButton==3)
					lblObstacle.setText("R");
				
					
				
				
				
			}
		});

	}

}
