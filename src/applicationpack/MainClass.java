package applicationpack;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

class MainPanel extends JPanel
{
    private JLabel lblIDValue;
    private JTextField txtName, txtAddress, txtPhone;
    private JComboBox cbxSex, cbxCourse;
    private JButton btnAddNew, btnSubmit, btnCancel, btnSave, btnSearchAll, btnSearchID, btnSearchName, btnSearchCourse;
    private JTable tabStudentdata;
    private JScrollPane scpStudentData;
    private DefaultTableModel tblModel;
    private DefaultTableColumnModel tblColModel;
    private TableColumn tblColID, tblColName, tblColAddress, tblColPhone, tblColSex, tblColCourse;
    private String[] sex = {"MALE", "FEMALE", "OTHER"};
    private String[] course = {"BTech", "BCA", "BBA", "BSc", "MTech", "MCA", "MBA"};
    
    private Connection con = null;
    private Statement smt = null;
    private int studentID;
    private int lastRowIndex;
    private boolean unsaveData = false;
    private MainPanel mPanel = null;
    
    private JLabel makeLabel(String s,int x,int y,int w,int h,int mode)
    {
        JLabel temp = new JLabel(s);
        temp.setBounds(x, y, w, h);
	if(mode == 1)
	{
            Border b1 = BorderFactory.createLineBorder(Color.RED, 2);
            Border b2 = BorderFactory.createLineBorder(Color.WHITE, 2);
            Border b3 = BorderFactory.createCompoundBorder(b1, b2);
            temp.setFont(new Font("Verdana", 1, 32));
	    temp.setOpaque(true);
	    temp.setBackground(Color.BLUE);
            temp.setForeground(Color.WHITE);
            temp.setBorder(b3);
	    temp.setHorizontalAlignment(JLabel.CENTER);
	}
        else if(mode == 2)
        {
            temp.setFont(new Font("Courier New", 1, 18));
            temp.setHorizontalAlignment(JLabel.LEFT);
        }
        else if(mode == 3)
        {
            temp.setFont(new Font("Courier New", 1, 18));
            temp.setOpaque(true);
            temp.setBackground(Color.WHITE);
            temp.setHorizontalAlignment(JLabel.CENTER);
            temp.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        }
        add(temp);
        return(temp);
    }
    
    private JTextField makeTextField(int x,int y,int w,int h)
    {
        JTextField temp = new JTextField();
        temp.setBounds( x, y, w, h);
        Border b1 = BorderFactory.createLineBorder(Color.BLACK, 2);
	temp.setBorder(b1);
        temp.setFont(new Font("Courier New",1,18));
        temp.setHorizontalAlignment(JTextField.CENTER);
        temp.setEnabled(false);
        add(temp);
        return(temp);
    }
    
    private JComboBox makeComboBox(String[] item,int x,int y,int w,int h)
    {
        JComboBox temp = new JComboBox(item);
        temp.setFont(new Font("Courier New",1,18));
        temp.setBounds(x, y, w, h);
        temp.setEnabled(false);
        temp.setSelectedIndex(-1);
        add(temp);
        return temp;
    }
    
