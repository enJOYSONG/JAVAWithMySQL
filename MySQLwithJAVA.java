import javax.imageio.ImageIO;
import javax.swing.JDialog;
import javax.swing.JTable;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.beans.PropertyChangeListener;
import java.beans.PropertyEditor;
import java.io.File;
import java.io.IOException;

import javax.swing.DefaultCellEditor;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.BoxLayout;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import com.mysql.jdbc.ResultSetMetaData;

import java.awt.Canvas;
import java.awt.ComponentOrientation;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JPanel;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.BevelBorder;

import java.awt.Label;
import java.awt.Panel;

public class MySQLwithJAVA extends JDialog{
	private String[] columnNameWeapon = {"WEAPON NAME"};
	private String[] columnNameBullet = {"BULLET NAME"};
	private String[] columnNameBulletSize = {"BULLET SIZE"};
	private String[] columnNameParticle = {"PARTICLE NAME"};
	private String[] columnNameParticleSize = {"PARTICLE PATH"};
	private String[] columnNameSound = {"SOUND NAME"};
	private String[] columnNameSoundPath = {"SOUND PATH"};
	private String[] columnNameModel = {"MODEL NAME"};
	private String[] columnNameModel2DPath = {"MODEL 2D PATH"};
	private String[] columnNameModel3DPath = {"MODEL 3D PATH"};

	private Object[][] rowData = {
			{}
	};


	ArrayList<RBX_Weapon> weapon_list_ = new ArrayList<RBX_Weapon>();
	ArrayList<RBX_Bullet> bullet_list_ = new ArrayList<RBX_Bullet>();
	ArrayList<RBX_Particle> particle_list_ = new ArrayList<RBX_Particle>();
	ArrayList<RBX_Sound> sound_list_ = new ArrayList<RBX_Sound>();
	ArrayList<RBX_Model> model_list_ = new ArrayList<RBX_Model>();
	private JTable table_weapon_;
	private JTable table_bullet_;
	private JTable table_particle_;
	private JTable table_sound_;
	private JTable table_model_;
	private JTable table_bullet_size_;
	private JTable table_particle_path_;
	private JTable table_sound_path_;
	private JTable table_model_2dpath_;
	private JTable table_model_3dpath_;
	
	DefaultTableModel defaultTableModelWeapon;
	DefaultTableModel defaultTableModelBullet;
	DefaultTableModel defaultTableModelBulletSize;
	DefaultTableModel defaultTableModelParticle;
	DefaultTableModel defaultTableModelParticlePath;
	DefaultTableModel defaultTableModelSound;
	DefaultTableModel defaultTableModelSoundPath;
	DefaultTableModel defaultTableModel_Model;
	DefaultTableModel defaultTableModel_Model2Dpath;
	DefaultTableModel defaultTableModel_Model3Dpath;
	
	JLabel picLabel;
	Connection con = null;
	java.sql.Statement st = null;
	
