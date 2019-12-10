package voca;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.JFileChooser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;       

public class VocaMain extends JFrame {

	private TableModel tableModel;
	private JTable table;
	private int selectIndex = -1;
	private JButton addButton, deleteButton , openDoc, openVoca, saveVoca;
	private JTextField cntTxt, korTxt, engTxt;
	private VocaMain vm = this;
	JLabel jl2;
	static Hashtable<String, String> ht;

	public VocaMain() {
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setPreferredSize(new Dimension(650, 1000));
		this.setTitle("Voca");
		componetAdd();

	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ht = new Hashtable<String, String>();
		try{
     
            File file = new File("resource\\vocadic.txt");
            FileReader filereader = new FileReader(file);
            BufferedReader bufReader = new BufferedReader(filereader);
            String line = "";
            while((line = bufReader.readLine()) != null){
            	
            	String[] strarr = line.split(" /// ");
            	if (strarr.length == 1 )
            	{
            		System.out.println(strarr[0]);
            	}
            	else ht.put(strarr[0],strarr[1]);
                //System.out.println(strarr[0]);
                //System.out.println(strarr[1]);
                
            
            }      
            bufReader.close();
        }catch (FileNotFoundException e) {
            // TODO: handle exception
        }catch(Exception e){
            System.out.println(e);
        }


		
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				VocaMain tableTest = new VocaMain();
				tableTest.showUI();
			}
		});

	}
	
	private void componetAdd() {
		tableModel = new TableModel();
		table = new JTable(tableModel);
		table.getSelectionModel().addListSelectionListener(new RowListener());
		table.setRowSelectionAllowed(true);
		table.setBorder(BorderFactory.createLineBorder(java.awt.Color.BLACK));
		
		


		final TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(tableModel);
		table.setRowSorter(sorter);
	
		
		JPanel tablePane = new JPanel();
		JPanel topPane = new JPanel();

		ActionListener fileAction = new FileAction();
		openDoc = new JButton("OpenDoc");
		openDoc.addActionListener(new FileAction());
		/*openVoca = new JButton("OpenVoca");
		openVoca.addActionListener(fileAction);
		saveVoca = new JButton("SaveVoca");
		saveVoca.addActionListener(fileAction);*/
		
		topPane.add(openDoc);
		//topPane.add(openVoca);
		//topPane.add(saveVoca);

		JPanel bttomPane = new JPanel();
		addButton = new JButton("추가");
		addButton.addActionListener(new AddAction());
		deleteButton = new JButton("삭제");
		deleteButton.addActionListener(new AddAction());

		
		JLabel jl1 = new JLabel("num voca :");
		jl2 = new JLabel("0");


		bttomPane.add(jl1);
		bttomPane.add(jl2);
		bttomPane.add(deleteButton);



		JScrollPane scrollPane = new JScrollPane(tablePane);

		//contentPane.add(scrollPane);


		tablePane.setLayout(new BorderLayout());
		tablePane.add(table.getTableHeader(), BorderLayout.PAGE_START);
		tablePane.add(table, BorderLayout.CENTER);

		this.getContentPane().setLayout(new BorderLayout());
		this.getContentPane().add(topPane, BorderLayout.PAGE_START);
		this.getContentPane().add(scrollPane, BorderLayout.CENTER);
		this.getContentPane().add(bttomPane, BorderLayout.PAGE_END);

	}

	public void showUI() {
		Toolkit tk = Toolkit.getDefaultToolkit();
		Dimension d = tk.getScreenSize();
		this.pack();
		this.setLocation(d.width / 2 - this.getWidth() / 2, d.height / 2 - this.getHeight() / 2);
		this.setVisible(true);
	}



	private class TableModel extends AbstractTableModel {
		
		final String[] columnNames = { "eng", "kor", "cnt" };
		final Class[] columnClasses = { Integer.class, String.class, Integer.class, String.class };
		final Vector<VocData> data = new Vector<VocData>();

		  
          public Class getColumnClass(int column) {
              switch (column) {
                  case 0:
                      return String.class;
                  case 1:
                      return String.class;
                  case 2:
                      return Integer.class;
                  default:
                      return String.class;
              }
          }
		
		public Object getValueAt(int row, int col) {
			VocData voc = data.elementAt(row);
			if (col == 2)
				return voc.getCnt();
			else if (col == 0)
				return voc.getEng();
			else if (col == 1)
				return voc.getKor();
			else
				return null;
		}

		public int getColumnCount() {
			return columnNames.length;
		}

		public int getRowCount() {

			return data.size();
		}

		@Override
		public String getColumnName(int i) {
			// TODO Auto-generated method stub
			return columnNames[i];
		}


		public void addVoc(VocData voc) {
			data.addElement(voc);
			fireTableRowsInserted(data.size() - 1, data.size() - 1);
			jl2.setText(String.valueOf(data.size()));
		}

		public void addVoc(String str) {
			str = str.toLowerCase();
			Iterator<VocData> i = data.iterator();
			boolean isExist = false;
			while (i.hasNext()) {
				VocData v = i.next();
				if (v.getEng().equals(str))
				{
					v.cnt++;
					isExist = true;
				}
					
			}
			if(isExist == false)
			{
				if(ht.containsKey(str))
				{
					data.addElement(new VocData(str,ht.get(str),1));
				}
				else data.addElement(new VocData(str,"x",1));
				fireTableRowsInserted(data.size() - 1, data.size() - 1);
			}
			jl2.setText(String.valueOf(data.size()));

		}

		
		public void delVoc(int row) {
			data.remove(row);
			fireTableRowsDeleted(row, row);
			jl2.setText(String.valueOf(data.size()));
		}

	}

	private class AddAction implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			if (e.getSource() == addButton) {
				VocData voc = new VocData();
				voc.setKor(korTxt.getText());
				voc.seteng(engTxt.getText());
				voc.setCnt(Integer.parseInt(cntTxt.getText()));

				tableModel.addVoc(voc);
			} else if (e.getSource() == deleteButton) {
				tableModel.delVoc(selectIndex);
			}
		}

	}
	
	private class FileAction implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			if (e.getSource() == openDoc) {
		
				
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
				int result = fileChooser.showOpenDialog(vm);
				if (result == JFileChooser.APPROVE_OPTION) {
					File selectedFile = fileChooser.getSelectedFile();
					if (selectedFile.canRead()) {
						FileReader filereader;
						try {
							filereader = new FileReader(selectedFile);
							BufferedReader bufReader = new BufferedReader(filereader);
							String line = "";
							String tmpstr = "";
							boolean isStrEnd = false;
							while ((line = bufReader.readLine()) != null) {
								isStrEnd = false;
								for (int i = 0; i < line.length(); i++) {
									char c = line.charAt(i);
									if (('a' <= c && c <= 'z') || ('A' <= c && c <= 'Z') || c=='\'')  {
										tmpstr += c;
										isStrEnd = true;
									} else {
										if (isStrEnd) {
											tableModel.addVoc(tmpstr);
											tmpstr = "";
											isStrEnd = false;
										}
									}

								}
								if (tmpstr.length() != 0) {
									if (isStrEnd) {
										tableModel.addVoc(tmpstr);
				
										tmpstr = "";
										isStrEnd = false;
									}
								}
								
							}
							bufReader.close();
						} catch (Exception e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
				
					}
					else {
						System.out.println("can not read");
					}
				}
				
				
		
			} 
			else if (e.getSource() == openVoca) {
				System.out.println("ov");
			} 
			else if (e.getSource() == saveVoca) {
				System.out.println("sv");
			} 
		}

	}
	


	private class RowListener implements ListSelectionListener {
		public void valueChanged(ListSelectionEvent event) {
			if (event.getValueIsAdjusting()) {
				selectIndex = table.getSelectionModel().getLeadSelectionIndex();
				return;
			}

		}
	}

	class VocData {
		private int cnt;
		private String eng, kor;

		VocData(){}
		VocData(String eng, String kor, int cnt)
		{
		
			this.cnt = cnt;
			this.eng= eng;
			this.kor= kor;
		}
		public int getCnt() {
			return cnt;
		}

		public void setCnt(int cnt) {
			this.cnt = cnt;
		}

		public String getEng() {
			return eng;
		}

		public void seteng(String eng) {
			this.eng = eng;
		}

		public String getKor() {
			return kor;
		}

		public void setKor(String kor) {
			this.kor = kor;
		}

	}

}