    private JButton makeButton (String s,int x,int y,int w,int h)
    {
        JButton temp = new JButton(s);
        temp.setBounds( x, y, w, h);
        temp.setOpaque(true);
        temp.setFont(new Font("Courier New",1,16));
	temp.setMargin(new Insets(0,0,0,0));
        temp.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                Object ob = e.getSource();
                if(ob == btnAddNew)
                {
                    setEnabelDisable();
                    setReset();
                    studentID++;
                    lblIDValue.setText(String.valueOf(studentID));
                }
                else if(ob == btnSubmit)
                {
                    String id = lblIDValue.getText();
                    String name = txtName.getText();
                    String addr = txtAddress.getText();
                    String phon = txtPhone.getText();
                    String sex = (String)cbxSex.getSelectedItem();
                    String cors = (String)cbxCourse.getSelectedItem();
                    tblModel.addRow(new String[]{id, name, addr, phon, sex, cors});
                    setEnabelDisable();
                    btnSave.setEnabled(true);
                    unsaveData = true;
                }
                else if(ob == btnCancel)
                {
                    setEnabelDisable();
                    studentID--;
                }
                else if(ob == btnSave)
                {
                    saveRecord();
                    unsaveData = false;
                }
                else if(ob == btnSearchAll)
                {
                    try
                    {
                        if(unsaveData)
                        {
                            saveRecord();
                            unsaveData = false;
                        }
                        tblModel.setRowCount(0);
                        String qry = "SELECT ID, NAME, ADDRESS, PHONE, SEX, COURSE FROM STUDENT ORDER BY ID";
                        populateTable(qry);
                    }
                    catch(Exception ex)
                    {
                        JOptionPane.showMessageDialog(null, ex);
                    }
                }
                else if(ob == btnSearchID)
                {
                    try
                    {
                        if(unsaveData)
                        {
                            saveRecord();
                            unsaveData = false;
                        }
                        tblModel.setRowCount(0);
                        System.setProperty("qry_on", "ID");
                        createsearchFrame("Searching Student By ID","Enter Student ID", mPanel);
                    }
                    catch(Exception ex)
                    {
                        JOptionPane.showMessageDialog(null, ex);
                    }
                }
                else if(ob == btnSearchName)
                {
                    try
                    {
                        if(unsaveData)
                        {
                            saveRecord();
                            unsaveData = false;
                        }
                        tblModel.setRowCount(0);
                        System.setProperty("qry_on", "Name");
                        createsearchFrame("Searching Student By Name","Enter Student Name", mPanel);
                    }
                    catch(Exception ex)
                    {
                        JOptionPane.showMessageDialog(null, ex);
                    }
                }
                else if(ob == btnSearchCourse)
                {
                    try
                    {
                        if(unsaveData)
                        {
                            saveRecord();
                            unsaveData = false;
                        }
                        tblModel.setRowCount(0);
                        System.setProperty("qry_on", "Course");
                        createsearchFrame("Searching Student By Course","Enter Course Name", mPanel);
                    }
                    catch(Exception ex)
                    {
                        JOptionPane.showMessageDialog(null, ex);
                    }
                }
            }
        });
        add(temp);
        return(temp);
    }
    
    private TableColumn makeTableColumn(int i, String cap, int width, DefaultTableCellRenderer centerRenderer)
    {
        TableColumn temp = new TableColumn(i);
        temp.setHeaderValue(cap);
        temp.setMaxWidth(width);
        temp.setCellRenderer(centerRenderer);
        tblColModel.addColumn(temp);
        return temp;
    }
    
    private void applicationInit()
    {
        try
        {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE", "SCOTT", "TIGER");
            DatabaseMetaData dbMeta = con.getMetaData();
            ResultSet dbaseRset = dbMeta.getTables(null,"SCOTT","STUDENT",new String[]{"TABLE"});
            smt = con.createStatement();
            String qry = "";
            studentID = 1000;
            if(!dbaseRset.next())
            {
                qry = "CREATE TABLE STUDENT(ID NUMBER(4) PRIMARY KEY, NAME VARCHAR2(20), ADDRESS VARCHAR2(20), PHONE VARCHAR2(10), SEX VARCHAR2(6), COURSE VARCHAR2(5))";
                smt.executeUpdate(qry);
            }
            else
            {
                qry = "SELECT ID, NAME, ADDRESS, PHONE, SEX, COURSE FROM STUDENT ORDER BY ID";
                populateTable(qry);
            }
        }
        catch(Exception ex)
        {
            JOptionPane.showMessageDialog(null, ex);
        }
    }
    
    private void populateTable(String qry)
    {
        try
        {
            ResultSet rset = smt.executeQuery(qry);
            studentID = 1000;
            while(rset.next())
            {
                studentID = rset.getInt(1);
                String name = rset.getString(2);
                String addr = rset.getString(3);
                String phon = rset.getString(4);
                String sx  = rset.getString(5);
                String cour = rset.getString(6);
                tblModel.addRow(new String[]{String.valueOf(studentID),name,addr,phon,sx,cour});
                lastRowIndex = rset.getRow();
            }
        }
        catch(Exception ex)
        {
            JOptionPane.showMessageDialog(null, ex);
        }
        
    }
    
    private String getColCaption(String str)
    {
        String temp = "<html><p style='font-family: Verdana, Geneva, Tahoma, sans-serif; font-weight: bold; font-size: 13pt;'>"+str+"</p></html>";
        return temp;
    }
    
    private void setEnabelDisable()
    {
        txtName.setEnabled(!txtName.isEnabled());
        txtAddress.setEnabled(!txtAddress.isEnabled());
        txtPhone.setEnabled(!txtPhone.isEnabled());
        cbxSex.setEnabled(!cbxSex.isEnabled());
        cbxCourse.setEnabled(!cbxCourse.isEnabled());
        btnAddNew.setEnabled(!btnAddNew.isEnabled());
        btnSubmit.setEnabled(!btnSubmit.isEnabled());
        btnCancel.setEnabled(!btnCancel.isEnabled());
        btnSave.setEnabled(!btnSave.isEnabled());
        btnSearchAll.setEnabled(!btnSearchAll.isEnabled());
        btnSearchID.setEnabled(!btnSearchID.isEnabled());
        btnSearchName.setEnabled(!btnSearchName.isEnabled());
        btnSearchCourse.setEnabled(!btnSearchCourse.isEnabled());
    }
    
    private void setReset()
    {
        lblIDValue.setText("");
        txtName.setText("");
        txtAddress.setText("");
        txtPhone.setText("");
        cbxSex.setSelectedIndex(-1);
        cbxCourse.setSelectedIndex(-1);
        txtName.grabFocus();
    }
    
    private void saveRecord()
    {
        try
        {
            String qry = "";
            for(;lastRowIndex<tabStudentdata.getRowCount();lastRowIndex++)
            {
                int id = Integer.parseInt((String)tabStudentdata.getValueAt(lastRowIndex, 0));
                String name = "'"+(String)tabStudentdata.getValueAt(lastRowIndex, 1)+"'";
                String addr = "'"+(String)tabStudentdata.getValueAt(lastRowIndex, 2)+"'";
                String phon = "'"+(String)tabStudentdata.getValueAt(lastRowIndex, 3)+"'";
                String sex  = "'"+(String)tabStudentdata.getValueAt(lastRowIndex, 4)+"'";
                String cors = "'"+(String)tabStudentdata.getValueAt(lastRowIndex, 5)+"'";
                qry = "INSERT INTO STUDENT VALUES("+id+","+name+","+addr+","+phon+","+sex+","+cors+")";
                smt.executeUpdate(qry);
            }
            btnSave.setEnabled(false);
            unsaveData = false;
            JOptionPane.showMessageDialog(null, "Data Saved Successfully");
        }
        catch(Exception ex)
        {
            JOptionPane.showMessageDialog(null, ex);
        }
    }
    
    private void createsearchFrame(String title, String caption, MainPanel mPanel)
    {
        int height = 220;
        int width = 430;
        SearchFrame searchframe = new SearchFrame(title, caption, mPanel);
        searchframe.setSize(width, height);
        searchframe.setModal(true);
        searchframe.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        searchframe.setTitle(title);
        searchframe.setResizable(false);
        searchframe.setLocationRelativeTo(null);
        searchframe.setVisible(true);
    }
    
    protected void prepareQuery(String qryArgumentString)
    {
        if(qryArgumentString != null)
        {
            String qry = "";
            String key = System.getProperty("qry_on");
            if(key.equals("ID"))
                qry = "SELECT ID, NAME, ADDRESS, PHONE, SEX, COURSE FROM STUDENT WHERE ID = "+Integer.parseInt(qryArgumentString)+"ORDER BY ID";
            else if(key.equals("Name"))
                qry = "SELECT ID, NAME, ADDRESS, PHONE, SEX, COURSE FROM STUDENT WHERE NAME LIKE '"+qryArgumentString+"%' ORDER BY ID";
            else if(key.equals("Course"))
                qry = "SELECT ID, NAME, ADDRESS, PHONE, SEX, COURSE FROM STUDENT WHERE COURSE = '"+qryArgumentString+"' ORDER BY ID";
            populateTable(qry);
        }
    }
    
    public MainPanel(int width)
    {
        mPanel = this;
        makeLabel("STUDENT RECORD MANAGEMENT SYSTEM", 10, 10, width-25, 60, 1);
        makeLabel("STUDENT ID",   10, 80, 200, 30, 2);
        lblIDValue = makeLabel("", 200, 80, 300, 30, 3);
        makeLabel("STUDENT NAME", 10, 120, 200, 30, 2);
        makeLabel("ADDRESS",      10, 160, 200, 30, 2);
        makeLabel("PHONE NUMBER", 10, 200, 200, 30, 2);
        makeLabel("SEX",          10, 240, 200, 30, 2);
        makeLabel("COURSE",      260, 240, 200, 30, 2);
        txtName    = makeTextField(200, 120, 300, 30);
        txtAddress = makeTextField(200, 160, 300, 30);
        txtPhone   = makeTextField(200, 200, 300, 30);
        cbxSex     = makeComboBox(sex, 80, 240, 140, 30);
        cbxCourse  = makeComboBox(course, 360, 240, 140, 30);
        btnAddNew  = makeButton("Add New", 10, 280, 100, 30);
        btnSubmit  = makeButton("Submit",  140, 280, 100, 30);
        btnSubmit.setEnabled(false);
        btnCancel  = makeButton("Cancel", 270, 280, 100, 30);
        btnCancel.setEnabled(false);
        btnSave  = makeButton("Save", 400, 280, 100, 30);
        btnSave.setEnabled(false);
        btnSearchAll    = makeButton("Search All", 10, 320, 230, 30);
        btnSearchAll.setEnabled(true);
        btnSearchID     = makeButton("Search By ID", 270, 320, 230, 30);
        btnSearchID.setEnabled(true);
        btnSearchName   = makeButton("Search by Name", 10, 360, 230, 30);
        btnSearchName.setEnabled(true);
        btnSearchCourse = makeButton("Search by Course", 270, 360, 230, 30);
        btnSearchCourse.setEnabled(true);
        
        int tableWidth = width-525;
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        
        tblColModel = new DefaultTableColumnModel();
        tblColID      = makeTableColumn(0, getColCaption("ID No"),        (int)(tableWidth*0.10), centerRenderer);
        tblColName    = makeTableColumn(1, getColCaption("STUDENT NAME"), (int)(tableWidth*0.25), centerRenderer);
        tblColAddress = makeTableColumn(2, getColCaption("ADDRESS"),      (int)(tableWidth*0.25), centerRenderer);
        tblColPhone   = makeTableColumn(3, getColCaption("PHONE NUMBER"), (int)(tableWidth*0.20), centerRenderer);
        tblColSex     = makeTableColumn(4, getColCaption("SEX"),          (int)(tableWidth*0.10), centerRenderer);
        tblColCourse  = makeTableColumn(5, getColCaption("COURSE"),       (int)(tableWidth*0.10), centerRenderer);
        tblModel = new DefaultTableModel();
        tblModel.setColumnCount(6);
        tabStudentdata  = new JTable(tblModel, tblColModel);
        tabStudentdata.setFont(new Font("Courier New", 1, 16));
        tabStudentdata.setRowHeight(30);
        tabStudentdata.setEnabled(false);
        scpStudentData  = new JScrollPane(tabStudentdata);
        scpStudentData.setBounds(510, 80, tableWidth, 310);
        scpStudentData.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        super.add(scpStudentData);
        
        applicationInit();
    }

}
class MainFrame extends JFrame
{
    public MainFrame(int width)
    {
        MainPanel panel = new MainPanel(width);
        panel.setBackground(new Color(220, 250, 200));
        panel.setLayout(new BorderLayout());
        add(panel);
    }
}
public class MainClass
{
    public static void main(String[] args)
    {
        Toolkit tk = Toolkit.getDefaultToolkit();
        int height = 450;
        int width = tk.getScreenSize().width;
        MainFrame frame = new MainFrame(width);
        frame.setSize(width, height);
        frame.setTitle("Student Record Management System");
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
    }
}