	public static void main(String[] args) {
		try {
			MySQLwithJAVA dialog = new MySQLwithJAVA();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public class RBX_Bullet {
		RBX_Bullet() {}
		RBX_Bullet(String bullet_name, int bullet_size) { 
			bullet_name_ = bullet_name;
			bullet_size_ = bullet_size;
		};

		public String bullet_name_;
		public int bullet_size_;
	};

	class RBX_Particle{
		RBX_Particle() {}
		RBX_Particle(String particle_name, String particle_path) {
			particle_name_ = particle_name;
			particle_path_ = particle_path;
		};

		public String particle_name_;
		public String particle_path_;
	};

	class RBX_Sound {
		RBX_Sound() {}
		RBX_Sound(String sound_name, String sound_path) {
			sound_name_ = sound_name;
			sound_path_ = sound_path;
		};

		public String sound_name_;
		public String sound_path_;
	};

	class RBX_Model {
		RBX_Model() {}
		public RBX_Model(String model_name, String model_path_2d, String model_path_3d) {
			model_name_ = model_name;
			model_path_2d_ = model_path_2d;
			model_path_3d_ = model_path_3d;
		};
		public String model_name_;
		public String model_path_2d_;
		public String model_path_3d_;
	};

	class RBX_Weapon {
		public RBX_Weapon(){
			bullet_ = new RBX_Bullet();
			particle_ = new RBX_Particle();
			sound_ = new RBX_Sound();
			model_ = new RBX_Model();
		};

		RBX_Weapon(String weapon_name, 
				String particle_name,
				String sound_name,
				String bullet_name,
				String model_name) {
			weapon_name_ = weapon_name;
			bullet_name_ = bullet_name;
			particle_name_ = particle_name;
			sound_name_ = sound_name;
			model_name_ = model_name;
		}; 
		RBX_Weapon(String weapon_name, 
				RBX_Bullet bullet,
				RBX_Particle particle,
				RBX_Sound sound,
				RBX_Model model) {
			weapon_name_ = weapon_name;
			bullet_ = bullet;
			particle_ = particle;
			sound_ = sound;
			model_ = model;
		}; 

		//@ fuck you encapsulation 
		public String weapon_name_;
		public String bullet_name_;
		public String particle_name_;
		public String sound_name_;
		public String model_name_;

		RBX_Bullet bullet_;
		RBX_Particle particle_;
		RBX_Sound sound_;
		RBX_Model model_;
	};
	
	public void ResetCombobox() {
		TableColumn weaponColumn = table_weapon_.getColumnModel().getColumn(0);
		JComboBox comboBox = new JComboBox();
		for(int i = 0 ; i < weapon_list_.size() ; i++) {
			comboBox.addItem(weapon_list_.get(i).weapon_name_);

		}
		comboBox.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JComboBox combo = (JComboBox)e.getSource();
				String currentQuantity = (String)combo.getSelectedItem();
				for(int i = 0 ; i < weapon_list_.size() ; i++) {
					if((currentQuantity != null) && (currentQuantity.equals(weapon_list_.get(i).weapon_name_))) {
						defaultTableModelBullet.setRowCount(0);
						defaultTableModelParticle.setRowCount(0);
						defaultTableModelSound.setRowCount(0);
						defaultTableModel_Model.setRowCount(0);
						defaultTableModelBulletSize.setRowCount(0);
						defaultTableModelParticlePath.setRowCount(0);
						defaultTableModelSoundPath.setRowCount(0);
						defaultTableModel_Model2Dpath.setRowCount(0);
						defaultTableModel_Model3Dpath.setRowCount(0);
						Object[] bullet = new Object[]{weapon_list_.get(i).bullet_name_};
						defaultTableModelBullet.addRow(bullet);
						Object[] particle = new Object[]{weapon_list_.get(i).particle_name_};
						defaultTableModelParticle.addRow(particle);
						Object[] sound = new Object[]{weapon_list_.get(i).sound_name_};
						defaultTableModelSound.addRow(sound);
						Object[] model = new Object[]{weapon_list_.get(i).model_name_};
						defaultTableModel_Model.addRow(model);
						Object[] bulletsize = new Object[]{weapon_list_.get(i).bullet_.bullet_size_};
						defaultTableModelBulletSize.addRow(bulletsize);
						Object[] particlepath = new Object[]{weapon_list_.get(i).particle_.particle_path_};
						defaultTableModelParticlePath.addRow(particlepath);
						Object[] soundpath = new Object[]{weapon_list_.get(i).sound_.sound_path_};
						defaultTableModelSoundPath.addRow(soundpath);
						Object[] model2d = new Object[]{weapon_list_.get(i).model_.model_path_2d_};
						defaultTableModel_Model2Dpath.addRow(model2d);
						Object[] model3d = new Object[]{weapon_list_.get(i).model_.model_path_3d_};
						defaultTableModel_Model3Dpath.addRow(model3d);
						
						BufferedImage myPicture;
						try {
							myPicture = ImageIO.read(new File(weapon_list_.get(i).model_.model_path_2d_));
							picLabel.setIcon(new ImageIcon(myPicture));
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}

				}

			}
		});
		weaponColumn.setCellEditor(new DefaultCellEditor(comboBox));

		TableColumn bulletColumn = table_bullet_.getColumnModel().getColumn(0);
		JComboBox comboBox2 = new JComboBox();
		for(int i = 0 ; i < bullet_list_.size() ; i++) {
			comboBox2.addItem(bullet_list_.get(i).bullet_name_);

		}
		bulletColumn.setCellEditor(new DefaultCellEditor(comboBox2));

		TableColumn particleColumn = table_particle_.getColumnModel().getColumn(0);
		JComboBox comboBox3 = new JComboBox();
		for(int i = 0 ; i < particle_list_.size() ; i++) {
			comboBox3.addItem(particle_list_.get(i).particle_name_);

		}
		particleColumn.setCellEditor(new DefaultCellEditor(comboBox3));

		TableColumn soundColumn = table_sound_.getColumnModel().getColumn(0);
		JComboBox comboBox4 = new JComboBox();
		for(int i = 0 ; i < sound_list_.size() ; i++) {
			comboBox4.addItem(sound_list_.get(i).sound_name_);

		}
		soundColumn.setCellEditor(new DefaultCellEditor(comboBox4));

		TableColumn modelColumn = table_model_.getColumnModel().getColumn(0);
		JComboBox comboBox5 = new JComboBox();
		for(int i = 0 ; i < model_list_.size() ; i++) {
			comboBox5.addItem(model_list_.get(i).model_name_);

		}
		modelColumn.setCellEditor(new DefaultCellEditor(comboBox5));
	}
	
