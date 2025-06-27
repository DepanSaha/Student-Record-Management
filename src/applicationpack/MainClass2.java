package applicationpack;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;

class SearchPanel extends JPanel implements ActionListener
{
    private JLabel lblCap, lblMsg;
    private JTextField txtValue;
    private JButton btnSubmit, btnCancel;
    private MainPanel mp;
    
    private JLabel makeLabel(String s,int x,int y,int w,int h,int mode)
    {
        JLabel temp = new JLabel(s);
        temp.setBounds(x, y, w, h);
	if(mode == 1)
	{
            Border b1 = BorderFactory.createLineBorder(Color.RED, 2);
            Border b2 = BorderFactory.createLineBorder(Color.WHITE, 2);
            Border b3 = BorderFactory.createCompoundBorder(b1, b2);
            temp.setFont(new Font("Verdana", 1, 20));
	    temp.setOpaque(true);
	    temp.setBackground(Color.BLUE);
            temp.setForeground(Color.WHITE);
            temp.setBorder(b3);
	    temp.setHorizontalAlignment(JLabel.CENTER);
	}
        else if(mode == 2)
        {
            temp.setFont(new Font("Courier New", 1, 16));
            temp.setHorizontalAlignment(JLabel.LEFT);
        }
        add(temp);
        return(temp);
    }
    
    private JTextField makeTextField(int x,int y,int w,int h)
    {
        JTextField temp = new JTextField();
        temp.setBounds(x, y, w, h);
        Border b1 = BorderFactory.createLineBorder(Color.BLACK, 2);
	temp.setBorder(b1);
        temp.setFont(new Font("Courier New",1,18));
        temp.setHorizontalAlignment(JTextField.CENTER);
        add(temp);
        return(temp);
    }
    
    private JButton makeButton (String s,int x,int y,int w,int h)
    {
        JButton temp = new JButton(s);
        temp.setBounds( x, y, w, h);
        temp.setOpaque(true);
        temp.setFont(new Font("Courier New",1,16));
	temp.setMargin(new Insets(0,0,0,0));
        temp.addActionListener(this);
        add(temp);
        return(temp);
    }

    public SearchPanel(String frameTitle, String labelCaption, MainPanel mPanel)
    {
        mp = mPanel;
        lblCap = makeLabel(frameTitle, 10, 10, 400, 45, 1);
        lblMsg = makeLabel(labelCaption, 10, 70, 200, 30, 2);
        txtValue = makeTextField(210, 70, 200, 30);
        btnSubmit = makeButton("Submit", 30, 120, 150, 30);
        btnCancel = makeButton("Cancel", 240, 120, 150, 30);
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        Object ob = e.getSource();
        if(ob == btnSubmit)
            mp.prepareQuery(txtValue.getText());
        else if(ob == btnCancel)
            mp.prepareQuery(null);
        SearchFrame frame = (SearchFrame)SwingUtilities.getWindowAncestor(this);
        frame.setVisible(false);
    }
}

class SearchFrame extends JDialog
{
    private SearchPanel searchPanel = null;
    public SearchFrame(String frameTitle, String labelCaption, MainPanel mPanel)
    {
        searchPanel = new SearchPanel(frameTitle, labelCaption, mPanel);
        searchPanel.setBackground(new Color(220, 250, 200));
        searchPanel.setLayout(new BorderLayout());
        super.add(searchPanel);
    }
}