	public void ResetData() {
		weapon_list_.clear();
		bullet_list_.clear();
		particle_list_.clear();
		sound_list_.clear();
		model_list_.clear();
		try {

			con = DriverManager.getConnection("jdbc:mysql://localhost",
					"root", "");

			ResultSet rs = null;
			st = con.createStatement();

			st.execute("use test");

			Object [] tempObject = new Object[10];

			//bullet
			int count = 0;
			ResultSet res = st.executeQuery("SELECT COUNT(*) FROM bullet");
			while (res.next()){
				count = res.getInt(1);
			}

			if (st.execute("select * from bullet")) {
				rs = st.getResultSet();
			}
			while (rs.next()){
				RBX_Bullet bullet = new RBX_Bullet(
						rs.getString("bulletname"), 
						rs.getInt("size"));

				bullet_list_.add(bullet);

			}

			//particle
			count = 0;
			res = st.executeQuery("SELECT COUNT(*) FROM particle");
			while (res.next()){
				count = res.getInt(1);
			}

			if (st.execute("select * from particle")) {
				rs = st.getResultSet();
			}
			while (rs.next()){
				RBX_Particle particle = new RBX_Particle(
						rs.getString("particlename"), 
						rs.getString("particlepath"));

				particle_list_.add(particle);

			}

			//sound
			count = 0;
			res = st.executeQuery("SELECT COUNT(*) FROM sound");
			while (res.next()){
				count = res.getInt(1);
			}

			if (st.execute("select * from sound")) {
				rs = st.getResultSet();
			}
			while (rs.next()){
				RBX_Sound sound = new RBX_Sound(
						rs.getString("soundname"), 
						rs.getString("soundpath"));

				sound_list_.add(sound);

			}

			//model
			count = 0;
			res = st.executeQuery("SELECT COUNT(*) FROM model");
			while (res.next()){
				count = res.getInt(1);
			}

			if (st.execute("select * from model")) {
				rs = st.getResultSet();
			}
			while (rs.next()){
				RBX_Model model = new RBX_Model(
						rs.getString("modelname"), 
						rs.getString("2dimagepath"),
						rs.getString("3dmodelpath"));

				model_list_.add(model);

			}


			//weapon
			count = 0;
			res = st.executeQuery("SELECT COUNT(*) FROM weapon");
			while (res.next()){
				count = res.getInt(1);
			}

			if (st.execute("select * from weapon")) {
				rs = st.getResultSet();
			}

			while (rs.next()){
				RBX_Weapon weapon = new RBX_Weapon(
						rs.getString("weaponname"), 
						rs.getString("particlename"),
						rs.getString("soundname"),
						rs.getString("bulletname"),
						rs.getString("modelname"));

				//@ search bullet by bullet_name_ and insert weapon object
				for(int i = 0 ; i < bullet_list_.size() ; i++) {
					RBX_Bullet bullet = new RBX_Bullet();
					bullet = bullet_list_.get(i);
					if(weapon.bullet_name_.equals(bullet.bullet_name_)) {
						weapon.bullet_ = bullet;
					}
				}

				//@ search particle by particle_name_ and insert weapon object
				for(int i = 0 ; i < particle_list_.size() ; i++) {
					RBX_Particle particle = new RBX_Particle();
					particle = particle_list_.get(i);
					if(weapon.particle_name_.equals(particle.particle_name_)) {
						weapon.particle_ = particle;
					}
				}

				//@ search sound by sound_name_ and insert weapon object
				for(int i = 0 ; i < sound_list_.size() ; i++) {
					RBX_Sound sound = new RBX_Sound();
					sound = sound_list_.get(i);
					if(weapon.sound_name_.equals(sound.sound_name_)) {
						weapon.sound_ = sound;
					}
				}

				//@ search model by model_name_ and insert weapon object
				for(int i = 0 ; i < model_list_.size() ; i++) {
					RBX_Model model = new RBX_Model();
					model = model_list_.get(i);
					if(weapon.model_name_.equals(model.model_name_)) {
						weapon.model_ = model;
					}
				}

				weapon_list_.add(weapon);

			}

			for(int i = 0 ; i < weapon_list_.size() ; i ++ ) {
				tempObject[0] = weapon_list_.get(i).weapon_name_;
				tempObject[1] = weapon_list_.get(i).bullet_name_;
				tempObject[2] = weapon_list_.get(i).bullet_.bullet_size_;
				tempObject[3] = weapon_list_.get(i).particle_name_;
				tempObject[4] = weapon_list_.get(i).particle_.particle_path_;
				tempObject[5] = weapon_list_.get(i).sound_name_;
				tempObject[6] = weapon_list_.get(i).sound_.sound_path_;
				tempObject[7] = weapon_list_.get(i).model_name_;
				tempObject[8] = weapon_list_.get(i).model_.model_path_2d_;
				tempObject[9] = weapon_list_.get(i).model_.model_path_3d_;

				//defaultTableModel.addRow(tempObject);
			}
		} catch (SQLException sqex) {
			System.out.println("SQLException: " + sqex.getMessage());
			System.out.println("SQLState: " + sqex.getSQLState());
		}

	}

	public MySQLwithJAVA() {

		setBounds(100, 100, 800, 700);
		getContentPane().setLayout(new GridLayout(1, 1, 0, 0));

		/*DefaultTableModel defaultTableModel = new DefaultTableModel(rowData, columnNames);



		table = new JTable(defaultTableModel);
		table.setFillsViewportHeight(true);
		JScrollPane pane = new JScrollPane(table);

		table.setRowSelectionAllowed(true);
		table.setColumnSelectionAllowed(false);


		getContentPane().add(pane);

		JPanel panel = new JPanel();
		getContentPane().add(panel);

		JButton btnNewButton = new JButton("New button");
		panel.add(btnNewButton);

		JButton btnNewButton_1 = new JButton("New button");
		panel.add(btnNewButton_1);

		JButton btnNewButton_2 = new JButton("New button");
		panel.add(btnNewButton_2);*/

		JPanel panel = new JPanel();
		panel.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		getContentPane().add(panel);
		panel.setLayout(new GridLayout(0, 1, 0, 0));

		defaultTableModelWeapon = new DefaultTableModel(rowData, columnNameWeapon);
		table_weapon_ = new JTable(defaultTableModelWeapon);
		JScrollPane pane = new JScrollPane(table_weapon_);
		table_weapon_.setRowSelectionAllowed(true);
		table_weapon_.setColumnSelectionAllowed(false);
		panel.add(pane);

		defaultTableModelBullet = new DefaultTableModel(rowData, columnNameBullet);
		table_bullet_ = new JTable(defaultTableModelBullet);
		JScrollPane pane2 = new JScrollPane(table_bullet_);
		table_bullet_.setRowSelectionAllowed(true);
		table_bullet_.setColumnSelectionAllowed(false);
		panel.add(pane2);

		defaultTableModelBulletSize = new DefaultTableModel(rowData, columnNameBulletSize);
		table_bullet_size_ = new JTable(defaultTableModelBulletSize);
		JScrollPane pane2_2 = new JScrollPane(table_bullet_size_);
		table_bullet_size_.setRowSelectionAllowed(true);
		table_bullet_size_.setColumnSelectionAllowed(false);
		panel.add(pane2_2);

		defaultTableModelParticle = new DefaultTableModel(rowData, columnNameParticle);
		table_particle_ = new JTable(defaultTableModelParticle);
		JScrollPane pane3 = new JScrollPane(table_particle_);
		table_particle_.setRowSelectionAllowed(true);
		table_particle_.setColumnSelectionAllowed(false);
		panel.add(pane3);

		defaultTableModelParticlePath = new DefaultTableModel(rowData, columnNameParticleSize);
		table_particle_path_ = new JTable(defaultTableModelParticlePath);
		JScrollPane pane3_2 = new JScrollPane(table_particle_path_);
		table_particle_path_.setRowSelectionAllowed(true);
		table_particle_path_.setColumnSelectionAllowed(false);
		panel.add(pane3_2);

		defaultTableModelSound = new DefaultTableModel(rowData, columnNameSound);
		table_sound_ = new JTable(defaultTableModelSound);
		JScrollPane pane4 = new JScrollPane(table_sound_);
		table_sound_.setRowSelectionAllowed(true);
		table_sound_.setColumnSelectionAllowed(false);
		panel.add(pane4);

		defaultTableModelSoundPath = new DefaultTableModel(rowData, columnNameSoundPath);
		table_sound_path_ = new JTable(defaultTableModelSoundPath);
		JScrollPane pane4_2 = new JScrollPane(table_sound_path_);
		table_sound_path_.setRowSelectionAllowed(true);
		table_sound_path_.setColumnSelectionAllowed(false);
		panel.add(pane4_2);

		defaultTableModel_Model = new DefaultTableModel(rowData, columnNameModel);
		table_model_ = new JTable(defaultTableModel_Model);
		JScrollPane pane5 = new JScrollPane(table_model_);
		table_model_.setRowSelectionAllowed(true);
		table_model_.setColumnSelectionAllowed(false);
		panel.add(pane5);

		defaultTableModel_Model2Dpath = new DefaultTableModel(rowData, columnNameModel2DPath);
		table_model_2dpath_ = new JTable(defaultTableModel_Model2Dpath);
		JScrollPane pane5_2 = new JScrollPane(table_model_2dpath_);
		table_model_2dpath_.setRowSelectionAllowed(true);
		table_model_2dpath_.setColumnSelectionAllowed(false);
		panel.add(pane5_2);

		defaultTableModel_Model3Dpath = new DefaultTableModel(rowData, columnNameModel3DPath);
		table_model_3dpath_ = new JTable(defaultTableModel_Model3Dpath);
		JScrollPane pane5_3 = new JScrollPane(table_model_3dpath_);
		table_model_3dpath_.setRowSelectionAllowed(true);
		table_model_3dpath_.setColumnSelectionAllowed(false);
		panel.add(pane5_3);

		JButton btnNewButton = new JButton("\uC800\uC7A5");
		JButton btnNewButton_1 = new JButton("\uC0AD\uC81C");
		JPanel pane6 = new JPanel();
		pane6.add(btnNewButton);
		pane6.add(btnNewButton_1);
		panel.add(pane6);
		
		Panel panel_1 = new Panel();

		picLabel = new JLabel();
		panel_1.add(picLabel);
		getContentPane().add(panel_1);
		
		ResetData();
		
		TableColumn weaponColumn = table_weapon_.getColumnModel().getColumn(0);
		JComboBox comboBox = new JComboBox();
		for(int i = 0 ; i < weapon_list_.size() ; i++) {
			comboBox.addItem(weapon_list_.get(i).weapon_name_);

		}
		comboBox.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JComboBox combo = (JComboBox)e.getSource();
				String currentQuantity = (String)combo.getSelectedItem();
				for(int i = 0 ; i < weapon_list_.size() ; i++) {
					if((currentQuantity != null) && (currentQuantity.equals(weapon_list_.get(i).weapon_name_))) {
						defaultTableModelBullet.setRowCount(0);
						defaultTableModelParticle.setRowCount(0);
						defaultTableModelSound.setRowCount(0);
						defaultTableModel_Model.setRowCount(0);
						defaultTableModelBulletSize.setRowCount(0);
						defaultTableModelParticlePath.setRowCount(0);
						defaultTableModelSoundPath.setRowCount(0);
						defaultTableModel_Model2Dpath.setRowCount(0);
						defaultTableModel_Model3Dpath.setRowCount(0);
						Object[] bullet = new Object[]{weapon_list_.get(i).bullet_name_};
						defaultTableModelBullet.addRow(bullet);
						Object[] particle = new Object[]{weapon_list_.get(i).particle_name_};
						defaultTableModelParticle.addRow(particle);
						Object[] sound = new Object[]{weapon_list_.get(i).sound_name_};
						defaultTableModelSound.addRow(sound);
						Object[] model = new Object[]{weapon_list_.get(i).model_name_};
						defaultTableModel_Model.addRow(model);
						Object[] bulletsize = new Object[]{weapon_list_.get(i).bullet_.bullet_size_};
						defaultTableModelBulletSize.addRow(bulletsize);
						Object[] particlepath = new Object[]{weapon_list_.get(i).particle_.particle_path_};
						defaultTableModelParticlePath.addRow(particlepath);
						Object[] soundpath = new Object[]{weapon_list_.get(i).sound_.sound_path_};
						defaultTableModelSoundPath.addRow(soundpath);
						Object[] model2d = new Object[]{weapon_list_.get(i).model_.model_path_2d_};
						defaultTableModel_Model2Dpath.addRow(model2d);
						Object[] model3d = new Object[]{weapon_list_.get(i).model_.model_path_3d_};
						defaultTableModel_Model3Dpath.addRow(model3d);
						
						BufferedImage myPicture;
						try {
							myPicture = ImageIO.read(new File(weapon_list_.get(i).model_.model_path_2d_));
							picLabel.setIcon(new ImageIcon(myPicture));
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}

				}

			}
		});
		weaponColumn.setCellEditor(new DefaultCellEditor(comboBox));

		TableColumn bulletColumn = table_bullet_.getColumnModel().getColumn(0);
		JComboBox comboBox2 = new JComboBox();
		for(int i = 0 ; i < bullet_list_.size() ; i++) {
			comboBox2.addItem(bullet_list_.get(i).bullet_name_);

		}
		bulletColumn.setCellEditor(new DefaultCellEditor(comboBox2));

		TableColumn particleColumn = table_particle_.getColumnModel().getColumn(0);
		JComboBox comboBox3 = new JComboBox();
		for(int i = 0 ; i < particle_list_.size() ; i++) {
			comboBox3.addItem(particle_list_.get(i).particle_name_);

		}
		particleColumn.setCellEditor(new DefaultCellEditor(comboBox3));

		TableColumn soundColumn = table_sound_.getColumnModel().getColumn(0);
		JComboBox comboBox4 = new JComboBox();
		for(int i = 0 ; i < sound_list_.size() ; i++) {
			comboBox4.addItem(sound_list_.get(i).sound_name_);

		}
		soundColumn.setCellEditor(new DefaultCellEditor(comboBox4));

		TableColumn modelColumn = table_model_.getColumnModel().getColumn(0);
		JComboBox comboBox5 = new JComboBox();
		for(int i = 0 ; i < model_list_.size() ; i++) {
			comboBox5.addItem(model_list_.get(i).model_name_);

		}
		modelColumn.setCellEditor(new DefaultCellEditor(comboBox5));

		btnNewButton_1.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				String weapon = (String)comboBox.getSelectedItem();
				try {
					st.executeUpdate("delete from weapon where weaponname = '" + weapon + "';");
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				ResetData();
				ResetCombobox();
			}
		});
		
		btnNewButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				
				String weapon = (String)comboBox.getSelectedItem();
				for(int i = 0 ; i < weapon_list_.size() ; i++) {
					if((weapon != null) && (weapon.equals(weapon_list_.get(i).weapon_name_))) {
						//table_bullet_size_.setValueAt(table_bullet_size_.editCellAt(table_bullet_size_.getSelectedRow(), table_bullet_size_.getSelectedColumn()), table_bullet_size_.getSelectedRow(), table_bullet_size_.getSelectedColumn());
						//table_bullet_size_.editingStopped(null);
						weapon_list_.get(i).bullet_.bullet_size_ = Integer.parseInt(String.valueOf(table_bullet_size_.getValueAt(0, 0)));
						weapon_list_.get(i).particle_.particle_path_ = (String)table_particle_path_.getValueAt(0, 0);
						weapon_list_.get(i).sound_.sound_path_ = (String)table_sound_path_.getValueAt(0, 0);
						weapon_list_.get(i).model_.model_path_2d_ = (String)table_model_2dpath_.getValueAt(0, 0);
						weapon_list_.get(i).model_.model_path_3d_ = (String)table_model_3dpath_.getValueAt(0, 0);

						try {
							String a = "update bullet set size = " + String.valueOf(weapon_list_.get(i).bullet_.bullet_size_)
									+ " where bulletname = '" + weapon_list_.get(i).bullet_.bullet_name_ +"'";
							int res = st.executeUpdate("update bullet set size = " + String.valueOf(weapon_list_.get(i).bullet_.bullet_size_)
									+ " where bulletname = '" + weapon_list_.get(i).bullet_.bullet_name_ +"'");
							res = st.executeUpdate("update particle set particlepath = '" + weapon_list_.get(i).particle_.particle_path_ + "' "
									+ "where particlename = '" + weapon_list_.get(i).particle_.particle_name_ +"'");
							res = st.executeUpdate("update sound set soundpath = '" + weapon_list_.get(i).sound_.sound_path_ + "' "
									+ "where soundname = '" + weapon_list_.get(i).sound_.sound_name_ +"'");
							res = st.executeUpdate("update sound set soundpath = '" + weapon_list_.get(i).sound_.sound_path_ + "' "
									+ "where soundname = '" + weapon_list_.get(i).sound_.sound_name_ +"'");
							res = st.executeUpdate("update model set 2dimagepath = '" + weapon_list_.get(i).model_.model_path_2d_ + "' "
									+ ", 3Dmodelpath = '" + weapon_list_.get(i).model_.model_path_3d_ + "' where modelname = '" + weapon_list_.get(i).model_.model_name_ +"'");
						
							defaultTableModelBullet.setRowCount(0);
							defaultTableModelParticle.setRowCount(0);
							defaultTableModelSound.setRowCount(0);
							defaultTableModel_Model.setRowCount(0);
							defaultTableModelBulletSize.setRowCount(0);
							defaultTableModelParticlePath.setRowCount(0);
							defaultTableModelSoundPath.setRowCount(0);
							defaultTableModel_Model2Dpath.setRowCount(0);
							defaultTableModel_Model3Dpath.setRowCount(0);
						} catch (SQLException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
				}
			}
		});




	}

	public class MyTableModel extends AbstractTableModel {
		private String[] columns = {"WEAPON NAME", "BULLET NAME", "BULLET SIZE", "PARTICLE NAME", "PARTICLE SIZE", 
				"SOUND NAME", "SOUND PATH", "MODEL NAME", "MODEL 2D PATH", "MODEL 3D PATH"};
		private Object[][] data = {
				{1, "a", 20, "aaa", 30, "aaaa", "D:", "aaaa", "D:/hi", "D:/hi2"}
		};

		public int getRowCount() {
			return data.length;
		}

		public int getColumnCount() {
			return columns.length;
		}

		public Object getValueAt(int rowIndex, int columnIndex) {
			return data[rowIndex][columnIndex];
		}

		@Override
		public String getColumnName(int column) {
			return columns[column];
		}

		//
		// This method is used by the JTable to define the default
		// renderer or editor for each cell. For example if you have
		// a boolean data it will be rendered as a check box. A
		// number value is right aligned.
		//
		@Override
		public Class getColumnClass(int columnIndex) {
			return data[0][columnIndex].getClass();
		}
	}

}
